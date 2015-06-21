package SessionBean.MusicManagement;

import EntityManager.Account;
import EntityManager.Album;
import EntityManager.Artist;
import EntityManager.Band;
import EntityManager.Genre;
import EntityManager.Music;
import EntityManager.ReturnHelper;
import EntityManager.SearchHelper;
import SessionBean.CommonInfrastructure.CommonInfrastructureBeanLocal;
import com.paypal.svcs.services.AdaptivePaymentsService;
import com.paypal.svcs.types.ap.PayRequest;
import com.paypal.svcs.types.ap.PayResponse;
import com.paypal.svcs.types.ap.Receiver;
import com.paypal.svcs.types.ap.ReceiverList;
import com.paypal.svcs.types.common.RequestEnvelope;
import it.sauronsoftware.jave.AudioAttributes;
import java.io.File;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
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
    private CommonInfrastructureBeanLocal commonInfrastructureBean;

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
    public void testAdaptivePayment() {
        try {
            PayRequest payRequest = new PayRequest();

            List<Receiver> receivers = new ArrayList<Receiver>();
//Artist (partial of the total)
            Receiver secondaryReceiver = new Receiver();
            secondaryReceiver.setAmount(0.50);
            secondaryReceiver.setEmail("daniel-artist@hotmail.com");
            receivers.add(secondaryReceiver);

//Daniel (total amount)
            Receiver primaryReceiver = new Receiver();
            primaryReceiver.setAmount(1.00);
            primaryReceiver.setEmail("danielmusic@hotmail.com");
            primaryReceiver.setPrimary(true);
            receivers.add(primaryReceiver);

            ReceiverList receiverList = new ReceiverList(receivers);
            payRequest.setReceiverList(receiverList);

            RequestEnvelope requestEnvelope = new RequestEnvelope("en_US");
            payRequest.setRequestEnvelope(requestEnvelope);
            payRequest.setActionType("PAY");
            payRequest.setFeesPayer("PRIMARYRECEIVER");
            payRequest.setCancelUrl("https://devtools-paypal.com/guide/ap_chained_payment?cancel=true");//Return if payment cancelled
            payRequest.setReturnUrl("https://devtools-paypal.com/guide/ap_chained_payment?success=true");//Return after payment complete
            payRequest.setCurrencyCode("USD");
            payRequest.setIpnNotificationUrl("http://replaceIpnUrl.com");

            Map<String, String> sdkConfig = new HashMap<String, String>();
            sdkConfig.put("mode", "sandbox");
            sdkConfig.put("acct1.UserName", "jb-us-seller_api1.paypal.com");
            sdkConfig.put("acct1.Password", "WX4WTU3S8MY44S7F");
            sdkConfig.put("acct1.Signature", "AFcWxV21C7fd0v3bYYYRCpSSRl31A7yDhhsPUU2XhtMoZXsWHFxu-RWy");
            sdkConfig.put("acct1.AppId", "APP-80W284485P519543T");

            AdaptivePaymentsService adaptivePaymentsService = new AdaptivePaymentsService(sdkConfig);
            PayResponse payResponse = adaptivePaymentsService.pay(payRequest);

            System.out.println("-----------");
            System.out.println(payResponse.getPaymentExecStatus());
            String payKey = payResponse.getPayKey();
            System.out.println("Open this link in browser: https://www.sandbox.paypal.com/webscr?cmd=_ap-payment&paykey=" + payKey);
            //open link in browser
            //Accounts (all password is 12345678): 
            //daniel-buyer@hotmail.com, daniel-artist@hotmail.com, danielmusic@hotmail.com
            //Login at https://www.sandbox.paypal.com
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public ReturnHelper generateDownloadLink(String email, Long musicID) {
        System.out.println("generateDownloadLink() called with email: " + email + " and musicID: " + musicID);
        try {
            ReturnHelper helper = new ReturnHelper();
            Query q = em.createQuery("select a from Artist a where a.email=:email and a.isDisabled=false and a.emailIsVerified=true");
            q.setParameter("email", email);
            Artist artist = (Artist) q.getSingleResult();
            Music music = em.getReference(Music.class, musicID);

            if (artist.getListOfPurchasedMusics().contains(music)) {
                //generate download link for user
                music.setNumDownloaded(music.getNumDownloaded() + 1);
                String downloadLink = commonInfrastructureBean.getMusicFileURLFromGoogleCloudStorage("music/" + artist.getId() + "/" + music.getAlbum().getId() + "/" + music.getName() + ".mp3");
                helper.setDescription(downloadLink);
                helper.setResult(true);
            } else {
                helper.setDescription("Failed to generate download link. Please check that you have bought this music.");
                helper.setResult(false);
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

    public void persist(Object object) {
        em.persist(object);
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
    public ReturnHelper createMusic(Part musicPart, Long albumID, Integer trackNumber, String name, Double price, List<Long> listOfGenreIDs) {
        ReturnHelper helper = new ReturnHelper();
        try {
            Album album = null;
            String fileName = musicPart.getSubmittedFileName();
            String tempMusicURL = "temp/" + fileName;
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
                helper.setResult(false);
                return helper;
            }

            File newFile128 = new File(tempMusicURL + "128");
            File newFile320 = new File(tempMusicURL + "320");
            encodeToMP3(file, newFile128, 128);
            encodeToMP3(file, newFile320, 320);

            //create music entity
            Music music = new Music();
            if (albumID == null) {
                helper.setDescription("Please check that the album exists for this music.");
                helper.setResult(false);
                return helper;
            } else {
                album = em.getReference(Album.class, albumID);
            }
            music.setAlbum(album);
            music.setArtistName(album.getArtist().getName());
            ArrayList<Genre> listOfGenres = new ArrayList<Genre>();
            for (Long genreID : listOfGenreIDs) {
                listOfGenres.add(em.getReference(Genre.class, genreID));
            }
            music.setListOfGenres(listOfGenres);
            music.setName(name);
            music.setPrice(price);
            music.setTrackNumber(trackNumber);
            em.persist(music);
            em.flush();

            String musicURL128;
            String musicURL320;
            Artist artist = album.getArtist();

            if (artist != null) {
                musicURL128 = "music/" + album.getArtist().getId() + "/" + album.getId() + "/" + music.getId() + "/128/" + fileName;
                musicURL320 = "music/" + album.getArtist().getId() + "/" + album.getId() + "/" + music.getId() + "/320/" + fileName;
            } else {
                musicURL128 = "music/" + album.getBand().getId() + "/" + album.getId() + "/" + music.getId() + "/128/" + fileName;
                musicURL320 = "music/" + album.getBand().getId() + "/" + album.getId() + "/" + music.getId() + "/320/" + fileName;
            }

            music.setFileLocation128(musicURL128);
            music.setFileLocation320(musicURL320);

            //end create music
            ReturnHelper result1 = commonInfrastructureBean.uploadFileToGoogleCloudStorage(musicURL128, tempMusicURL + "128", Boolean.FALSE);
            ReturnHelper result2 = commonInfrastructureBean.uploadFileToGoogleCloudStorage(musicURL320, tempMusicURL + "320", Boolean.FALSE);

            if (result1.getResult() && result2.getResult()) {
                helper.setDescription("Track has been uploaded successfully.");
                helper.setResult(true);
            } else {
                helper.setDescription("Error occurred while uploading track... Please check that the track is in the correct format.");
                helper.setResult(false);
                //delete music entity
                em.refresh(music);
                em.remove(music);
            }

            System.out.println("deleting file... " + file.delete());
            System.out.println("deleting file newFile128... " + newFile128.delete());
            System.out.println("deleting file newFile320... " + newFile320.delete());

            return helper;
        } catch (Exception e) {
            e.printStackTrace();
            helper.setDescription("Error occurred while creating track, please try again.");
            helper.setResult(false);
            return helper;
        }
    }

    @Override
    public ReturnHelper createAlbum(Part imagePart, String name, String description, Long artistOrBandID) {
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

            if (imagePart != null) {
                String fileName = imagePart.getSubmittedFileName();
                tempImageURL = "temp/" + fileName;
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
                imageLocation = "image/" + account.getId() + "/" + name + commonInfrastructureBean.generateUUID();
                result = commonInfrastructureBean.uploadFileToGoogleCloudStorage(imageLocation, tempImageURL, true);

                File file = new File(tempImageURL);
                System.out.println("deleting file... " + file.delete());
            }

            Album album = new Album();
            if ((result != null)) {
                if (result.getResult()) {
                    System.out.println("Image location set... " + imageLocation);
                    album.setImageLocation(imageLocation);

                } else {
                    helper.setDescription("Image failed to upload, please try again.");
                    helper.setResult(false);
                    return helper;
                }
            }

            if (isArtist) {
                album.setArtist(artist);
            } else {
                album.setBand(band);
            }

            album.setDescription(description);
            album.setName(name);
            em.persist(album);
            em.flush();
            System.out.println("Album has been persisted.");
            em.refresh(album);
            System.out.println("Album ID: " + album.getId());
            helper.setID(album.getId());
            helper.setDescription("Album has been created successfully.");
            helper.setResult(true);
            return helper;

        } catch (Exception e) {
            e.printStackTrace();
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
    public ReturnHelper editAlbum(Long albumID, Part imagePart, String name, String description) {
        System.out.println("editAlbum() called.");
        ReturnHelper helper = new ReturnHelper();
        try {
            Album album = em.getReference(Album.class, albumID);

            if (album.getIsPublished()) {
                System.out.println("Album is already published, cannot be edited.");
                helper.setDescription("Album has been published and cannot be edited.");
                helper.setResult(false);
                return helper;
            } else {
                album.setName(name);
                album.setDescription(description);

                if (imagePart != null) {
                    String imageLocation = null;
                    String tempImageURL = null;
                    String fileName = imagePart.getSubmittedFileName();
                    tempImageURL = "temp/" + fileName;
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
                        imageLocation = "image/" + album.getArtist().getId() + "/" + name + commonInfrastructureBean.generateUUID();
                    } else {
                        imageLocation = "image/" + album.getBand().getId() + "/" + name + commonInfrastructureBean.generateUUID();
                    }

                    result = commonInfrastructureBean.uploadFileToGoogleCloudStorage(imageLocation, tempImageURL, true);
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
            Album album = em.getReference(Album.class, albumID);

            if (album.getArtist() != null) {
                if (!album.getArtist().getEmailIsVerified()) {
                    helper.setDescription("Sorry your email is not verified, please verify your email first.");
                    helper.setResult(false);
                    return helper;
                }
            } else {
                if (!album.getBand().getEmailIsVerified()) {
                    helper.setDescription("Sorry your email is not verified, please verify your email first.");
                    helper.setResult(false);
                    return helper;
                }
            }

            album.setIsPublished(true);
            album.setPublishedDate(new Date());
            helper.setDescription("Album has been published successfully.");
            helper.setResult(true);
            return helper;
        } catch (Exception e) {
            e.printStackTrace();
            helper.setDescription("Error occurred while trying to publish album, please try again.");
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
            //if purchased cannot delete
            if (album.getIsPublished()) {
                
            } else {
                //if album is not published do hard delete
                
                //if not purchased do hard delete
                ReturnHelper result = commonInfrastructureBean.deleteFileFromGoogleCloudStorage(album.getImageLocation());
                for (Music m : album.getListOfMusics()) {
                    ReturnHelper result1 = commonInfrastructureBean.deleteFileFromGoogleCloudStorage(m.getFileLocation128());
                    ReturnHelper result2 = commonInfrastructureBean.deleteFileFromGoogleCloudStorage(m.getFileLocation320());
                }
                em.remove(album);
            }

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
