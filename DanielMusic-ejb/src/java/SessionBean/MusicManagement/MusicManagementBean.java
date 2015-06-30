package SessionBean.MusicManagement;

import EntityManager.Account;
import EntityManager.Album;
import EntityManager.Artist;
import EntityManager.Band;
import EntityManager.Genre;
import EntityManager.Music;
import EntityManager.ReturnHelper;
import EntityManager.SearchHelper;
import SessionBean.AccountManagement.AccountManagementBeanLocal;
import SessionBean.CommonInfrastructure.CommonInfrastructureBeanLocal;
import it.sauronsoftware.jave.AudioAttributes;
import java.io.File;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.Part;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

@Stateless
public class MusicManagementBean implements MusicManagementBeanLocal {

    @EJB
    private CommonInfrastructureBeanLocal cibl;
    @EJB
    private AccountManagementBeanLocal ambl;

    @PersistenceContext(unitName = "DanielMusic-ejbPU")
    private EntityManager em;

    @Override
    public ReturnHelper encodeToMP3(File sourceFileName, File targetFileName, int bitrate) {
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            Encoder encoder = new Encoder();
            AudioAttributes aa = new AudioAttributes();
            aa.setCodec("libmp3lame");
            aa.setBitRate(new Integer(bitrate * 000));
            aa.setChannels(new Integer(2));
            aa.setSamplingRate(new Integer(44100));
            EncodingAttributes ea = new EncodingAttributes();
            ea.setFormat("mp3");
            ea.setAudioAttributes(aa);
            encoder.encode(sourceFileName, targetFileName, ea);
            result.setResult(true);
            result.setDescription("mp3 file encoded successfully.");
            return result;
        } catch (EncoderException ex) {
            System.out.println("encodeWavToMP3(): Error, " + ex.getMessage());
            result.setDescription("Error in converting the provided wav file to mp3. Please try again.");
        } catch (Exception ex) {
            System.out.println("encodeWavToMP3(): Unknown error.");
            result.setDescription("Error in converting the provided wav file to mp3. Please try again.");
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public String generateDownloadLink(String fileLocation, Long musicID, Boolean isIncreaseDownloadCount) {
        System.out.println("generateDownloadLink() called");
        try {
            ReturnHelper helper = new ReturnHelper();
            Query q = em.createQuery("select a from Artist a where a.email=:email and a.isDisabled=false and a.emailIsVerified=true");
            Artist artist = (Artist) q.getSingleResult();
            Music music = em.getReference(Music.class, musicID);

            //generate download link for user
            music.setNumDownloaded(music.getNumDownloaded() + 1);
            String downloadLink = cibl.getFileURLFromGoogleCloudStorage("music/" + artist.getId() + "/" + music.getAlbum().getId() + "/" + music.getName() + ".mp3", 120L);//2mins expiry
            helper.setDescription(downloadLink);
            helper.setResult(true);

        } catch (Exception e) {
            System.out.println("Error. Failed to generateDownloadLink()");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Music> searchMusicByGenre(Long genreID) {
        System.out.println("searchMusicByGenre() called with genreID: " + genreID);
        try {
            Query q = em.createQuery("SELECT m FROM Music m, Album a WHERE a.listOfMusics.id=m.id and m.listOfGenres.id=:genreID AND m.isDeleted=false AND a.isDeleted=FALSE AND a.isPublished=true ORDER BY a.publishedDate DESC ");
            q.setParameter("genreID", genreID);
            List<Music> listOfMusics = q.getResultList();
            System.out.println("searchMusicByGenre() successful");

            return listOfMusics;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while calling searchMusicByGenre()");
            return null;
        }
    }

    @Override
    public SearchHelper search(String searchString) {
        System.out.println("search() called with searchString: " + searchString);
        try {
            Query q;
            SearchHelper helper = new SearchHelper();

            q = em.createQuery("SELECT a FROM Album a WHERE a.name LIKE '%:searchString%' AND a.isDeleted=false AND a.isPublished=true ORDER BY a.publishedDate DESC");
            q.setParameter("searchString", searchString);
            List<Album> listOfAlbums = q.getResultList();

            q = em.createQuery("SELECT a FROM Artist a WHERE a.name LIKE '%:searchString%' AND a.isDisabled=false AND a.isApproved=true");
            q.setParameter("searchString", searchString);
            List<Artist> listOfArtists = q.getResultList();

            q = em.createQuery("SELECT m FROM Music m WHERE m.name LIKE '%:searchString%' AND m.isDeleted=false AND m.album.isPublished=true ORDER BY m.album.publishedDate DESC");
            q.setParameter("searchString", searchString);
            List<Music> listOfMusics = q.getResultList();

            helper.setListOfAlbums(listOfAlbums);
            helper.setListOfArtists(listOfArtists);
            helper.setListOfMusics(listOfMusics);

            System.out.println("search() successful");

            return helper;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while calling search()");
            return null;
        }
    }

    @Override
    public ReturnHelper createMusic(Part musicPart, Long albumID, Integer trackNumber, String name, Double price, String lyrics, Integer yearReleased) {
        ReturnHelper helper = new ReturnHelper();
        helper.setResult(false);
        try {
            Album album = null;
            //Check if album is published
            if (albumID == null) {
                helper.setDescription("Please check that the album exists for this music.");
                return helper;
            } else {
                Query q = em.createQuery("SELECT E FROM Album E where E.id=:id");
                q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                q.setParameter("id", albumID);
                album = (Album) q.getSingleResult();
                if (album.getIsPublished()) {
                    helper.setDescription("Unable to edit a published album.");
                    return helper;
                }
            }

            String fileName = musicPart.getSubmittedFileName();
            String tempMusicURL = "temp/music_" + fileName + cibl.generateUUID();
            System.out.println("file name is " + fileName);
            InputStream fileInputStream = musicPart.getInputStream();
            OutputStream fileOutputStream = new FileOutputStream(tempMusicURL);

            System.out.println("writing to... " + tempMusicURL);
            int nextByte;
            while ((nextByte = fileInputStream.read()) != -1) {
                fileOutputStream.write(nextByte);
            }
            fileOutputStream.close();
            fileInputStream.close();

            File file = new File(tempMusicURL);

            //check if the music >10mins, if more than 10mins return ReturnHelper
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            double durationInSeconds = (frames + 0.0) / format.getFrameRate();
            if (durationInSeconds > 600) {
                helper.setDescription("The track duration cannot be more than 10mins, please upload a shorter duration.");
                return helper;
            }

            File newFile128 = new File(tempMusicURL + "128");
            File newFile320 = new File(tempMusicURL + "320");
            encodeToMP3(file, newFile128, 128);
            encodeToMP3(file, newFile320, 320);

            //create music entity
            Music music = new Music();
            music.setAlbum(album);
            music.setArtistName(album.getArtist().getName());
            ArrayList<Genre> listOfGenres = new ArrayList<Genre>();
            music.setLyrics(lyrics);
            music.setListOfGenres(listOfGenres);
            music.setName(name);
            music.setPrice(price);
            music.setTrackNumber(trackNumber);
            music.setYearReleased(yearReleased);
            em.persist(music);
            em.flush();

            String musicURL128;
            String musicURL320;
            String musicURLwav;
            Artist artist = album.getArtist();

            if (artist != null) {
                musicURL128 = "music/" + album.getArtist().getId() + "/" + album.getId() + "/" + music.getId() + "/128/" + fileName;
                musicURL320 = "music/" + album.getArtist().getId() + "/" + album.getId() + "/" + music.getId() + "/320/" + fileName;
                musicURLwav = "music/" + album.getArtist().getId() + "/" + album.getId() + "/" + music.getId() + "/wav/" + fileName;
            } else {
                musicURL128 = "music/" + album.getBand().getId() + "/" + album.getId() + "/" + music.getId() + "/128/" + fileName;
                musicURL320 = "music/" + album.getBand().getId() + "/" + album.getId() + "/" + music.getId() + "/320/" + fileName;
                musicURLwav = "music/" + album.getBand().getId() + "/" + album.getId() + "/" + music.getId() + "/wav/" + fileName;
            }

            music.setFileLocation128(musicURL128);
            music.setFileLocation320(musicURL320);
            music.setFileLocationWAV(musicURLwav);

            //end create music
            ReturnHelper result1 = cibl.uploadFileToGoogleCloudStorage(musicURL128, tempMusicURL + "128", false, false);
            ReturnHelper result2 = cibl.uploadFileToGoogleCloudStorage(musicURL320, tempMusicURL + "320", false, false);
            ReturnHelper result3 = cibl.uploadFileToGoogleCloudStorage(musicURLwav, tempMusicURL, false, false);

            if (result1.getResult() && result2.getResult() && result3.getResult()) {
                helper.setDescription("Track has been uploaded successfully.");
            } else {
                helper.setDescription("Error occurred while uploading track... Please check that the track is in the correct format.");
                //delete music entity
                em.refresh(music);
                em.remove(music);
            }

            System.out.println("deleting file... " + file.delete());
            System.out.println("deleting file newFile128... " + newFile128.delete());
            System.out.println("deleting file newFile320... " + newFile320.delete());
            helper.setResult(true);
            return helper;
        } catch (Exception e) {
            e.printStackTrace();
            helper.setDescription("Error occurred while creating track, please try again.");
            helper.setResult(false);
            return helper;
        }
    }

    @Override
    public Music getMusic(Long musicID) {
        System.out.println("MusicManagementBean: getMusic() called");
        Music music;
        try {
            music = em.getReference(Music.class, musicID);
            Boolean isDeleted = music.getIsDeleted();
            if (isDeleted) {
                System.out.println("MusicManagementBean: Failed to getMusic(), music has been deleted");
                return null;
            } else {
                System.out.println("MusicManagementBean: Successfully called getMusic(), music retrieved");
                return music;
            }
        } catch (Exception e) {
            System.out.println("MusicManagementBean: Error occurred while trying to call getMusic()");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ReturnHelper deleteMusic(Long musicID) {
        System.out.println("MusicManagementBean: deleteMusic() called");
        ReturnHelper helper = new ReturnHelper();
        Music music;
        try {

            Query q = em.createQuery("SELECT E FROM Music E where E.id=:id");
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            q.setParameter("id", musicID);
            music = (Music) q.getSingleResult();
            Long numPurchase = music.getNumPurchase();
            //Check if album is published
            if (music == null) {
                helper.setDescription("Please check that the album exists for this music.");
                return helper;
            } else {
                if (music.getAlbum().getIsPublished()) {
                    helper.setDescription("Unable to edit a published album.");
                    return helper;
                }
            }

            if (numPurchase > 0L) {
                music.setIsDeleted(true);
            } else {
                em.remove(music);
            }
            System.out.println("MusicManagementBean: deleteMusic() successfully");
            helper.setDescription("The track has been deleted successfully.");
            helper.setResult(true);
            return helper;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("MusicManagementBean: Error occurred while calling deleteMusic()");
            helper.setDescription("Error occurred while trying to delete the track, please try again.");
            helper.setResult(false);
            return helper;
        }
    }

    @Override
    public ReturnHelper editMusic(Long musicID, Integer trackNumber, String name, Double price, String lyrics, String credits) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Music> ListAllTracksByAlbumID(Long albumID) {
        System.out.println("MusicManagementBean: ListAllTracksByAlbumID() called");
        try {
            Query q = em.createQuery("select a from Music a where a.isDeleted=false AND a.album.id=:albumID");
            q.setParameter("albumID", albumID);
            List<Music> albums = q.getResultList();
            return albums;
        } catch (Exception ex) {
            System.out.println("ListAllTracksByAlbumID() failed");
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public ReturnHelper createAlbum(Part imagePart, String name, String description, Long artistOrBandID, Integer yearReleased, String credits, Double price) {
        System.out.println("createAlbum() called");
        ReturnHelper helper = new ReturnHelper();
        try {
            ReturnHelper result = null;
            String imageLocation = null;
            String tempImageURL = null;
            Boolean isArtist = null;

            Account account = em.getReference(Account.class, artistOrBandID);
            Artist artist = null;
            Band band = null;
            if (account instanceof Artist) {
                artist = (Artist) account;
                isArtist = true;
            } else {
                band = (Band) account;
                isArtist = false;
            }

            Album album = new Album();

            if (isArtist) {
                album.setArtist(artist);
            } else {
                album.setBand(band);
            }

            album.setDescription(description);
            album.setName(name);
            album.setYearReleased(yearReleased);
            album.setCredits(credits);
            album.setPrice(price);
            em.persist(album);
            em.flush();
            System.out.println("MusicManagementBean: em.flush(). Album has been persisted.");
            em.refresh(album);
            System.out.println("MusicManagementBean: em.refresh(). Album ID: " + album.getId());

            //check whether user uploads an image
            if (imagePart != null) {
                if (imagePart.getSize() > 5000000) {
                    em.remove(album);
                    helper.setDescription("Image failed to upload, please check the file size is less than 5MB and create album again.");
                    helper.setResult(false);
                    return helper;
                }
                String fileName = imagePart.getSubmittedFileName();
                tempImageURL = "temp/albumart_" + fileName + cibl.generateUUID();
                System.out.println("file name is " + fileName);
                InputStream fileInputStream = imagePart.getInputStream();
                OutputStream fileOutputStream = new FileOutputStream(tempImageURL);

                System.out.println("writing to... " + tempImageURL);
                int nextByte;
                while ((nextByte = fileInputStream.read()) != -1) {
                    fileOutputStream.write(nextByte);
                }
                fileOutputStream.close();
                fileInputStream.close();
                imageLocation = "image/album/" + album.getId() + "/albumart/" + name + ".jpg";
                result = cibl.uploadFileToGoogleCloudStorage(imageLocation, tempImageURL, true, true);

                File file = new File(tempImageURL);
                System.out.println("deleting file... " + file.delete());

                if ((result != null)) {
                    if (result.getResult()) {
                        System.out.println("Image location set... " + imageLocation);
                        album.setImageLocation(imageLocation);
                        em.merge(album);
                    } else {
                        em.remove(album);
                        helper.setDescription("Image failed to upload, please check the file uploaded is an image and create album again.");
                        helper.setResult(false);
                        return helper;
                    }
                }
            }

            System.out.println("Album created successfully.");
            helper.setID(album.getId());
            helper.setDescription("Album has been created successfully.");
            helper.setResult(true);
            return helper;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("MusicManagementBean: Error occurred while calling createAlbum()");
            helper.setDescription("Error occurred while trying to create album, please try again.");
            helper.setResult(false);
            return helper;
        }
    }

    @Override
    public Album getAlbum(Long albumID) {
        System.out.println("getAlbum() called.");
        try {
            Album album = em.getReference(Album.class, albumID);
            System.out.println("getAlbum() called successfully");
            return album;
        } catch (Exception e) {
            System.out.println("Error while calling getAlbum()");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Album> ListAllAlbumByArtistorBandID(Long artistOrBandAccountID, Boolean showUnpublished, Boolean showUnapproved) {
        System.out.println("getAlbumByArtists() called");
        try {
            Query q = null;
            Account account = ambl.getAccount(artistOrBandAccountID);
            if (account instanceof Artist) {
                if (showUnpublished) {
                    q = em.createQuery("select a from Album a where (a.artist.id=:artistID AND a.artist.isApproved>=:isApproved) and a.isDeleted=false");
                } else {
                    q = em.createQuery("select a from Album a where (a.artist.id=:artistID AND a.artist.isApproved>=:isApproved) and a.isDeleted=false and a.isPublished=:isPublished");
                    q.setParameter("isPublished", true);
                }
                q.setParameter("artistID", artistOrBandAccountID);
            } else if (account instanceof Band) {
                if (showUnpublished) {
                    q = em.createQuery("select a from Album a where (a.band.id=:bandID AND a.band.isApproved>=:isApproved) and a.isDeleted=false");
                } else {
                    q = em.createQuery("select a from Album a where (a.band.id=:bandID AND a.band.isApproved>=:isApproved) and a.isDeleted=false and a.isPublished=:isPublished");
                    q.setParameter("isPublished", true);
                }
                q.setParameter("bandID", artistOrBandAccountID);
            } else {
                throw new Exception("ID given is not artist or band");
            }
            if (showUnapproved) {
                q.setParameter("isApproved", -2);
            } else {
                q.setParameter("isApproved", 1);
            }
            List<Album> albums = q.getResultList();
            return albums;
        } catch (Exception ex) {
            System.out.println("getAlbumByArtists() failed");
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public ReturnHelper editAlbum(Long albumID, Part imagePart, String name, String description, Integer yearReleased, String credits, Double price) {
        System.out.println("editAlbum() called.");
        ReturnHelper helper = new ReturnHelper();
        try {
            Album album = null;
            //Check if album is published
            if (albumID == null) {
                helper.setDescription("Please check that the album exists for this music.");
                return helper;
            } else {
                Query q = em.createQuery("SELECT E FROM Album E where E.id=:id");
                q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                q.setParameter("id", albumID);
                album = (Album) q.getSingleResult();
                if (album.getIsPublished()) {
                    helper.setDescription("Unable to edit a published album.");
                    return helper;
                }
            }

            if (album.getIsPublished()) {
                System.out.println("Album is already published, cannot be edited.");
                helper.setDescription("Album has been published and cannot be edited.");
                helper.setResult(false);
                return helper;
            } else {
                album.setName(name);
                album.setDescription(description);
                album.setYearReleased(yearReleased);
                album.setCredits(credits);
                album.setPrice(price);

                if (imagePart != null && imagePart.getSize() < 5000000) {
                    String imageLocation = null;
                    String tempImageURL = null;
                    String fileName = imagePart.getSubmittedFileName();
                    tempImageURL = "temp/" + fileName + cibl.generateUUID();
                    System.out.println("file name is " + fileName);
                    InputStream fileInputStream = imagePart.getInputStream();
                    OutputStream fileOutputStream = new FileOutputStream(tempImageURL);

                    System.out.println("writing to... " + tempImageURL);
                    int nextByte;
                    while ((nextByte = fileInputStream.read()) != -1) {
                        fileOutputStream.write(nextByte);
                    }
                    fileOutputStream.close();
                    fileInputStream.close();
                    ReturnHelper result = new ReturnHelper();

                    //check whether there is previous image
                    //YG- no need to delete before upload
//                    if (album.getImageLocation() != null) {
//                        result = commonInfrastructureBean.deleteFileFromGoogleCloudStorage(album.getImageLocation());
//                        if (!result.getResult()) {
//                            album.setImageLocation(null);
//                            helper.setDescription("Error while editing album, please try again.");
//                            helper.setResult(false);
//                            return helper;
//                        }
//                    }
                    //check whether album belongs to artist/band
                    if (album.getArtist() != null) {
                        imageLocation = "image/album/" + album.getId() + "/albumart/" + name + ".jpg";
                    } else {
                        imageLocation = "image/album/" + album.getId() + "/albumart/" + name + ".jpg";
                    }

                    result = cibl.uploadFileToGoogleCloudStorage(imageLocation, tempImageURL, true, true);
                    File file = new File(tempImageURL);
                    System.out.println("deleting file... " + file.delete());

                    if (result.getResult()) {
                        album.setImageLocation(imageLocation);
                    } else {
                        helper.setDescription("Error while editing album, please try again.");
                        helper.setResult(false);
                        return helper;
                    }
                }

                helper.setDescription("Album details have been updated successfully.");
                helper.setResult(true);
                return helper;
            }
        } catch (Exception e) {
            System.out.println("Error while calling editAlbum()");
            e.printStackTrace();
            helper.setDescription("Error while editing album, please try again.");
            helper.setResult(false);
            return helper;
        }
    }

    @Override
    public ReturnHelper publishAlbum(Long albumID) {
        System.out.println("publishAlbum() called.");
        ReturnHelper helper = new ReturnHelper();
        try {
            Query q = em.createQuery("SELECT E FROM Album E where E.id=:id");
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            q.setParameter("id", albumID);
            Album album = (Album) q.getSingleResult();
            Boolean isArtist = false;
            Boolean isBand = false;
            if (album.getArtist() != null) {
                isArtist = true;
                if (!album.getArtist().getEmailIsVerified()) {
                    helper.setDescription("Sorry your email is not verified, please verify your email first.");
                    helper.setResult(false);
                    return helper;
                } else if (album.getArtist().getPaypalEmail() == null || album.getArtist().getPaypalEmail().length() == 0) {
                    helper.setDescription("Sorry your PayPal email is not filled in, please edit your profile first.");
                    helper.setResult(false);
                    return helper;
                }
            } else {
                isBand = true;
                if (!album.getBand().getEmailIsVerified()) {
                    helper.setDescription("Sorry your email is not verified, please verify your email first.");
                    helper.setResult(false);
                    return helper;
                }else if (album.getBand().getPaypalEmail() == null || album.getBand().getPaypalEmail().length() == 0) {
                    helper.setDescription("Sorry your PayPal email is not filled in, please edit your profile first.");
                    helper.setResult(false);
                    return helper;
                }
            }

            if (isArtist) {
                if (album.getArtist().getIsApproved() == 0 || album.getArtist().getIsApproved() == -1) {
                    album.getArtist().setIsApproved(-2);
                }
            } else if (isBand) {
                if (album.getBand().getIsApproved() == 0 || album.getBand().getIsApproved() == -1) {
                    album.getBand().setIsApproved(-2);
                }
            }
            if (album.getListOfMusics() == null || album.getListOfMusics().isEmpty()) {
                helper.setDescription("The album cannot be published, no tracks found.");
                helper.setResult(false);
                return helper;
            }
            album.setIsPublished(true);
            helper.setDescription("Album has been published successfully.");
            helper.setResult(true);
            return helper;

        } catch (Exception e) {
            e.printStackTrace();
            helper.setDescription("Error occurred while trying to publish album, please try again later.");
            helper.setResult(false);
            return helper;
        }
    }

    @Override
    public ReturnHelper deleteAlbum(Long albumID) {
        System.out.println("MusicManagementBean: deleteAlbum() called.");
        ReturnHelper helper = new ReturnHelper();
        try {
            Album album = em.getReference(Album.class, albumID);
            //if album is published, check whether album/music is purchased 
            //if purchased do soft delete
            if (album.getIsPublished()) {
                //no album purchase yet
                if (album.getNumPurchase() == 0) {
                    Boolean trackPurchase = false;
                    //check for track purchase
                    List<Music> listOfMusics = album.getListOfMusics();
                    for (int i = 0; i < listOfMusics.size(); i++) {
                        Music m = listOfMusics.get(i);
                        //no track download
                        if (m.getNumPurchase() == 0) {
                            cibl.deleteFileFromGoogleCloudStorage(m.getFileLocation128());
                            cibl.deleteFileFromGoogleCloudStorage(m.getFileLocation320());
                            em.remove(m);
                        } else {
                            trackPurchase = true;
                        }
                    }
                    //if no track purchase, delete whole album
                    if (!trackPurchase) {
                        em.remove(album);
                    }
                    if (album.getImageLocation() != null) {
                        cibl.deleteFileFromGoogleCloudStorage(album.getImageLocation());
                    }
                }
            } else {
                //if album is not published do hard delete
                if (album.getImageLocation() != null) {
                    cibl.deleteFileFromGoogleCloudStorage(album.getImageLocation());
                }
                for (Music m : album.getListOfMusics()) {
                    cibl.deleteFileFromGoogleCloudStorage(m.getFileLocation128());
                    cibl.deleteFileFromGoogleCloudStorage(m.getFileLocation320());
                }
                em.remove(album);
            }

            em.flush();

            album.setIsDeleted(true);
            for (Music m : album.getListOfMusics()) {
                m.setIsDeleted(true);
            }

            helper.setDescription("Album has been deleted successfully");
            helper.setResult(true);
            return helper;
        } catch (Exception e) {
            System.out.println("MusicManagementBean: deleteAlbum() failed.");
            e.printStackTrace();
            helper.setDescription("Error occurred while trying to delete album, please try again.");
            helper.setResult(false);
            return helper;
        }
    }

}
