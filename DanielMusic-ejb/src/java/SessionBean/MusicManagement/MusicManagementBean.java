package SessionBean.MusicManagement;

import EntityManager.Account;
import EntityManager.Album;
import EntityManager.Artist;
import EntityManager.ExploreHelper;
import EntityManager.Genre;
import EntityManager.Music;
import EntityManager.ReturnHelper;
import EntityManager.SearchHelper;
import SessionBean.AccountManagement.AccountManagementBeanLocal;
import SessionBean.CommonInfrastructure.CommonInfrastructureBeanLocal;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v23Tag;
import com.mpatric.mp3agic.Mp3File;
import it.sauronsoftware.jave.AudioAttributes;
import java.io.File;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.MultimediaInfo;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.Part;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

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
            aa.setBitRate(bitrate * 1000);
            aa.setChannels(2);
            aa.setSamplingRate(44100);
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
    public String generateDownloadLink(Long musicID, String type, Boolean isIncreaseDownloadCount) {
        System.out.println("generateDownloadLink() called");
        try {
            Query q = em.createQuery("select a from Music a where a.id=:id");
            q.setParameter("id", musicID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            Music music = (Music) q.getSingleResult();
            String downloadLink = null;
            switch (type) {
                case "wav":
                    downloadLink = cibl.getFileURLFromGoogleCloudStorage(music.getFileLocationWAV(), 120L);//2mins expiry
                    break;
                case "320":
                    downloadLink = cibl.getFileURLFromGoogleCloudStorage(music.getFileLocation320(), 120L);//2mins expiry
                    break;
                case "128":
                    downloadLink = cibl.getFileURLFromGoogleCloudStorage(music.getFileLocation128(), 120L);//2mins expiry
                    break;
            }
            //generate download link for user
            if (downloadLink != null) {
                if (isIncreaseDownloadCount) {
                    music.setNumDownloaded(music.getNumDownloaded() + 1);
                }
                return downloadLink;
            }
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

            q = em.createQuery("SELECT a FROM Album a WHERE a.name LIKE :searchString AND a.isDeleted=false AND a.artist.isApproved=1 AND a.isPublished=true ORDER BY a.yearReleased DESC");
            q.setParameter("searchString", "%" + searchString + "%");
            List<Album> listOfAlbums = q.getResultList();

            q = em.createQuery("SELECT a FROM Artist a WHERE a.name LIKE :searchString AND a.isDisabled=false AND a.isApproved=1");
            q.setParameter("searchString", "%" + searchString + "%");
            List<Artist> listOfArtists = q.getResultList();

            q = em.createQuery("SELECT m FROM Music m WHERE m.name LIKE :searchString AND m.isDeleted=false AND m.album.artist.isApproved=1 AND m.album.isPublished=true ORDER BY m.album.yearReleased DESC");
            q.setParameter("searchString", "%" + searchString + "%");
            List<Music> listOfMusics = q.getResultList();

            helper.setListOfAlbums(listOfAlbums);
            helper.setListOfArtists(listOfArtists);
            helper.setListOfMusics(listOfMusics);

            System.out.println("search() successful");
            System.out.println(helper.getListOfArtists().size());
            System.out.println(helper.getListOfAlbums().size());
            System.out.println(helper.getListOfMusics().size());

            return helper;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while calling search()");
            return null;
        }
    }

    public static String removeExtension(String s) {

        String separator = System.getProperty("file.separator");
        String filename;

        // Remove the path upto the filename.
        int lastSeparatorIndex = s.lastIndexOf(separator);
        if (lastSeparatorIndex == -1) {
            filename = s;
        } else {
            filename = s.substring(lastSeparatorIndex + 1);
        }

        // Remove the extension.
        int extensionIndex = filename.lastIndexOf(".");
        if (extensionIndex == -1) {
            return filename;
        }

        return filename.substring(0, extensionIndex);
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
            if (name == null || name.isEmpty()) {
                helper.setDescription("Music name cannot be empty.");
                return helper;
            }

            String fileName = musicPart.getSubmittedFileName();
            //Don't take file extension for the filename
            fileName = removeExtension(fileName);
            String tempMusicURL = "temp/musicUpload_" + cibl.generateUUID() + "_" + fileName + ".wav";
            InputStream fileInputStream = musicPart.getInputStream();
            OutputStream fileOutputStream = new FileOutputStream(tempMusicURL);

            int nextByte;
            while ((nextByte = fileInputStream.read()) != -1) {
                fileOutputStream.write(nextByte);
            }
            fileOutputStream.close();
            fileInputStream.close();

            File file = new File(tempMusicURL);
            if (file.length() / 1024 / 1024 > 100) {
                helper.setDescription("File size is over 100mb and cannot be uploaded");
                file.delete();
                return helper;
            }

            //Check if the file meets bitrate requirements
            Encoder encoder = new Encoder();
            MultimediaInfo multimediaInfo = encoder.getInfo(file);
            System.out.println("BR:" + multimediaInfo.getAudio().getBitRate());
            System.out.println("SR:" + multimediaInfo.getAudio().getSamplingRate());
            System.out.println("F:" + multimediaInfo.getFormat());
            if (!multimediaInfo.getFormat().equals("wav")) {
                helper.setDescription("File uploaded does not appear to be a proper wav format.");
                file.delete();
                return helper;
            } else if (multimediaInfo.getAudio().getSamplingRate() < 44100) {
                helper.setDescription("File uploaded does not meet the minimum sampling rate of at least 44.1kHz ");
                file.delete();
                return helper;
            }
            //check if the music >10mins, if more than 10mins return ReturnHelper
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            double durationInSeconds = (frames + 0.0) / format.getFrameRate();
            if (durationInSeconds > 600) {
                helper.setDescription("The track duration cannot be more than 10mins, please upload a shorter duration.");
                file.delete();
                return helper;
            }
            audioInputStream.close();

            //Create the 2 mp3 from the wav file
            File newFile128 = new File(tempMusicURL + "_128.mp3");
            File newFile320 = new File(tempMusicURL + "_320.mp3");
            encodeToMP3(file, newFile128, 128);
            encodeToMP3(file, newFile320, 320);

            //To the tagging for the mp3 files
//            AudioFile f = AudioFileIO.read(file);
////            AudioFileIO test = new AudioFileIO();
////            test.deleteTag(f);
//            Tag tag = f.getTag();
//            tag.setField(FieldKey.TRACK, trackNumber + "");
//            tag.setField(FieldKey.ARTIST, album.getArtistName());
//            tag.setField(FieldKey.TITLE, name + "");
//            tag.setField(FieldKey.ALBUM, album.getName() + "");
//            tag.setField(FieldKey.YEAR, yearReleased + "");
//            f.commit();
            Mp3File mp3file = new Mp3File(tempMusicURL + "_128.mp3");
            ID3v2 id3v2Tag;
            if (mp3file.hasId3v2Tag()) {
                mp3file.removeId3v2Tag();
            }
            id3v2Tag = new ID3v23Tag();
            mp3file.setId3v2Tag(id3v2Tag);
            id3v2Tag.setTrack(trackNumber + "");
            id3v2Tag.setArtist(album.getArtistName());
            id3v2Tag.setTitle(name);
            id3v2Tag.setAlbum(album.getName());
            id3v2Tag.setYear(yearReleased + "");
            mp3file.save(tempMusicURL + "_128tagged.mp3");
            //repeat for 320
            mp3file = new Mp3File(tempMusicURL + "_320.mp3");
            if (mp3file.hasId3v2Tag()) {
                mp3file.removeId3v2Tag();
            }
            id3v2Tag = new ID3v23Tag();
            mp3file.setId3v2Tag(id3v2Tag);
            id3v2Tag.setTrack(trackNumber + "");
            id3v2Tag.setArtist(album.getArtistName());
            id3v2Tag.setTitle(name);
            id3v2Tag.setAlbum(album.getName());
            id3v2Tag.setYear(yearReleased + "");
            mp3file.save(tempMusicURL + "_320tagged.mp3");

            //create music entity
            Music music = new Music();
            music.setAlbum(album);
            music.setArtistName(album.getArtist().getName());
            ArrayList<Genre> listOfGenres = new ArrayList<Genre>();
            listOfGenres.add(album.getArtist().getGenre());
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

            musicURL128 = "music/" + album.getArtist().getId() + "/" + album.getId() + "/" + music.getId() + "/128/" + fileName + ".mp3";
            musicURL320 = "music/" + album.getArtist().getId() + "/" + album.getId() + "/" + music.getId() + "/320/" + fileName + ".mp3";
            musicURLwav = "music/" + album.getArtist().getId() + "/" + album.getId() + "/" + music.getId() + "/wav/" + fileName + ".wav";

            music.setFileLocation128(musicURL128);
            music.setFileLocation320(musicURL320);
            music.setFileLocationWAV(musicURLwav);

            //upload music to storage
            ReturnHelper result1 = cibl.uploadFileToGoogleCloudStorage(musicURL128, tempMusicURL + "_128tagged.mp3", false, true);
            ReturnHelper result2 = cibl.uploadFileToGoogleCloudStorage(musicURL320, tempMusicURL + "_320tagged.mp3", false, false);
            ReturnHelper result3 = cibl.uploadFileToGoogleCloudStorage(musicURLwav, tempMusicURL, false, false);

            if (result1.getResult() && result2.getResult() && result3.getResult()) {
                helper.setDescription("Track has been uploaded successfully.");
            } else {
                helper.setDescription("Error occurred while uploading track... Please check that the track is in the correct format.");
                //delete music entity
                em.refresh(music);
                em.remove(music);
            }

            //Delete away the used files
            file.delete();
            newFile128.delete();
            newFile320.delete();
            File newFile128tagged = new File(tempMusicURL + "_128tagged.mp3");
            File newFile320tagged = new File(tempMusicURL + "_320tagged.mp3");
            newFile128tagged.delete();
            newFile320tagged.delete();
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

//    @Override
//    public ReturnHelper editMusic(Long musicID, Integer trackNumber, String name, Double price, String lyrics, String credits) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
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
    public ReturnHelper createAlbum(Boolean isSingle, Part imagePart, String name, String description, Long artistOrBandID, Integer yearReleased, String credits, Double price) {
        System.out.println("createAlbum() called");
        ReturnHelper helper = new ReturnHelper();
        try {
            ReturnHelper result = new ReturnHelper();
            result.setResult(false);
            String imageLocation = null;
            String tempImageURL = null;
            Boolean isArtist = null;

            Account account = em.getReference(Account.class, artistOrBandID);
            Artist artist = null;
            artist = (Artist) account;
            if (artist.getGenre() == null) {
                result.setDescription("Update your profile with a genre first before creating albums.");
                return result;
            }
            String text = Double.toString(Math.abs(price));
            int integerPlaces = text.indexOf('.');
            int decimalPlaces = text.length() - integerPlaces - 1;
            if (decimalPlaces > 1) {
                result.setDescription("Price must be rounded to the nearest 10 cents.");
                return result;
            }
            Album album = new Album();

            album.setArtist(artist);
            album.setIsSingle(isSingle);
            album.setDescription(description);
            album.setName(name);
            album.setArtistName(account.getName());
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
    public List<Album> ListAllAlbumByArtistOrBandID(Long artistOrBandAccountID, Boolean showUnpublished, Boolean showUnapproved) {
        System.out.println("ListAllAlbumByArtistOrBandID() called");
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
            System.out.println("ListAllAlbumByArtistOrBandID() failed");
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
            } else if (imagePart.getSize() < 5000000) {
                helper.setDescription("Album art cannot be larger than 5MB");
                helper.setResult(false);
                return helper;
            } else {
                String text = Double.toString(Math.abs(price));
                int integerPlaces = text.indexOf('.');
                int decimalPlaces = text.length() - integerPlaces - 1;
                if (decimalPlaces > 1) {
                    helper.setDescription("Price must be rounded to the nearest 10 cents.");
                    helper.setResult(false);
                    return helper;
                }
                album.setName(name);
                album.setDescription(description);
                album.setYearReleased(yearReleased);
                album.setCredits(credits);
                album.setPrice(price);

                if (imagePart != null) {
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

                    if (result.getResult()) {
                        album.setImageLocation(imageLocation);
                    } else {
                        helper.setDescription("Error while editing album, please try again.");
                        helper.setResult(false);
                        return helper;
                    }
                }
                em.merge(album);
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
    public ReturnHelper editPublishedAlbum(Long albumID, Part imagePart, String description, String credits, Double price) {
        System.out.println("MusicManagementBean: editAlbumPrice() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            if (imagePart.getSize() < 5000000) {
                result.setDescription("Album art cannot be larger than 5MB");
                result.setResult(false);
                return result;
            }
            result = editAlbumPrice(albumID, price);
            if (!result.getResult()) {
                return result;
            }
            Query q = em.createQuery("SELECT E FROM Album E where E.id=:id");
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            q.setParameter("id", albumID);
            Album album = (Album) q.getSingleResult();
            album.setDescription(description);
            album.setCredits(credits);

            if (imagePart != null) {
                String imageLocation = null;
                String tempImageURL = null;
                String fileName = imagePart.getSubmittedFileName();
                tempImageURL = "temp/" + fileName + cibl.generateUUID();
                InputStream fileInputStream = imagePart.getInputStream();
                OutputStream fileOutputStream = new FileOutputStream(tempImageURL);

                int nextByte;
                while ((nextByte = fileInputStream.read()) != -1) {
                    fileOutputStream.write(nextByte);
                }
                fileOutputStream.close();
                fileInputStream.close();

                //check whether album belongs to artist/band
                if (album.getArtist() != null) {
                    imageLocation = "image/album/" + album.getId() + "/albumart/" + album.getName() + ".jpg";
                } else {
                    imageLocation = "image/album/" + album.getId() + "/albumart/" + album.getName() + ".jpg";
                }

                result = cibl.uploadFileToGoogleCloudStorage(imageLocation, tempImageURL, true, true);
                File file = new File(tempImageURL);

                if (result.getResult()) {
                    album.setImageLocation(imageLocation);
                } else {
                    result.setDescription("Error while editing album, please try again.");
                    result.setResult(false);
                    return result;
                }
            }
            em.merge(album);
            result.setDescription("Album details have been updated successfully.");
            result.setResult(true);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            result.setDescription("Internal server error");
            return result;
        }
    }

    @Override
    public ReturnHelper editMusicPrice(Long musicID, Double newPrice) {
        System.out.println("MusicManagementBean: editMusicPrice() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            Music music = em.getReference(Music.class, musicID);
            Boolean isDeleted = music.getIsDeleted();
            if (isDeleted) {
                result.setDescription("Price cannot be updated as the music has been deleted.");
            } else {
                String text = Double.toString(Math.abs(newPrice));
                int integerPlaces = text.indexOf('.');
                int decimalPlaces = text.length() - integerPlaces - 1;
                if (decimalPlaces > 1) {
                    result.setDescription("Price must be rounded to the nearest 10 cents.");
                    return result;
                }
                music.setPrice(newPrice);
                em.merge(music);
                result.setDescription("Price updated");
                result.setResult(true);
            }
        } catch (Exception e) {
            System.out.println("MusicManagementBean: Error occurred while trying to editMusicPrice()");
            e.printStackTrace();
            result.setDescription("Internal server error");
        }
        return result;
    }

    @Override
    public ReturnHelper editAlbumPrice(Long albumID, Double newPrice) {
        System.out.println("MusicManagementBean: editAlbumPrice() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            Album album = em.getReference(Album.class, albumID);
            Boolean isDeleted = album.getIsDeleted();
            if (isDeleted) {
                result.setDescription("Price cannot be updated as the music has been deleted.");
            } else {
                String text = Double.toString(Math.abs(newPrice));
                int integerPlaces = text.indexOf('.');
                int decimalPlaces = text.length() - integerPlaces - 1;
                if (decimalPlaces > 1) {
                    result.setDescription("Price must be rounded to the nearest 10 cents.");
                    return result;
                }
                album.setPrice(newPrice);
                em.merge(album);
                result.setDescription("Price updated");
                result.setResult(true);
            }
        } catch (Exception e) {
            System.out.println("MusicManagementBean: Error occurred while trying to editAlbumPrice()");
            e.printStackTrace();
            result.setDescription("Internal server error");
        }
        return result;
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
            if (!album.getArtist().getEmailIsVerified()) {
                helper.setDescription("Sorry your email is not verified, please verify your email first.");
                helper.setResult(false);
                return helper;
            } else if (album.getArtist().getPaypalEmail() == null || album.getArtist().getPaypalEmail().length() == 0) {
                helper.setDescription("Sorry your PayPal email is not filled in, please edit your profile first.");
                helper.setResult(false);
                return helper;
            }

            if (album.getArtist().getIsApproved() == 0 || album.getArtist().getIsApproved() == -1) {
                album.getArtist().setIsApproved(-2);
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

    @Override
    public List<Artist> listAllArtistBandInGenre(Long genreID) {
        System.out.println("MusicManagement: listAllArtistBandInGenre() called");
        try {
            Query q;
            q = em.createQuery("select a from Artist a where a.isApproved=true and a.genre.id=:genreID");
            q.setParameter("genreID", genreID);
            List<Artist> artists = q.getResultList();
            return artists;
        } catch (Exception e) {
            System.out.println("MusicManagement: Error while calling listAllArtistBandInGenre()");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ExploreHelper> listAllGenreArtist() {
        System.out.println("MusicManagement: listAllGenreArtist() called");
        try {
            Query q;
            q = em.createQuery("select a from Genre a  ORDER BY a.name ASC");

            List<Genre> genres = q.getResultList();
            List<ExploreHelper> exploreHelpers = new ArrayList();
            for (Genre genre : genres) {
                ExploreHelper exploreHelper = new ExploreHelper();
                exploreHelper.setGenre(genre);
                exploreHelper.setArtists(listAllArtistBandInGenre(genre.getId()));
                exploreHelpers.add(exploreHelper);
            }
            return exploreHelpers;
        } catch (Exception e) {
            System.out.println("MusicManagement: Error while calling listAllGenreArtist()");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Boolean checkIfMusicBelongsToArtist(Long artistID, Long musicID) {
        System.out.println("checkIfMusicBelongsToArtist() called");
        try {
            Query q = em.createQuery("SELECT e FROM Music e WHERE e.id=:musicID and e.album.artist.id=:artistID");
            q.setParameter("artistID", artistID);
            q.setParameter("musicID", musicID);
            Music music = (Music) q.getSingleResult();
            return true;
        } catch (NoResultException ex) {
            return false;
        } catch (Exception ex) {
            System.out.println("checkIfMusicBelongsToArtist() failed");
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean checkIfAlbumBelongsToArtist(Long artistID, Long albumID) {
        System.out.println("checkIfAlbumBelongsToArtist() called");
        try {
            Query q = em.createQuery("SELECT e FROM Album e WHERE e.id=:albumID AND e.artist.id=:artistID");
            q.setParameter("artistID", artistID);
            q.setParameter("albumID", albumID);
            Album album = (Album) q.getSingleResult();
            return true;
        } catch (NoResultException ex) {
            return false;
        } catch (Exception ex) {
            System.out.println("checkIfAlbumBelongsToArtist() failed");
            ex.printStackTrace();
        }
        return false;
    }

}
