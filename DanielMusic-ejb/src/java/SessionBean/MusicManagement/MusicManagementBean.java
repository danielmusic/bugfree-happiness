package SessionBean.MusicManagement;

import EntityManager.Account;
import EntityManager.Album;
import EntityManager.Artist;
import EntityManager.ExploreHelper;
import EntityManager.Genre;
import EntityManager.Music;
import EntityManager.ReturnHelper;
import EntityManager.SearchHelper;
import EntityManager.StartupBean;
import SessionBean.AccountManagement.AccountManagementBeanLocal;
import SessionBean.CommonInfrastructure.CommonInfrastructureBeanLocal;
import SessionBean.CommonInfrastructure.SendGridLocal;
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
import java.util.Random;
import java.util.logging.Logger;
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
import org.apache.commons.lang3.StringEscapeUtils;

@Stateless
public class MusicManagementBean implements MusicManagementBeanLocal {

    private static final Logger log = Logger.getLogger(StartupBean.class.getName());

    private static final String approvalRequestEmailSubject = "sounds.sg - New Artist Approval Request";
    private static final String approvalRequestEmailMsg = "Hi there!<br/><br/>There's a new artist pending your approval. Please login to <a href='http://sounds.sg/admin/login.jsp'>admin console</a> to view the full details. <br/><br/><b>Request Details</b><br/>";

