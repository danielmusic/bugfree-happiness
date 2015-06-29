package SessionBean.ClientManagement;

import EntityManager.Account;
import EntityManager.Album;
import EntityManager.Artist;
import EntityManager.Band;
import EntityManager.Music;
import EntityManager.Payment;
import EntityManager.PaymentHelper;
import EntityManager.ReturnHelper;
import EntityManager.ShoppingCart;
import SessionBean.CommonInfrastructure.CommonInfrastructureBeanLocal;
import com.paypal.svcs.services.AdaptivePaymentsService;
import com.paypal.svcs.types.ap.PayRequest;
import com.paypal.svcs.types.ap.PayResponse;
import com.paypal.svcs.types.ap.Receiver;
import com.paypal.svcs.types.ap.ReceiverList;
import com.paypal.svcs.types.common.RequestEnvelope;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ClientManagementBean implements ClientManagementBeanLocal {

    private static final Double ARTISTBAND_CUT_PERCENTAGE = 0.7; //70%
    private static final String MAIN_PAYPAL_RECIVING_ACCOUNT = "danielmusic@hotmail.com"; // For reciving the 100% of the amount first before passing it to the artists

    @EJB
    private CommonInfrastructureBeanLocal cibl;

    @PersistenceContext(unitName = "DanielMusic-ejbPU")
    private EntityManager em;

    @Override
    public Boolean testPayment(Double totalAmount, Double amount2, Double amount3) {
        try {
            //Create PayPal request
            PayRequest payRequest = new PayRequest();
            List<Receiver> receivers = new ArrayList<Receiver>();
            //Artist (partial of the total)
            Receiver secondaryReceiver = new Receiver();
            secondaryReceiver.setAmount(amount2); //Artist receive this full amount
            secondaryReceiver.setEmail("daniel-artist@hotmail.com");
            receivers.add(secondaryReceiver);

            //Artist (partial of the total)
            secondaryReceiver = new Receiver();
            secondaryReceiver.setAmount(amount3); //Artist receive this full amount
            secondaryReceiver.setEmail("daniel-buyer@hotmail.com");
            receivers.add(secondaryReceiver);

            //Daniel (total amount)
            Receiver primaryReceiver = new Receiver();
            primaryReceiver.setAmount(totalAmount);//total amount to be charged
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
            payRequest.setCurrencyCode("SGD");
            //payRequest.setIpnNotificationUrl("http://replaceIpnUrl.com");

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
            //Login at https://www.sandbox.paypal.com\
            if (payKey != null) {
                return true;
            } else {
                return false;

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public String getPaymentLink(Long accountID, String nonMemberEmail, List<Long> trackIDs, List<Long> albumIDs) {
        System.out.println("getPaymentLink() called");
        try {
            //If accountID is not null, is a registered account purchase. Get it's shopping cart content
            //If null, the other 3 arguments should be filled in
            //Calculate the total amount to be paid
            Account account = null;
            Double totalPaymentAmount = 0.0;
            Set<Music> tracksInCart = null;
            Set<Album> albumInCart = null;
            List<PaymentHelper> partiesReceivingPayments = new ArrayList();
            Artist currentArtist = null;
            Band currentBand = null;

            if (accountID != null) {
                // If it's a logged in account, get from cart
                account = em.getReference(Account.class, accountID);
                if (account == null) {
                    return null;
                }
                // Get his shopping cart
                ShoppingCart shoppingCart = account.getShoppingCart();
                tracksInCart = shoppingCart.getListOfMusics();
                albumInCart = shoppingCart.getListOfAlbums();
            } else { // if it's a non logged in user, retrieve the list of IDs into the actual entities
                for (Long current : trackIDs) {
                    Music music = em.getReference(Music.class, current);
                    if (music == null) {
                        return null;
                    }
                    tracksInCart.add(music);
                }
                for (Long current : albumIDs) {
                    Album album = em.getReference(Album.class, current);
                    if (album == null) {
                        return null;
                    }
                    albumInCart.add(album);
                }
            }

            //Create the list of artist/band to be paid and the amount to be paid for each artist for each music track
            //and calculate the total amount to be paid
            for (Music music : tracksInCart) {
                totalPaymentAmount = totalPaymentAmount + music.getPrice();
                currentArtist = null;
                currentBand = null;
                // If it's a artist
                if (music.getAlbum().getArtist() != null) {
                    currentArtist = music.getAlbum().getArtist();
                    //Loop through the helpers to search for the artist
                    Boolean artistFound = false;
                    for (int i = 0; i < partiesReceivingPayments.size(); i++) {
                        PaymentHelper currentPaymentHelper = partiesReceivingPayments.get(i);
                        //If the artist already exist inside, add to the price for that artist
                        if (currentPaymentHelper.getArtistOrBandPaypalEmail().equals(currentArtist.getPaypalEmail())) {
                            currentPaymentHelper.setTotalPaymentAmount(currentPaymentHelper.getTotalPaymentAmount() + music.getPrice());
                            partiesReceivingPayments.set(i, currentPaymentHelper);
                            artistFound = true;
                            break;
                        }
                    }//If artist not found, create the new paymenthelper and add it to the list
                    if (!artistFound) {
                        PaymentHelper paymentHelper = new PaymentHelper(currentArtist.getPaypalEmail());
                        paymentHelper.setTotalPaymentAmount(music.getPrice());
                    }
                } else if (music.getAlbum().getBand() != null) { //Otherwise if it's a band
                    currentBand = music.getAlbum().getBand();
                    //Loop through the helpers to search for the band
                    Boolean bandFound = false;
                    for (int i = 0; i < partiesReceivingPayments.size(); i++) {
                        PaymentHelper currentPaymentHelper = partiesReceivingPayments.get(i);
                        //If the band already exist inside, add to the price for that band
                        if (currentPaymentHelper.getArtistOrBandPaypalEmail().equals(currentBand.getPaypalEmail())) {
                            currentPaymentHelper.setTotalPaymentAmount(currentPaymentHelper.getTotalPaymentAmount() + music.getPrice());
                            partiesReceivingPayments.set(i, currentPaymentHelper);
                            bandFound = true;
                            break;
                        }
                    }//If band not found, create the new paymenthelper and add it to the list
                    if (!bandFound) {
                        PaymentHelper paymentHelper = new PaymentHelper(currentBand.getPaypalEmail());
                        paymentHelper.setTotalPaymentAmount(music.getPrice());
                    }
                }
            }

            //Create the list of artist/band to be paid and the amount to be paid for each artist for each music track
            //and calculate the total amount to be paid
            for (Album album : albumInCart) {
                totalPaymentAmount = totalPaymentAmount + album.getPrice();
                currentArtist = null;
                currentBand = null;
                // If it's a artist
                if (album.getArtist() != null) {
                    currentArtist = album.getArtist();
                    //Loop through the helpers to search for the artist
                    Boolean artistFound = false;
                    for (int i = 0; i < partiesReceivingPayments.size(); i++) {
                        PaymentHelper currentPaymentHelper = partiesReceivingPayments.get(i);
                        //If the artist already exist inside, add to the price for that artist
                        if (currentPaymentHelper.getArtistOrBandPaypalEmail().equals(currentArtist.getPaypalEmail())) {
                            currentPaymentHelper.setTotalPaymentAmount(currentPaymentHelper.getTotalPaymentAmount() + album.getPrice());
                            partiesReceivingPayments.set(i, currentPaymentHelper);
                            artistFound = true;
                            break;
                        }
                    }//If artist not found, create the new paymenthelper and add it to the list
                    if (!artistFound) {
                        PaymentHelper paymentHelper = new PaymentHelper(currentArtist.getPaypalEmail());
                        paymentHelper.setTotalPaymentAmount(album.getPrice());
                    }
                } else if (album.getBand() != null) { //Otherwise if it's a band
                    currentBand = album.getBand();
                    //Loop through the helpers to search for the band
                    Boolean bandFound = false;
                    for (int i = 0; i < partiesReceivingPayments.size(); i++) {
                        PaymentHelper currentPaymentHelper = partiesReceivingPayments.get(i);
                        //If the band already exist inside, add to the price for that band
                        if (currentPaymentHelper.getArtistOrBandPaypalEmail().equals(currentBand.getPaypalEmail())) {
                            currentPaymentHelper.setTotalPaymentAmount(currentPaymentHelper.getTotalPaymentAmount() + album.getPrice());
                            partiesReceivingPayments.set(i, currentPaymentHelper);
                            bandFound = true;
                            break;
                        }
                    }//If band not found, create the new paymenthelper and add it to the list
                    if (!bandFound) {
                        PaymentHelper paymentHelper = new PaymentHelper(currentBand.getPaypalEmail());
                        paymentHelper.setTotalPaymentAmount(album.getPrice());
                    }
                }
            }

            //Create a payment record in database (without marking it as successful first)
            String UUID = cibl.generateUUID();
            Payment payment = new Payment(totalPaymentAmount, UUID);
            payment.setAlbumPurchased(albumInCart);
            payment.setMusicPurchased(tracksInCart);
            if (totalPaymentAmount == 0.0) { //If free
                if (accountID != null) {
                    payment.setAccount(account);
                } else {
                    payment.setNonMemberEmail(nonMemberEmail);
                }
                payment.setNonMemberEmail(nonMemberEmail);
                payment.setPaymentCompleted(true);
                payment.setDateCompleted(new Date());
                em.persist(payment);
                return "NO_PAYMENT_REQUIRED";
            }
            em.persist(payment);

            //Create PayPal request
            PayRequest payRequest = new PayRequest();
            List<Receiver> receivers = new ArrayList<Receiver>();
            for (int i = 0; i < partiesReceivingPayments.size(); i++) {
                //Artist (partial of the total)
                Receiver secondaryReceiver = new Receiver();
                secondaryReceiver.setAmount(partiesReceivingPayments.get(i).getTotalPaymentAmount() * ARTISTBAND_CUT_PERCENTAGE);
                secondaryReceiver.setEmail(partiesReceivingPayments.get(i).getArtistOrBandPaypalEmail());
                receivers.add(secondaryReceiver);
            }

            //Daniel (total amount)
            Receiver primaryReceiver = new Receiver();
            primaryReceiver.setAmount(totalPaymentAmount);
            primaryReceiver.setEmail(MAIN_PAYPAL_RECIVING_ACCOUNT);
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
            payRequest.setCurrencyCode("SGD");
            //payRequest.setIpnNotificationUrl("http://replaceIpnUrl.com");

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
            if (payKey != null) {
                return "https://www.paypal.com/webscr?cmd=_ap-payment&paykey=" + payKey;
            } else {
                return null;
            }

            //open link in browser
            //Accounts (all password is 12345678): 
            //daniel-buyer@hotmail.com, daniel-artist@hotmail.com, danielmusic@hotmail.com
            //Login at https://www.sandbox.paypal.com
        } catch (Exception ex) {
            System.out.println("getPaymentLink() failed");
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public ReturnHelper completePayment(Long paymentID, String UUID) {
        System.out.println("completePayment() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            Query q = em.createQuery("SELECT e FROM Payment e WHERE e.id=:id");
            q.setParameter("id", paymentID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            Payment payment = (Payment) q.getSingleResult();
            if (!payment.getUUID().equals(UUID)) {
                result.setDescription("Payment could not be completed due to invalid UUID. If you have completed your PayPal payment and see this error message, please contact us.");
            } else if (payment.getPaymentCompleted()) {
                result.setDescription("Payment is already completed.");
            } else {
                payment.setDateCompleted(new Date());
                payment.setPaymentCompleted(true);
                result.setResult(true);
                result.setDescription("Payment completed successfully. Thank you for your purchase!");
                em.merge(payment);
                clearShoppingCart(payment.getAccount().getId());
            }
        } catch (NoResultException ex) {
            result.setDescription("Unable to find a matching payment record in our system. If you have completed your PayPal payment and see this error message, please contact us.");
        } catch (Exception ex) {
            result.setDescription("Internal server error.");
        }
        return result;
    }

    @Override
    public ShoppingCart getShoppingCart(Long accountID) {
        System.out.println("ClientManagementBean: getShoppingCart() called");
        ShoppingCart cart;
        Account account;
        try {
            account = em.getReference(Account.class, accountID);
            cart = account.getShoppingCart();
            System.out.println("ClientManagementBean: getShoppingCart() successfully");
            return cart;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ClientManagementBean: getShoppingCart() failed to execute");
            return null;
        }
    }

    @Override
    public ReturnHelper removeItemFromShoppingCart(Long accountID, Long trackOrAlbumID, Boolean isTrack) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ReturnHelper addItemToShoppingCart(Long accountID, Long trackOrAlbumID, Boolean isTrack) {
        System.out.println("ClientManagementBean: addItemToShoppingCart() called");
        ShoppingCart cart;
        Account account;
        Music music;
        Album album;
        ReturnHelper helper = new ReturnHelper();
        try {
            account = em.getReference(Account.class, accountID);
            cart = account.getShoppingCart();

            if (isTrack) {
                music = em.getReference(Music.class, trackOrAlbumID);
                cart.getListOfMusics().add(music);
            } else {
                album = em.getReference(Album.class, trackOrAlbumID);
                cart.getListOfAlbums().add(album);
            }

            System.out.println("ClientManagementBean: addItemToShoppingCart() successfully");

            helper.setDescription("Item added to cart successfully.");
            helper.setResult(true);
            return helper;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ClientManagementBean: addItemToShoppingCart() failed");
            helper.setDescription("Error occurred while adding to cart.");
            helper.setResult(false);
            return helper;
        }
    }

    @Override
    public ReturnHelper clearShoppingCart(Long accountID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
