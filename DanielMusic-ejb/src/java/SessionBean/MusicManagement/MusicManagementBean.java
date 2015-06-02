package SessionBean.MusicManagement;

import EntityManager.Album;
import EntityManager.Artist;
import EntityManager.Music;
import static EntityManager.Music_.artistName;
import EntityManager.ReturnHelper;
import EntityManager.SearchHelper;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class MusicManagementBean implements MusicManagementBeanLocal {

    @PersistenceContext(unitName = "DanielMusic-ejbPU")
    private EntityManager em;

    @Override
    public ReturnHelper encodeToMP3(File sourceFileName, File targetFileName) {
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            Encoder encoder = new Encoder();
            AudioAttributes aa = new AudioAttributes();
            aa.setCodec("libmp3lame");
            aa.setBitRate(new Integer(320000));
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
    public String generateDownloadLink(Long accountID, Long musicID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Music> searchMusicByGenre(Long genreID) {
        System.out.println("searchMusicByGenre() called with genreID: " + genreID);
        try {
            Query q = em.createQuery("SELECT m FROM Music m, Album a WHERE a.listOfMusics.id=m.id and m.listOfGenres.id=:genreID AND m.isDeleted=false ORDER BY a.publishedDate DESC ");
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
    public List<Music> searchMusicByArtist(String artistName) {
        System.out.println("searchMusicByArtist() called with artistName: " + artistName);
        try {
            Query q = em.createQuery("SELECT m FROM Music m WHERE m.artistName LIKE %:artistName% AND m.isDeleted=false");
            q.setParameter("artistName", artistName);
            List<Music> listOfMusics = q.getResultList();
            System.out.println("searchMusicByArtist() successful");

            return listOfMusics;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while calling searchMusicByArtist()");
            return null;
        }
    }

    @Override
    public List<Music> searchMusic(String searchString) {
        System.out.println("searchMusic() called with searchString: " + searchString);
        try {
            Scanner sc = new Scanner(searchString);
            ArrayList<String> arrayList = new ArrayList<String>();
            List<Music> listOfMusics = null;
            while (sc.hasNext()) {
                arrayList.add(sc.next());
            }
            for (String s : arrayList) {
                Query q = em.createQuery("SELECT m FROM Music m WHERE (m.artistName LIKE %:s%) OR  AND m.isDeleted=false");
                q.setParameter("artistName", artistName);
                listOfMusics = q.getResultList();
            }

            System.out.println("searchMusic() successful");

            return listOfMusics;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while calling searchMusic()");
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

            q = em.createQuery("SELECT a FROM Album a WHERE a.name LIKE '%:searchString%' AND a.isDeleted=false ORDER BY a.publishedDate DESC");
            q.setParameter("searchString", searchString);
            List<Album> listOfAlbums = q.getResultList();

            q = em.createQuery("SELECT a FROM Artist a WHERE a.name LIKE '%:searchString%' AND a.isDisabled=false AND a.isApproved=true");
            q.setParameter("searchString", searchString);
            List<Artist> listOfArtists = q.getResultList();

            q = em.createQuery("SELECT m FROM Music m WHERE m.name LIKE '%:searchString%' AND m.isDeleted=false ORDER BY m.album.publishedDate DESC");
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
}