    @EJB
    private CommonInfrastructureBeanLocal cibl;
    @EJB
    private AccountManagementBeanLocal ambl;
    @EJB
    private SendGridLocal sgl;

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
            log.info("encodeWavToMP3(): Error, " + ex.getMessage());
            result.setDescription("Error in converting the provided wav file to mp3. Please try again.");
        } catch (Exception ex) {
            log.info("encodeWavToMP3(): Unknown error.");
            result.setDescription("Error in converting the provided wav file to mp3. Please try again.");
            log.info(ex.getMessage());
        }
        return result;
    }

    @Override
    public String generateDownloadLink(Long musicID, String type, Boolean isIncreaseDownloadCount, Long expiryInSeconds) {
        log.info("generateDownloadLink() called");
        try {
            Query q = em.createQuery("select a from Music a where a.id=:id");
            q.setParameter("id", musicID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            Music music = (Music) q.getSingleResult();
            String downloadLink = null;
            switch (type) {
                case "wav":
                    downloadLink = cibl.getFileURLFromGoogleCloudStorage(music.getFileLocationWAV(), expiryInSeconds, music.getName() + ".wav");
                    break;
                case "320":
                    downloadLink = cibl.getFileURLFromGoogleCloudStorage(music.getFileLocation320(), expiryInSeconds, music.getName() + ".mp3");
                    break;
                case "128":
                    downloadLink = cibl.getFileURLFromGoogleCloudStorage(music.getFileLocation128(), expiryInSeconds, music.getName() + ".mp3");
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
            log.info("Error. Failed to generateDownloadLink()");
            log.info(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Music> searchMusicByGenre(Long genreID) {
        log.info("searchMusicByGenre() called with genreID: " + genreID);
        try {
            Query q = em.createQuery("SELECT m FROM Music m, Album a WHERE a.listOfMusics.id=m.id and m.listOfGenres.id=:genreID AND m.isDeleted=false AND a.isDeleted=FALSE AND a.isPublished=true ORDER BY a.publishedDate DESC ");
            q.setParameter("genreID", genreID);
            List<Music> listOfMusics = q.getResultList();
            log.info("searchMusicByGenre() successful");

            return listOfMusics;
        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("Error while calling searchMusicByGenre()");
            return null;
        }
    }

    @Override
    public SearchHelper search(String searchString) {
        log.info("search() called with searchString: " + searchString);
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

            return helper;
        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("Error while calling search()");
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
    public ReturnHelper createMusic(Part musicPart, Long albumID, Integer trackNumber, String name, Double price, String lyrics, String credits, Integer yearReleased) {
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

            String fileName = cibl.getSubmittedFileName(musicPart);
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
                helper.setDescription("File size is over 100MB and can not be accepted by our system");
                file.delete();
                return helper;
            }

            //Check if the file meets bitrate requirements
            Encoder encoder = new Encoder();
            MultimediaInfo multimediaInfo = encoder.getInfo(file);
            log.info("BR:" + multimediaInfo.getAudio().getBitRate());
            log.info("SR:" + multimediaInfo.getAudio().getSamplingRate());
            log.info("F:" + multimediaInfo.getFormat());
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
            music.setArtistName(StringEscapeUtils.escapeHtml4(album.getArtist().getName()));
            List<Genre> listOfGenres = new ArrayList<Genre>();
            Genre genre = album.getListOfGenres().get(0);
            listOfGenres.add(genre);
            music.setLyrics(StringEscapeUtils.escapeHtml4(lyrics));
            music.setCredits(StringEscapeUtils.escapeHtml4(credits));
            music.setListOfGenres(listOfGenres);
            music.setName(StringEscapeUtils.escapeHtml4(name));
            music.setPrice(price);
            music.setTrackNumber(trackNumber);
            music.setYearReleased(yearReleased);
            em.persist(music);
            List<Music> genresMusic = genre.getListOfMusics();
            genresMusic.add(music);
            em.merge(genre);
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
            ReturnHelper result1 = cibl.uploadFileToGoogleCloudStorage(musicURL128, tempMusicURL + "_128tagged.mp3", fileName + ".mp3", false, true);
            ReturnHelper result2 = cibl.uploadFileToGoogleCloudStorage(musicURL320, tempMusicURL + "_320tagged.mp3", fileName + ".mp3", false, false);
            ReturnHelper result3 = cibl.uploadFileToGoogleCloudStorage(musicURLwav, tempMusicURL, fileName + ".wav", false, false);

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
            log.info(e.getMessage());
            helper.setDescription("Error occurred while creating track, please try again.");
            helper.setResult(false);
            return helper;
        }
    }

    @Override
    public Music getMusic(Long musicID) {
        log.info("MusicManagementBean: getMusic() called");
        Music music;
        try {
            music = em.getReference(Music.class, musicID);
            Boolean isDeleted = music.getIsDeleted();
            if (isDeleted) {
                log.info("MusicManagementBean: Failed to getMusic(), music has been deleted");
                return null;
            } else {
                log.info("MusicManagementBean: Successfully called getMusic(), music retrieved");
                return music;
            }
        } catch (Exception e) {
            log.info("MusicManagementBean: Error occurred while trying to call getMusic()");
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public ReturnHelper deleteMusic(Long musicID) {
        log.info("MusicManagementBean: deleteMusic() called");
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
            em.remove(music);
            log.info("MusicManagementBean: deleteMusic() successfully");
            helper.setDescription("The track has been deleted successfully.");
            helper.setResult(true);
            return helper;
        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("MusicManagementBean: Error occurred while calling deleteMusic()");
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
    public List<Music> listAllTracksByAlbumID(Long albumID) {
        log.info("MusicManagementBean: ListAllTracksByAlbumID() called");
        try {
            Query q = em.createQuery("select a from Music a where a.isDeleted=false AND a.album.id=:albumID");
            q.setParameter("albumID", albumID);
            List<Music> albums = q.getResultList();
            return albums;
        } catch (Exception ex) {
            log.info("ListAllTracksByAlbumID() failed");
            log.info(ex.getMessage());
            return null;
        }
    }

    @Override
    public ReturnHelper createAlbum(Boolean isSingle, Part imagePart, String name, Long genreID, String description, Long artistOrBandID, Integer yearReleased, String credits, Double price) {
        log.info("createAlbum() called");
        ReturnHelper helper = new ReturnHelper();
        try {
            ReturnHelper result = new ReturnHelper();
            result.setResult(false);
            String imageLocation = null;
            String tempImageURL = null;

            Account account = em.getReference(Account.class, artistOrBandID);
            Artist artist = null;
            artist = (Artist) account;
//            String text = Double.toString(Math.abs(price));
//            int integerPlaces = text.indexOf('.');
//            int decimalPlaces = text.length() - integerPlaces - 1;
//            if (decimalPlaces > 1) {
//                result.setDescription("Price must be rounded to the nearest 10 cents.");
//                return result;
//            }
            Album album = new Album();
            //Update album genre
            Genre genre = em.getReference(Genre.class, genreID);
            List<Genre> genres = new ArrayList();
            genres.add(genre);
            album.setListOfGenres(genres);
            album.setGenreName(StringEscapeUtils.escapeHtml4(genre.getName()));
            album.setArtist(artist);
            album.setIsSingle(isSingle);
            album.setDescription(StringEscapeUtils.escapeHtml4(description));
            album.setName(StringEscapeUtils.escapeHtml4(name));
            album.setGenreName(StringEscapeUtils.escapeHtml4(album.getGenreName()));
            album.setArtistName(StringEscapeUtils.escapeHtml4(account.getName()));
            album.setYearReleased(yearReleased);
            album.setCredits(StringEscapeUtils.escapeHtml4(credits));
            album.setPrice(price);
            em.persist(album);
            List<Album> genresAlbums = genre.getListOfAlbums();
            genresAlbums.add(album);
            em.merge(genre);
            em.flush();
            log.info("MusicManagementBean: em.flush(). Album has been persisted.");
            em.refresh(album);
            log.info("MusicManagementBean: em.refresh(). Album ID: " + album.getId());

            //check whether user uploads an image
            if (imagePart != null) {
                if (imagePart.getSize() > 5000000) {
                    em.remove(album);
                    helper.setDescription("Album art cannot be larger than 5MB");
                    helper.setResult(false);
                    return helper;
                }
                String fileName = cibl.getSubmittedFileName(imagePart);
                tempImageURL = "temp/albumart_" + cibl.generateUUID() + "_" + fileName;
                InputStream fileInputStream = imagePart.getInputStream();
                OutputStream fileOutputStream = new FileOutputStream(tempImageURL);

                int nextByte;
                while ((nextByte = fileInputStream.read()) != -1) {
                    fileOutputStream.write(nextByte);
                }
                fileOutputStream.close();
                fileInputStream.close();
                ReturnHelper checkImageResult = cibl.checkIfImageFitsRequirement(tempImageURL);
                if (!checkImageResult.getResult()) {
                    result.setDescription("Album art does not meet image requirements. " + checkImageResult.getDescription());
                    return result;
                }
                imageLocation = "images/album/" + album.getId() + "/albumart/" + name + ".jpg";
                result = cibl.uploadFileToGoogleCloudStorage(imageLocation, tempImageURL, null, true, true);

                File file = new File(tempImageURL);
                file.delete();

                if ((result != null)) {
                    if (result.getResult()) {
                        log.info("Image location set... " + imageLocation);
                        album.setImageLocation(imageLocation);
                        em.merge(album);
                    } else {
                        em.remove(album);
                        helper.setDescription("We are unable to connect to our cloud storage provider, please try again later");
                        helper.setResult(false);
                        return helper;
                    }
                }
            }

            log.info("Album created successfully.");
            helper.setID(album.getId());
            helper.setDescription("Album has been created successfully.");
            helper.setResult(true);
            return helper;

        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("MusicManagementBean: Error occurred while calling createAlbum()");
            helper.setDescription("Error occurred while trying to create album, please try again.");
            helper.setResult(false);
            return helper;
        }
    }

    @Override
    public Album getAlbum(Long albumID) {
        log.info("getAlbum() called.");
        try {
            Query q = em.createQuery("SELECT E FROM Album E WHERE E.id=:albumID");
            q.setParameter("albumID", albumID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            Album album = (Album) q.getSingleResult();
            log.info("getAlbum() called successfully");
            return album;
        } catch (Exception e) {
            log.info("Error while calling getAlbum()");
            log.info(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Album> listAllAlbumByArtistOrBandID(Long artistOrBandAccountID, Boolean showUnpublished, Boolean showUnapproved) {
        log.info("ListAllAlbumByArtistOrBandID() called");
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
            log.info("ListAllAlbumByArtistOrBandID() failed");
            log.info(ex.getMessage());
            return null;
        }
    }

    @Override
    public ReturnHelper editAlbum(Long albumID, Part imagePart, String name, Long genreID, String description, Integer yearReleased, String credits, Double price) {
        log.info("editAlbum() called.");
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

            if (album.getIsPublished()) {
                log.info("Album is already published, cannot be edited.");
                helper.setDescription("Album has been published and cannot be edited.");
                helper.setResult(false);
                return helper;
            } else if (imagePart != null && imagePart.getSize() > 5000000) {
                helper.setDescription("Album art cannot be larger than 5MB");
                helper.setResult(false);
                return helper;
            } else {
//                String text = Double.toString(Math.abs(price));
//                int integerPlaces = text.indexOf('.');
//                int decimalPlaces = text.length() - integerPlaces - 1;
//                if (decimalPlaces > 1) {
//                    helper.setDescription("Price must be rounded to the nearest 10 cents.");
//                    helper.setResult(false);
//                    return helper;
//                }
                album.setName(name);
                //Update album genre
                Genre newGenre = em.getReference(Genre.class, genreID);
                List<Genre> newGenreList = new ArrayList();
                newGenreList.add(newGenre);
                List<Genre> albumGenres = album.getListOfGenres();
                for (Genre genre : albumGenres) {
                    genre.getListOfAlbums().remove(album);
                }
                album.setListOfGenres(null);
                album.setListOfGenres(newGenreList);
                album.setGenreName(newGenreList.get(0).getName());
                newGenre.getListOfAlbums().add(album);

                List<Music> albumMusics = album.getListOfMusics();
//                for (Music music : albumMusics) {
//                    List<Genre> musicGenres = music.getListOfGenres();
//                    for (Genre genre : musicGenres) {
//                        genre.getListOfMusics().remove(music);
//                        musicGenres.remove(genre);
//                    }
//                    music.setListOfGenres(newGenreList);
//                }

                //handle genre side removal and add of music
                List<Genre> musicGenres = new ArrayList();
                for (Music music : albumMusics) {
                    musicGenres.add(music.getListOfGenres().get(0));
                    for (int i = 0; i < music.getListOfGenres().size(); i++) {
                        music.getListOfGenres().remove(i);
                    }
                    music.setListOfGenres(newGenreList);
                }
                for (Genre genre : musicGenres) {
                    for (Music music : albumMusics) {
                        genre.getListOfMusics().remove(music);
                    }
                }
                for (Music music : albumMusics) {
                    newGenre.getListOfMusics().add(music);
                }

                //todo halppp
//                for (Genre albumGenre : album.getListOfGenres()) {
//                    List<Album> genreAlbums = albumGenre.getListOfAlbums();
//                    genreAlbums.remove(album);
//                    albumGenre.setListOfAlbums(genreAlbums);
//                    em.merge(albumGenre);
//                }
//                Genre genre = em.getReference(Genre.class, genreID);
//                List<Genre> genres = new ArrayList();
//                genres.add(genre);
//                album.setListOfGenres(genres);
//                album.setGenreName(genre.getName());
//                //Update the list of music genres
//                log.info("WUBWUBWUB");
//                for (Music music : album.getListOfMusics()) {
//                    log.info(music.getName());
//                    log.info(music.getListOfGenres().get(0).getName());
//                    for (Genre musicGenre : music.getListOfGenres()) {
//                        List<Music> genreMusics = musicGenre.getListOfMusics();
//                        genreMusics.remove(music);
//                        musicGenre.setListOfMusics(genreMusics);
//                        em.merge(musicGenre);
//                    }
//                    music.setListOfGenres(genres);
//                    em.merge(music);
//                    log.info(music.getListOfGenres().get(0).getName());
//                }
                //todo halppp end
                album.setDescription(StringEscapeUtils.escapeHtml4(description));
                album.setYearReleased(yearReleased);
                album.setCredits(StringEscapeUtils.escapeHtml4(credits));
                album.setPrice(price);

                if (imagePart != null) {
                    String imageLocation = null;
                    String tempImageURL = null;
                    String fileName = cibl.getSubmittedFileName(imagePart);
                    tempImageURL = "temp/" + cibl.generateUUID() + "_" + fileName;
                    InputStream fileInputStream = imagePart.getInputStream();
                    OutputStream fileOutputStream = new FileOutputStream(tempImageURL);

                    int nextByte;
                    while ((nextByte = fileInputStream.read()) != -1) {
                        fileOutputStream.write(nextByte);
                    }
                    fileOutputStream.close();
                    fileInputStream.close();

                    ReturnHelper result = new ReturnHelper();
                    result.setResult(false);

                    ReturnHelper checkImageResult = cibl.checkIfImageFitsRequirement(tempImageURL);
                    if (!checkImageResult.getResult()) {
                        result.setDescription("Album art does not meet image requirements. " + checkImageResult.getDescription());
                        return result;
                    }

                    imageLocation = "image/album/" + album.getId() + "/albumart/" + name + ".jpg";
                    result = cibl.uploadFileToGoogleCloudStorage(imageLocation, tempImageURL, null, true, true);

                    File file = new File(tempImageURL);
                    file.delete();
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
            log.info("Error while calling editAlbum()");
            log.info(e.getMessage());
            helper.setDescription("Error while editing album, please try again.");
            helper.setResult(false);
            return helper;
        }
    }

    @Override
    public ReturnHelper editMusicPrice(Long musicID, Double newPrice) {
        log.info("MusicManagementBean: editMusicPrice() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            Music music = em.getReference(Music.class, musicID);
            Boolean isDeleted = music.getIsDeleted();
            if (isDeleted) {
                result.setDescription("Price cannot be updated as the music has been deleted.");
            } else {
//                String text = Double.toString(Math.abs(newPrice));
//                int integerPlaces = text.indexOf('.');
//                int decimalPlaces = text.length() - integerPlaces - 1;
//                if (decimalPlaces > 1) {
//                    result.setDescription("Price must be rounded to the nearest 10 cents.");
//                    return result;
//                }
                music.setPrice(newPrice);
                em.merge(music);
                result.setDescription("Price updated");
                result.setResult(true);
            }
        } catch (Exception e) {
            log.info("MusicManagementBean: Error occurred while trying to editMusicPrice()");
            log.info(e.getMessage());
            result.setDescription("Internal server error");
        }
        return result;
    }

    @Override
    public ReturnHelper editAlbumPrice(Long albumID, Double newPrice) {
        log.info("MusicManagementBean: editAlbumPrice() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            Album album = em.getReference(Album.class, albumID);
            Boolean isDeleted = album.getIsDeleted();
            if (isDeleted) {
                result.setDescription("Price cannot be updated as the music has been deleted.");
            } else {
//                String text = Double.toString(Math.abs(newPrice));
//                int integerPlaces = text.indexOf('.');
//                int decimalPlaces = text.length() - integerPlaces - 1;
//                if (decimalPlaces > 1) {
//                    result.setDescription("Price must be rounded to the nearest 10 cents.");
//                    return result;
//                }
                album.setPrice(newPrice);
                em.merge(album);
                if (album.getIsSingle()) {
                    Long musicID = album.getListOfMusics().get(0).getId();
                    editMusicPrice(musicID, newPrice);
                }
                result.setDescription("Price updated");
                result.setResult(true);
            }
        } catch (Exception e) {
            log.info("MusicManagementBean: Error occurred while trying to editAlbumPrice()");
            log.info(e.getMessage());
            result.setDescription("Internal server error");
        }
        return result;
    }

    @Override
    public ReturnHelper featureMusic(Long musicID) {
        log.info("MusicManagementBean: featureMusic() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            Music music = em.getReference(Music.class, musicID);
            Boolean isDeleted = music.getIsDeleted();
            if (isDeleted) {
                result.setDescription("Music cannot be featured as it has been deleted.");
                return result;
            } else {
                //Check if artist has any other featured music
                Query q = em.createQuery("SELECT E FROM Music E where E.isFeatured=true and e.album.artist.id=:artistID");
                q.setParameter("artistID", music.getAlbum().getArtist().getId());
                q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                List<Music> musics = q.getResultList();
                if (musics.size() > 0) {
                    result.setDescription("You already have other featured music. Only one music can be set as featured.");
                    return result;
                } else if (!music.getAlbum().getIsPublished()) {
                    result.setDescription("You can only feature a music from a published album/single");
                    return result;
                }
                music.setIsFeatured(true);
                em.merge(music);
                Artist artist = music.getAlbum().getArtist();
                //Update explore helper in case featured music gets changed. But only if account is approved and not disabled
                if (artist.getIsApproved() == 1 && !artist.getIsDisabled()) {
                    ExploreHelper exploreHelper;
                    try {
                        q = em.createQuery("SELECT e FROM ExploreHelper e WHERE e.artist.id=:id");
                        q.setParameter("id", artist.getId());
                        exploreHelper = (ExploreHelper) q.getSingleResult();
                        em.remove(exploreHelper);
                    } catch (NoResultException e) {
                        //Safe to ignore as the artist may not be in ExploreHelper in the first place
                    }
                    exploreHelper = new ExploreHelper();
                    exploreHelper.setGenre(artist.getGenre());
                    exploreHelper.setArtist(artist);
                    exploreHelper.setFeaturedMusic(music);
                    em.persist(exploreHelper);
                }
                result.setDescription("Music featured.");
                result.setResult(true);
            }
        } catch (Exception e) {
            log.info("MusicManagementBean: Error occurred while trying to featureMusic()");
            log.info(e.getMessage());
            result.setDescription("Internal server error");
        }
        return result;
    }

    @Override
    public ReturnHelper unfeatureMusic(Long musicID) {
        log.info("MusicManagementBean: unfeatureMusic() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            Music music = em.getReference(Music.class, musicID);
            Boolean isDeleted = music.getIsDeleted();
            if (isDeleted) {
                result.setDescription("Music cannot be featured as it has been deleted.");
            } else {
                music.setIsFeatured(false);
                em.merge(music);
                Artist artist = music.getAlbum().getArtist();
                //Update explore helper in case featured music gets changed. But only if account is approved and not disabled
                if (artist.getIsApproved() == 1 && !artist.getIsDisabled()) {
                    ExploreHelper exploreHelper;
                    try {
                        Query q = em.createQuery("SELECT e FROM ExploreHelper e WHERE e.artist.id=:id");
                        q.setParameter("id", artist.getId());
                        exploreHelper = (ExploreHelper) q.getSingleResult();
                        em.remove(exploreHelper);
                    } catch (NoResultException e) {
                        //Safe to ignore as the artist may not be in ExploreHelper in the first place
                    }
                    exploreHelper = new ExploreHelper();
                    exploreHelper.setGenre(artist.getGenre());
                    exploreHelper.setArtist(artist);
                    exploreHelper.setFeaturedMusic(null);
                    em.persist(exploreHelper);
                }
                result.setDescription("Music no longer featured.");
                result.setResult(true);
            }
        } catch (Exception e) {
            log.info("MusicManagementBean: Error occurred while trying to unfeatureMusic()");
            log.info(e.getMessage());
            result.setDescription("Internal server error");
        }
        return result;
    }

    @Override
    public Music getFeaturedMusic(Long artistID) {
        log.info("MusicManagementBean: getFeaturedMusic() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            Query q = em.createQuery("SELECT E FROM Music E where E.isFeatured=true AND E.album.artist.id=:artistID AND E.album.isPublished=true");
            q.setParameter("artistID", artistID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            List<Music> musics = q.getResultList();
            if (musics.size() > 0) {
                return (Music) q.getResultList().get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.info("MusicManagementBean: Error occurred while trying to getFeaturedMusic()");
            log.info(e.getMessage());
            result.setDescription("Internal server error");
            return null;
        }
    }

    @Override
    public ReturnHelper publishAlbum(Long albumID) {
        log.info("publishAlbum() called.");
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

            if (album.getListOfMusics() == null || album.getListOfMusics().isEmpty()) {
                helper.setDescription("The album cannot be published, no tracks found.");
                helper.setResult(false);
                return helper;
            }

            //Reupdate the approval status to pending regardless if they were new or not approved previously
            //Send the email also
            if (album.getArtist().getIsApproved() == 0 || album.getArtist().getIsApproved() == -1) {
                album.getArtist().setIsApproved(-2);
                sgl.sendEmail("admin@sounds.sg", "no-reply@sounds.sg", approvalRequestEmailSubject, approvalRequestEmailMsg + "Name: " + album.getArtistName() + "<br/>Account Email: " + album.getArtist().getEmail() + "<br/>Contact Email: " + album.getArtist().getContactEmail() + "<br/>PayPal Email: " + album.getArtist().getPaypalEmail() + "<br/>Facebook: " + album.getArtist().getFacebookURL());
            }
            album.setIsPublished(true);
            helper.setDescription("Album has been published successfully.");
            helper.setResult(true);
            return helper;

        } catch (Exception e) {
            log.info(e.getMessage());
            helper.setDescription("Error occurred while trying to publish album, please try again later.");
            helper.setResult(false);
            return helper;
        }
    }

    @Override
    public ReturnHelper deleteAlbum(Long albumID) {
        log.info("MusicManagementBean: deleteAlbum() called.");
        ReturnHelper helper = new ReturnHelper();
        try {
            Album album = em.getReference(Album.class, albumID);
            //if album is published, check whether album/music is purchased 
            //if purchased do soft delete
            if (album.getIsPublished()) {
                Boolean trackPurchase = false;
                //check for track purchase
                List<Music> listOfMusics = album.getListOfMusics();
                for (int i = 0; i < listOfMusics.size(); i++) {
                    Music m = listOfMusics.get(i);
                    if (m.getNumPurchase() > 0) {
                        trackPurchase = true;
                        break;
                    }
                }
                //if no track purchase & album no purchase, hard delete whole album
                if (!trackPurchase && album.getNumPurchase() == 0) {
                    for (Music m : album.getListOfMusics()) {
                        cibl.deleteFileFromGoogleCloudStorage(m.getFileLocationWAV());
                        cibl.deleteFileFromGoogleCloudStorage(m.getFileLocation128());
                        cibl.deleteFileFromGoogleCloudStorage(m.getFileLocation320());
                        em.remove(m);
                    }
                    if (album.getImageLocation() != null) {
                        cibl.deleteFileFromGoogleCloudStorage(album.getImageLocation());
                    }
                    em.remove(album);
                } else { //otherwise soft delete
                    album.setIsDeleted(true);
                    for (Music m : album.getListOfMusics()) {
                        m.setIsDeleted(true);
                    }
                }
            } else {
                //if album is not published do hard delete
                if (album.getImageLocation() != null) {
                    cibl.deleteFileFromGoogleCloudStorage(album.getImageLocation());
                }
                for (Music m : album.getListOfMusics()) {
                    cibl.deleteFileFromGoogleCloudStorage(m.getFileLocationWAV());
                    cibl.deleteFileFromGoogleCloudStorage(m.getFileLocation128());
                    cibl.deleteFileFromGoogleCloudStorage(m.getFileLocation320());
                }
                if (album.getImageLocation() != null) {
                    cibl.deleteFileFromGoogleCloudStorage(album.getImageLocation());
                }
                em.remove(album);
            }

            em.flush();

            helper.setDescription("Album has been deleted successfully");
            helper.setResult(true);
            return helper;
        } catch (Exception e) {
            log.info("MusicManagementBean: deleteAlbum() failed.");
            log.info(e.getMessage());
            helper.setDescription("Error occurred while trying to delete album, please try again.");
            helper.setResult(false);
            return helper;
        }
    }

    @Override
    public List<Artist> listAllArtistBandInGenre(Long genreID) {
        log.info("MusicManagement: listAllArtistBandInGenre() called");
        try {
            Query q;
            q = em.createQuery("select a from Artist a where a.isApproved=true and a.isDisabled=false and a.genre.id=:genreID");
            q.setParameter("genreID", genreID);
            List<Artist> artists = q.getResultList();
            return artists;
        } catch (Exception e) {
            log.info("MusicManagement: Error while calling listAllArtistBandInGenre()");
            log.info(e.getMessage());
        }
        return null;
    }

    @Override
    public List<ExploreHelper> listAllGenreArtist() {
        log.info("MusicManagement: listAllGenreArtist() called");
        try {
            Query q;
            q = em.createQuery("select a from ExploreHelper a ORDER BY a.genre.name ASC");
            List<ExploreHelper> exploreHelpers = q.getResultList();
            return exploreHelpers;
        } catch (Exception e) {
            log.info("MusicManagement: Error while calling listAllGenreArtist()");
            log.info(e.getMessage());
        }

        return null;
    }

    @Override
    public List<ExploreHelper> listAllActiveGenres() {
        log.info("MusicManagement: listAllActiveGenres() called");
        try {
            Query q;
            q = em.createQuery("select DISTINCT(a.genre) from ExploreHelper a ORDER BY a.genre.name ASC");
            List<ExploreHelper> exploreHelpers = q.getResultList();
            return exploreHelpers;
        } catch (Exception e) {
            log.info("MusicManagement: Error while calling listAllActiveGenres()");
            log.info(e.getMessage());
        }

        return null;
    }

    @Override
    public Long getArtistID(String artistName) {
        log.info("getArtist() called.");
        try {
            Query q = em.createQuery("SELECT E FROM Artist E WHERE E.name=:artistName");
            q.setParameter("artistName", artistName);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            Artist artist = (Artist) q.getSingleResult();
            return artist.getId();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @Override
    public Boolean checkIfMusicBelongsToArtist(Long artistID, Long musicID) {
        log.info("checkIfMusicBelongsToArtist() called");
        try {
            Query q = em.createQuery("SELECT e FROM Music e WHERE e.id=:musicID and e.album.artist.id=:artistID");
            q.setParameter("artistID", artistID);
            q.setParameter("musicID", musicID);
            Music music = (Music) q.getSingleResult();
            return true;
        } catch (NoResultException ex) {
            return false;
        } catch (Exception ex) {
            log.info("checkIfMusicBelongsToArtist() failed");
            log.info(ex.getMessage());
        }
        return false;
    }

    @Override
    public Boolean checkIfAlbumBelongsToArtist(Long artistID, Long albumID) {
        log.info("checkIfAlbumBelongsToArtist() called");
        try {
            Query q = em.createQuery("SELECT e FROM Album e WHERE e.id=:albumID AND e.artist.id=:artistID");
            q.setParameter("artistID", artistID);
            q.setParameter("albumID", albumID);
            Album album = (Album) q.getSingleResult();
            return true;
        } catch (NoResultException ex) {
            return false;
        } catch (Exception ex) {
            log.info("checkIfAlbumBelongsToArtist() failed");
            log.info(ex.getMessage());
        }
        return false;
    }

    @Override
    public Music getNextMusicByArtist(Long artistID) {
        log.info("getNextMusicByArtist() called");
        try {
            Query q = em.createQuery("SELECT e FROM Music e WHERE e.album.artist.id=:artistID and e.album.isPublished=true and e.isDeleted=false");
            q.setParameter("artistID", artistID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            List<Music> musics = q.getResultList();
            Random random = new Random();
            int musicNum = random.nextInt(musics.size());
            return musics.get(musicNum);
        } catch (Exception ex) {
            log.info("getNextMusicByArtist() failed");
            log.info(ex.getMessage());
            return null;
        }
    }

    @Override
    public Music getNextMusicByGenre(Long genreID) {
        log.info("getNextMusicByGenre() called");
        try {
            Query q = em.createQuery("SELECT e FROM Music e WHERE e.album.artist.genre.id=:genreID and e.album.isPublished=true and e.isDeleted=false");
            q.setParameter("genreID", genreID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            List<Music> musics = q.getResultList();
            Random random = new Random();
            int musicNum = random.nextInt(musics.size());
            return musics.get(musicNum);
        } catch (Exception ex) {
            log.info("getNextMusicByGenre() failed");
            log.info(ex.getMessage());
            return null;
        }
    }

    @Override
    public Music getNextMusic(String musicURL, String currentPage) {
        log.info("getNextMusic() called");
        try {
            Query q = em.createQuery("SELECT e FROM Music e WHERE e.fileLocation128=:musicURL and e.isDeleted=false");
            musicURL = musicURL.substring(40);
            q.setParameter("musicURL", musicURL);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            Music music = (Music) q.getSingleResult();
            switch (currentPage) {
                case "#!/explore":
                    return getNextMusicByGenre(music.getListOfGenres().get(0).getId());
                default:
                    return getNextMusicByArtist(music.getAlbum().getArtist().getId());
            }
        } catch (Exception ex) {
            log.info("getNextMusic() failed");
            log.info(ex.getMessage());
            return null;
        }
    }
}
