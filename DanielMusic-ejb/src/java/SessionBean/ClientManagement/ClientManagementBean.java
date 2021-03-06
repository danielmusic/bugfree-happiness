package SessionBean.ClientManagement;

import EntityManager.Account;
import EntityManager.Album;
import EntityManager.Artist;
import EntityManager.CheckoutHelper;
import EntityManager.Music;
import EntityManager.Payment;
import EntityManager.PaymentHelper;
import EntityManager.ReturnHelper;
import EntityManager.ShoppingCart;
import EntityManager.StartupBean;
import SessionBean.CommonInfrastructure.CommonInfrastructureBeanLocal;
import SessionBean.CommonInfrastructure.SendGridLocal;
import SessionBean.MusicManagement.MusicManagementBeanLocal;
import com.paypal.svcs.services.AdaptivePaymentsService;
import com.paypal.svcs.types.ap.PayRequest;
import com.paypal.svcs.types.ap.PayResponse;
import com.paypal.svcs.types.ap.Receiver;
import com.paypal.svcs.types.ap.ReceiverList;
import com.paypal.svcs.types.common.RequestEnvelope;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ClientManagementBean implements ClientManagementBeanLocal {

    private static final Logger log = Logger.getLogger(StartupBean.class.getName());

    private static final Double ARTISTBAND_CUT_PERCENTAGE = 0.7; //70%
    private static final String MAIN_PAYPAL_RECIVING_ACCOUNT = "admin@sounds.sg"; // For reciving the 100% of the amount first before passing it to the artists

    @EJB
    private CommonInfrastructureBeanLocal cibl;

    @EJB
    private MusicManagementBeanLocal mmbl;

    @EJB
    private SendGridLocal sgl;

    @PersistenceContext(unitName = "DanielMusic-ejbPU")
    private EntityManager em;

//    @Override
//    public Boolean testPayment(Double totalAmount, Double amount2, Double amount3) {
//        try {
//            //Create PayPal request
//            PayRequest payRequest = new PayRequest();
//            List<Receiver> receivers = new ArrayList<Receiver>();
//            //Artist (partial of the total)
//            Receiver secondaryReceiver = new Receiver();
//            secondaryReceiver.setAmount(amount2); //Artist receive this full amount
//            secondaryReceiver.setEmail("daniel-artist@hotmail.com");
//            secondaryReceiver.setPaymentType("DIGITALGOODS");
//            receivers.add(secondaryReceiver);
//
//            //Artist (partial of the total)
//            secondaryReceiver = new Receiver();
//            secondaryReceiver.setAmount(amount3); //Artist receive this full amount
//            secondaryReceiver.setEmail("daniel-artist2@hotmail.com");
//            secondaryReceiver.setPaymentType("DIGITALGOODS");
//            receivers.add(secondaryReceiver);
//
//            //Daniel (total amount)
//            Receiver primaryReceiver = new Receiver();
//            primaryReceiver.setAmount(totalAmount);//total amount to be charged
//            primaryReceiver.setEmail("admin@sounds.sg");
//            primaryReceiver.setPaymentType("DIGITALGOODS");
//            primaryReceiver.setPrimary(true);
//            receivers.add(primaryReceiver);
//
//            ReceiverList receiverList = new ReceiverList(receivers);
//            payRequest.setReceiverList(receiverList);
//
//            RequestEnvelope requestEnvelope = new RequestEnvelope("en_US");
//            payRequest.setRequestEnvelope(requestEnvelope);
//            payRequest.setActionType("PAY");
//            payRequest.setFeesPayer("PRIMARYRECEIVER");
//            payRequest.setCancelUrl("http://sounds.sg/#!/payment-cancelled");//Return if payment cancelled
//            payRequest.setReturnUrl("http://sounds.sg/#!/payment-success");//Return after payment complete
//            payRequest.setCurrencyCode("SGD");
//            //payRequest.setIpnNotificationUrl("http://replaceIpnUrl.com");
//
//            Map<String, String> sdkConfig = new HashMap<String, String>();
//            sdkConfig.put("mode", "sandbox");
//            sdkConfig.put("acct1.UserName", "admin_api2.sounds.sg");
//            sdkConfig.put("acct1.Password", "HHPY7E2L5PSPXXPB");
//            sdkConfig.put("acct1.Signature", "AFcWxV21C7fd0v3bYYYRCpSSRl31AnFFn0o9OOq7PrTE0l.RWx3ZgAsB");
//            sdkConfig.put("acct1.AppId", "APP-80W284485P519543T");
//
//            AdaptivePaymentsService adaptivePaymentsService = new AdaptivePaymentsService(sdkConfig);
//            PayResponse payResponse = adaptivePaymentsService.pay(payRequest);
//
//            log.info(payResponse.getPaymentExecStatus());
//            String payKey = payResponse.getPayKey();
//            //log.info("Open this link in browser: https://www.sandbox.paypal.com/webscr?cmd=_ap-payment&paykey=" + payKey);
//            //open link in browser
//            //Accounts (all password is 12345678): 
//            //daniel-buyer@hotmail.com, daniel-artist@hotmail.com, danielmusic@hotmail.com
//            //Login at https://www.sandbox.paypal.com\
//            if (payKey != null) {
//                return true;
//            } else {
//                return false;
//
//            }
//        } catch (Exception ex) {
//            log.info(ex.getMessage());
//            return false;
//        }
//    }
    @Override
    public CheckoutHelper getPayKey(Long accountID, String nonMemberEmail, Set<Music> tracksInCart, Set<Album> albumInCart) {
        CheckoutHelper checkoutHelper = new CheckoutHelper();
        log.info("getPayKey() called");
        try {
            //If accountID is not null, is a registered account purchase. Get it's shopping cart content
            //If null, the other 3 arguments should be filled in
            //Calculate the total amount to be paid
            Account account = null;
            Double totalPaymentAmount = 0.0;
            List<PaymentHelper> partiesReceivingPayments = new ArrayList();
            Artist currentArtist = null;

            if (accountID != null) {
                // If it's a logged in account, get from cart
                Query q = em.createQuery("SELECT E FROM Account E where E.id=:accountID");
                q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                q.setParameter("accountID", accountID);
                account = (Account) q.getSingleResult();
                if (account == null) {
                    checkoutHelper.setMessage("Youra account does not appear to be valid. Try logging out and login again before checking out.");
                    return null;
                }
            }

            //Create the list of artist/band to be paid and the amount to be paid for each artist for each music track
            //and calculate the total amount to be paid
            //Also create the list of pricing in the payment
            List<Music> listOfMusicsInCart = new ArrayList();
            List<Double> listOfMusicPrices = new ArrayList();
            List<Album> listOfAlbumsInCart = new ArrayList();
            List<Double> listOfAlbumPrices = new ArrayList();
            for (Music music : tracksInCart) {
                listOfMusicsInCart.add(music);
                listOfMusicPrices.add(music.getPrice());
                totalPaymentAmount = totalPaymentAmount + music.getPrice();
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
                    partiesReceivingPayments.add(paymentHelper);
                }

            }

            //Create the list of artist/band to be paid and the amount to be paid for each artist for each music track
            //and calculate the total amount to be paid
            for (Album album : albumInCart) {
                listOfAlbumsInCart.add(album);
                listOfAlbumPrices.add(album.getPrice());
                totalPaymentAmount = totalPaymentAmount + album.getPrice();
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
                    partiesReceivingPayments.add(paymentHelper);
                }
            }

            //Check if items has been purchased before
            if (account != null) {
                List<Music> purchasedMusic = account.getListOfPurchasedMusics();
                for (Music music : listOfMusicsInCart) {
                    if (purchasedMusic.contains(music)) {
                        checkoutHelper.setMessage("Unable to checkout as you have already purchased the music \"" + music.getName() + "\" by the artist \"" + music.getArtistName() + "\" before.");
                        return checkoutHelper;
                    }
                }
            }

            //Create a payment record in database (without marking it as successful first)
            String UUID = cibl.generateUUID();
            Payment payment = new Payment(totalPaymentAmount, UUID);
            payment.setAlbumPurchased(listOfAlbumsInCart);
            payment.setAlbumPrices(listOfAlbumPrices);
            payment.setMusicPurchased(listOfMusicsInCart);
            payment.setMusicPrices(listOfMusicPrices);
            //Tie payment to account
            if (accountID != null) {
                payment.setAccount(account);
            } else {
                payment.setNonMemberEmail(nonMemberEmail);
            }
            //Complete payment directly if free
            if (totalPaymentAmount == 0.0) {
                em.persist(payment);
                checkoutHelper.setPayment(payment);
                if (completePayment(payment.getId(), UUID).getResult()) {
                    checkoutHelper.setPayKey("NO_PAYMENT_REQUIRED_PAYMENT_COMPLETE");
                } else {
                    checkoutHelper.setPayKey("NO_PAYMENT_REQUIRED_FAILED");
                }
                return checkoutHelper;
            }
            em.persist(payment);
            checkoutHelper.setPayment(payment);
            //Create PayPal request
            PayRequest payRequest = new PayRequest();
            List<Receiver> receivers = new ArrayList<Receiver>();
            for (int i = 0; i < partiesReceivingPayments.size(); i++) {
                //Artist (partial of the total)
                Receiver secondaryReceivers = new Receiver();
                secondaryReceivers.setAmount(Math.round(partiesReceivingPayments.get(i).getTotalPaymentAmount() * ARTISTBAND_CUT_PERCENTAGE * 100.0) / 100.0);
                secondaryReceivers.setEmail(partiesReceivingPayments.get(i).getArtistOrBandPaypalEmail());
                secondaryReceivers.setPaymentType("DIGITALGOODS");
//                secondaryReceivers.setPrimary(Boolean.TRUE);
                receivers.add(secondaryReceivers);
            }

            //Daniel (full total amount)
            Receiver primaryReceiver = new Receiver();
//            secondaryReceiver.setAmount(Math.round(totalPaymentAmount * (1 - ARTISTBAND_CUT_PERCENTAGE) * 100.0)/100.0);
            primaryReceiver.setAmount(totalPaymentAmount);
            primaryReceiver.setEmail(MAIN_PAYPAL_RECIVING_ACCOUNT);
            primaryReceiver.setPrimary(Boolean.TRUE);
            primaryReceiver.setPaymentType("DIGITALGOODS");
            receivers.add(primaryReceiver);

            ReceiverList receiverList = new ReceiverList(receivers);
            payRequest.setReceiverList(receiverList);

            RequestEnvelope requestEnvelope = new RequestEnvelope("en_US");
            payRequest.setRequestEnvelope(requestEnvelope);
            payRequest.setActionType("PAY");
            payRequest.setFeesPayer("PRIMARYRECEIVER");
            payRequest.setCancelUrl("http://sounds.sg/payment-cancelled.jsp");//Return if payment cancelled
            payRequest.setReturnUrl("http://sounds.sg/MusicManagementController?target=CompletePayment&paymentID=" + payment.getId() + "&UUID=" + payment.getUUID());//Return after payment complete
//            payRequest.setCancelUrl("http://localhost:8080/DanielMusic-war/payment-cancelled.jsp");//Return if payment cancelled
//            payRequest.setReturnUrl("http://localhost:8080/DanielMusic-war/MusicManagementController?target=CompletePayment&paymentID=" + payment.getId() + "&UUID=" + payment.getUUID());//Return after payment complete
            payRequest.setCurrencyCode("SGD");
            //payRequest.setIpnNotificationUrl("http://replaceIpnUrl.com");

            Map<String, String> sdkConfig = new HashMap<String, String>();
            sdkConfig.put("mode", "live");
            //yg sandbox enviroment
//            sdkConfig.put("acct1.UserName", "jb-us-seller_api1.paypal.com");
//            sdkConfig.put("acct1.Password", "WX4WTU3S8MY44S7F");
//            sdkConfig.put("acct1.Signature", "AFcWxV21C7fd0v3bYYYRCpSSRl31A7yDhhsPUU2XhtMoZXsWHFxu-RWy");
//            sdkConfig.put("acct1.AppId", "APP-80W284485P519543T");

            //admin@sounds.sg sandbox enviroment
//            sdkConfig.put("acct1.UserName", "admin_api2.sounds.sg");
//            sdkConfig.put("acct1.Password", "HHPY7E2L5PSPXXPB");
//            sdkConfig.put("acct1.Signature", "AFcWxV21C7fd0v3bYYYRCpSSRl31AnFFn0o9OOq7PrTE0l.RWx3ZgAsB");
//            sdkConfig.put("acct1.AppId", "APP-80W284485P519543T");

            //admin@sounds.sg live enviroment
            sdkConfig.put("acct1.UserName", "admin_api1.sounds.sg");
            sdkConfig.put("acct1.Password", "MCDXCV9Z5CUBPGQL");
            sdkConfig.put("acct1.Signature", "AK-3.v.BuHWiLizR6luASn-.zTHiAXXLSUxEFphZj0e3AJwluUoE16b7");
            sdkConfig.put("acct1.AppId", "APP-2G515359GL3090930");

            AdaptivePaymentsService adaptivePaymentsService = new AdaptivePaymentsService(sdkConfig);
            PayResponse payResponse = adaptivePaymentsService.pay(payRequest);

            log.info(payResponse.getPaymentExecStatus());
            String payKey = payResponse.getPayKey();
            log.info("Open this link in browser: https://www.sandbox.paypal.com/webapps/adaptivepayment/flow/pay?paykey=" + payKey);
            checkoutHelper.setPayKey(payKey);
            return checkoutHelper;

            //open link in browser
            //Accounts (all password is 12345678): 
            //daniel-buyer@hotmail.com, daniel-artist@hotmail.com, danielmusic@hotmail.com
            //Login at https://www.sandbox.paypal.com
        } catch (Exception ex) {
            log.log(Level.SEVERE, "getPayKey() failed");
            log.log(Level.SEVERE, ex.getMessage());
            return null;
        }
    }

    @Override
    public Payment getPayment(Long paymentID) {
        log.info("getPayment() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            Query q = em.createQuery("SELECT e FROM Payment e WHERE e.id=:id");
            q.setParameter("id", paymentID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            Payment payment = (Payment) q.getSingleResult();
            return payment;
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            log.info(ex.getMessage());
            return null;
        }
    }

//    @Override
//    public DownloadHelper getPurchaseDownloadLinks(Long paymentID) {
//        log.info("getPurchaseDownloadLinks() called");
//        List<DownloadHelper> purchaseDownloads = new ArrayList();
//        try {
//            Payment payment = getPayment(paymentID);
//            List<Music> listOfPurchasedMusic = new ArrayList();
//            List<String> downloadLinks128 = new ArrayList();
//            List<String> downloadLinks320 = new ArrayList();
//            List<String> downloadLinksWav = new ArrayList();
//
//            Iterator iterator = payment.getAlbumPurchased().iterator();
//            while (iterator.hasNext()) {
//                Album album = (Album) iterator.next();
//                for (Music m : album.getListOfMusics()) {
//                    listOfPurchasedMusic.add(m);
//                    downloadLinksWav.add(mmbl.generateDownloadLink(m.getId(), "wav", Boolean.TRUE));
//                    downloadLinks128.add(mmbl.generateDownloadLink(m.getId(), "128", Boolean.TRUE));
//                    downloadLinks320.add(mmbl.generateDownloadLink(m.getId(), "320", Boolean.TRUE));
//                }
//            }
//
//            iterator = payment.getMusicPurchased().iterator();
//            while (iterator.hasNext()) {
//                Music m = (Music) iterator.next();
//                listOfPurchasedMusic.add(m);
//                downloadLinksWav.add(mmbl.generateDownloadLink(m.getId(), "wav", Boolean.TRUE));
//                downloadLinks128.add(mmbl.generateDownloadLink(m.getId(), "128", Boolean.TRUE));
//                downloadLinks320.add(mmbl.generateDownloadLink(m.getId(), "320", Boolean.TRUE));
//            }
//            DownloadHelper downloadHelper = new DownloadHelper();
//            downloadHelper.setDownloadLinksWav(downloadLinksWav);
//            downloadHelper.setDownloadLinks128(downloadLinks128);
//            downloadHelper.setDownloadLinks320(downloadLinks320);
//
//            return downloadHelper;
//        } catch (NoResultException ex) {
//            return null;
//        } catch (Exception ex) {
//            log.info(ex.getMessage());
//            return null;
//        }
//    }
    @Override
    public ReturnHelper completePayment(Long paymentID, String UUID) {
        log.info("completePayment() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            Query q = em.createQuery("SELECT e FROM Payment e WHERE e.id=:id");
            q.setParameter("id", paymentID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            Payment payment = (Payment) q.getSingleResult();
            if (!payment.getUUID().equals(UUID)) {
                log.log(Level.SEVERE, "completePayment(): UUID does not match");
                result.setDescription("Payment could not be completed due to invalid UUID. If you have completed your PayPal payment and see this error message, please contact us.");
                return result;
            } else if (payment.getPaymentCompleted()) {
                log.info("completePayment(): Payment already completed");
                result.setDescription("Payment completed successfully. Thank you for your purchase!");
                result.setResult(true);
                return result;
            } else {
                payment.setDateCompleted(new Date());
                payment.setPaymentCompleted(true);
                em.merge(payment);
                result.setResult(true);
                result.setID(payment.getId());
                result.setDescription("Payment completed successfully. Thank you for your purchase!");
                Account account = payment.getAccount();
                //Increase purchase count
                //And add the purchased music to the user list of purchased music
                Iterator iterator = payment.getMusicPurchased().iterator();
                while (iterator.hasNext()) {
                    Music music = (Music) iterator.next();
                    music.setNumPurchase(music.getNumPurchase() + 1);
                    if (account != null) { //If payment is tied to an account
                        account.getListOfPurchasedMusics().add(music);
                    }
                }
                iterator = payment.getAlbumPurchased().iterator();
                while (iterator.hasNext()) {
                    Album album = (Album) iterator.next();
                    album.setNumPurchase(album.getNumPurchase() + 1);
                    if (account != null) { //If payment is tied to an account
                        for (Music music : album.getListOfMusics()) {
                            account.getListOfPurchasedMusics().add(music);
                        }
                    }
                }
                //Only clear shopping cart if account not null
                if (account != null) {
                    clearShoppingCart(account.getId());
                }
                //Notify artists
                notifyArtistsOfCustomerPurchase(paymentID);
                //Send buyer an email
                ReturnHelper result2 = sendDownloadLinkToBuyer(paymentID);
                payment.setPaymentCompleted(true);
                payment.setDateCompleted(new Date());
                em.flush();
                if (!result2.getResult()) {
                    if (payment.getAccount() != null) {
                        result.setDescription("Payment completed but our system was unable to email you the download links. Please view your profile to retrieve the download links.");
                        return result;
                    } else {
                        result.setDescription("Payment completed but our system was unable to email you the download links. Contact admin@sounds.sg if you are unable to download your music on this page.");
                        return result;
                    }
                }
                result.setDescription("Payment completed");
                result.setResult(true);
                return result;
            }
        } catch (NoResultException ex) {
            result.setDescription("Unable to find a matching payment record in our system. If you have completed your PayPal payment and see this error message, please contact us.");
        } catch (Exception ex) {
            result.setDescription("Internal server error.");
            log.log(Level.SEVERE, ex.getMessage());
        }
        return result;
    }

    @Override
    public ShoppingCart getShoppingCart(Long accountID) {
        log.info("ClientManagementBean: getShoppingCart() called");
        ShoppingCart cart;
        Account account;
        try {
            Query q = em.createQuery("Select a from Account a where a.id=:accountID");
            q.setParameter("accountID", accountID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            account = (Account) q.getSingleResult();
            // account = em.getReference(Account.class, accountID);

            cart = account.getShoppingCart();
            log.info("ClientManagementBean: getShoppingCart() successfully");
            return cart;
        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("ClientManagementBean: getShoppingCart() failed to execute");
            return null;
        }
    }

    @Override
    public ReturnHelper removeItemFromShoppingCart(Long accountID, Long trackOrAlbumID, Boolean isTrack) {
        log.info("ClientManagementBean: removeItemFromShoppingCart() called");
        ReturnHelper helper = new ReturnHelper();
        Account account;
        ShoppingCart cart;
        Boolean result;
        try {
            Query q = em.createQuery("Select a from Account a where a.id=:accountID");
            q.setParameter("accountID", accountID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            account = (Account) q.getSingleResult();
            cart = account.getShoppingCart();
            em.merge(cart);
            if (isTrack) {
                log.info("ClientManagementBean: removeItemFromShoppingCart(): Removing track from cart...");
                q = em.createQuery("Select m from Music m where m.id=:trackOrAlbumID");
                q.setParameter("trackOrAlbumID", trackOrAlbumID);
                q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                Music m = (Music) q.getSingleResult();
                result = cart.getListOfMusics().remove(m);
                em.flush();

                if (result) {
                    log.info("ClientManagementBean: removeItemFromShoppingCart(): Track has been removed from cart");
                    helper.setDescription("Track has been removed from cart successfully.");
                    helper.setResult(true);
                } else {
                    log.info("ClientManagementBean: removeItemFromShoppingCart(): Failed to remove track from cart");
                    helper.setDescription("Error while removing track from cart, please try again.");
                    helper.setResult(false);
                }
            } else {
                log.info("ClientManagementBean: removeItemFromShoppingCart(): Removing album from cart...");
                q = em.createQuery("Select a from Album a where a.id=:trackOrAlbumID");
                q.setParameter("trackOrAlbumID", trackOrAlbumID);
                q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                Album a = (Album) q.getSingleResult();
                result = cart.getListOfAlbums().remove(a);
                em.flush();

                if (result) {
                    log.info("ClientManagementBean: removeItemFromShoppingCart(): Album has been removed from cart");
                    helper.setDescription("A;bum has been removed from cart successfully.");
                    helper.setResult(true);
                } else {
                    log.info("ClientManagementBean: removeItemFromShoppingCart(): Failed to remove album from cart");
                    helper.setDescription("Error while removing album from cart, please try again.");
                    helper.setResult(false);
                }
            }
            return helper;
        } catch (Exception e) {
            log.info("ClientManagementBean: removeItemFromShoppingCart() exception occurred");
            helper.setDescription("An error occurred while removing item from shopping cart, please try again.");
            helper.setResult(false);
            return helper;
        }
    }

    @Override
    public ReturnHelper addItemToShoppingCart(Long accountID, Long trackOrAlbumID, Boolean isTrack) {
        log.info("ClientManagementBean: addItemToShoppingCart() called");
        ShoppingCart cart;
        Music music;
        Album album;
        ReturnHelper helper = new ReturnHelper();
        try {
            Query q = em.createQuery("Select sc from Account sc where sc.id=:accountID");
            q.setParameter("accountID", accountID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            Account account = (Account) q.getSingleResult();
            cart = account.getShoppingCart();
            if (cart == null) {
                cart = new ShoppingCart();
                cart.setAccount(account);
                account.setShoppingCart(cart);
                cart.setListOfAlbums(new HashSet<>());
                cart.setListOfMusics(new HashSet<>());
                em.persist(cart);
            }

            Boolean result;
            if (isTrack) {
                log.info("addItemToShoppingCart: inside isTrack");
                q = em.createQuery("Select m from Music m where m.id=:trackOrAlbumID");
                q.setParameter("trackOrAlbumID", trackOrAlbumID);
                q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                music = (Music) q.getSingleResult();
                Set<Music> musicSet = cart.getListOfMusics();
                result = musicSet.add(music);
                cart.setListOfMusics(musicSet);
                em.merge(cart);
            } else {
                log.info("addItemToShoppingCart: inside isAlbum");

                q = em.createQuery("Select a from Album a where a.id=:trackOrAlbumID");
                q.setParameter("trackOrAlbumID", trackOrAlbumID);
                q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                album = (Album) q.getSingleResult();
                Set<Album> albumSet = cart.getListOfAlbums();
                result = albumSet.add(album);
                cart.setListOfAlbums(albumSet);
                em.merge(cart);
            }
            if (!result) {
                helper.setDescription("Failed to add item to cart, please try again. Note that duplicate item cannot be added.");
                helper.setResult(false);
                return helper;
            }
            log.info("ClientManagementBean: addItemToShoppingCart() successfully");

            helper.setDescription("Item has been added to cart successfully.");
            helper.setResult(true);
            return helper;
        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("ClientManagementBean: addItemToShoppingCart() failed");
            helper.setDescription("Error occurred while adding item to cart.");
            helper.setResult(false);
            return helper;
        }
    }

    @Override
    public ReturnHelper clearShoppingCart(Long accountID) {
        log.info("ClientManagementBean: clearShoppingCart() called");
        ShoppingCart cart;
        ReturnHelper helper = new ReturnHelper();
        try {
            Query q = em.createQuery("Select sc from ShoppingCart sc where sc.account.id=:accountID");
            q.setParameter("accountID", accountID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            cart = (ShoppingCart) q.getSingleResult();

            cart.getListOfAlbums().removeAll(cart.getListOfAlbums());
            cart.getListOfMusics().removeAll(cart.getListOfMusics());
            log.info("ClientManagementBean: clearShoppingCart() successfully");

            helper.setDescription("Shopping cart has been cleared successfully.");
            helper.setResult(true);
            return helper;
        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("ClientManagementBean: clearShoppingCart() failed");
            helper.setDescription("Failed to clear shopping cart please try again.");
            helper.setResult(false);
            return helper;
        }
    }

    @Override
    public Boolean checkArtistPayPalEmailExists(Long trackOrAlbumID, Boolean isTrack) {
        log.info("ClientManagementBean: checkArtistPayPalEmailExists() called");
        try {
            Query q;
            if (isTrack) {
                q = em.createQuery("Select m from Music m where m.id=:trackOrAlbumID");
                q.setParameter("trackOrAlbumID", trackOrAlbumID);
                q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                Music m = (Music) q.getSingleResult();
                if (m.getAlbum().getArtist().getPaypalEmail() != null) {
                    return true;
                }
            } else {
                q = em.createQuery("Select a from Album a where a.id=:trackOrAlbumID");
                q.setParameter("trackOrAlbumID", trackOrAlbumID);
                q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                Album a = (Album) q.getSingleResult();
                if (a.getArtist().getPaypalEmail() != null) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            log.info("ClientManagementBean: checkArtistPayPalEmailExists() failed");
        }
        return false;
    }

    @Override
    public ReturnHelper notifyArtistsOfCustomerPurchase(Long paymentID) {
        log.info("notifyArtistsOfCustomerPurchase() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            Query q = em.createQuery("Select m from Payment m where m.id=:paymentID");
            q.setParameter("paymentID", paymentID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            Payment payment = (Payment) q.getSingleResult();
            List<Album> listOfAlbums = payment.getAlbumPurchased();
            List<Music> listOfMusics = payment.getMusicPurchased();
            //1 Figure out the list of artist
            // for each artist
            HashSet<Artist> setOfArtists = new HashSet();
            for (Album a : listOfAlbums) {
                setOfArtists.add(a.getArtist());
            }
            for (Music m : listOfMusics) {
                setOfArtists.add(m.getAlbum().getArtist());
            }
            for (Artist artist : setOfArtists) {
                String emailTemplate = "<body style=\"font-family: arial\">\n"
                        + "<p align=\"left\" style=\"margin-left:150px;\"><img src=\"http://sounds.sg/img/EmailGraphic.png\" width=\"100px\"/></p>"
                        + "<h2>Hey " + artist.getName() + ",</h2>";
                String buyerName = "unregistered user";
                if (payment.getAccount() != null) {
                    if (payment.getAccount().getName() == null || payment.getAccount().getName().isEmpty()) {
                        buyerName = "a fan";
                    } else {
                        buyerName = payment.getAccount().getName();
                    }
                }
                emailTemplate += "<br/><span style=\"color:#E34529;font-size:200%;font-weight:bold;\">" + buyerName + " purchased your album/track(s) listed below:</span><br/><br/>";

                //2 Create the list of albums/musics for each artist
                Boolean first = true;
                for (Album a : listOfAlbums) {
                    if (a.getArtist().getId().equals(artist.getId())) {
                        if (first) {
                            first = false;
                            emailTemplate += "<h2>Albums</h2><ol>";
                        }
                        emailTemplate += "<li>" + a.getName() + "</li>";
                    }
                }
                if (first == false) {
                    emailTemplate += "</ol>";
                }
                first = true;
                for (Music m : listOfMusics) {
                    if (m.getAlbum().getArtist().getId().equals(artist.getId())) {
                        if (first) {
                            first = false;
                            emailTemplate += "<h2>Tracks</h2><ol>";
                        }
                        emailTemplate += "<li> " + m.getAlbum().getName() + ": " + m.getName() + "</li>";

                    }
                }
                if (first == false) {
                    emailTemplate += "</ol>";
                }

                emailTemplate += "The payment has been credited into your PayPal account.<br/><br/>"
                        + "Have a great day!<br/>"
                        + "The <a href=\"http://sounds.sg\" style=\"color:inherit;text-decoration:none;\">sounds.sg</a> team<br/>"
                        + "<br/>"
                        + "<hr/>"
                        + "<span style=\"font-size: 70%;color: #969696;\">&copy; 2016 - <a href=\"http://sounds.sg\" style=\"color:inherit;text-decoration:none;\">SOUNDS.SG</a>, ALL RIGHTS RESERVED</span></body>";
                //Send using sendgrid
                if (sgl.sendEmail(artist.getEmail(), "no-reply@sounds.sg", "Someone bought your sounds!", emailTemplate)) {
                    result.setDescription("Artist notification sent.");
                    result.setResult(true);
                } else {
                    result.setDescription("Failed to notify artists");
                }
            }

            result.setResult(true);
            result.setDescription("Artist notified of the purchase");
        } catch (Exception ex) {
            result.setDescription("Internal server error");
            log.info(ex.getMessage());
        }
        return result;
    }

    @Override
    public ReturnHelper sendDownloadLinkToBuyer(Long paymentID) {
        log.info("sendDownloadLinkToBuyer() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            //Retrieve payment
            Query q = em.createQuery("Select m from Payment m where m.id=:paymentID");
            q.setParameter("paymentID", paymentID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            Payment payment = (Payment) q.getSingleResult();
            //Get either member email or email tied to payment
            String buyerEmail = "";
            if (payment.getAccount() != null) {
                buyerEmail = payment.getAccount().getEmail();
            } else {
                buyerEmail = payment.getNonMemberEmail();
            }

            //Retrieve list of musics/albums purchased & generate links for each of them
            List<Music> listOfMusics = payment.getMusicPurchased();
            List<Album> listOfAlbums = payment.getAlbumPurchased();

            //Create the email template
            String emailTemplate = "<body style=\"font-family: arial\">\n"
                    + "<p align=\"left\" style=\"margin-left:150px;\"><img src=\"http://sounds.sg/img/EmailGraphic.png\" width=\"100px\"/></p>";
            if (payment.getAccount() != null) {
                if (payment.getAccount().getName() == null || payment.getAccount().getName().isEmpty()) {
                    emailTemplate += "<span style=\"font-size: 150%;\">Hey there,</span>";
                } else {
                    emailTemplate += "<h2>Hey " + payment.getAccount().getName() + ",</h2>";
                }
            }
            emailTemplate += "<br/><span style=\"color:#E34529;font-size:200%;font-weight:bold;\">Thank you for purchasing from sounds.sg</span><br/><br/>"
                    + "To start your download, please click on the following link(s) below:";

            //album first then music
            if (listOfAlbums != null && listOfAlbums.size() > 0) {
                emailTemplate += ""
                        + "<h2>Albums</h2>"
                        + "<table border=1>"
                        + "    <thead>"
                        + "        <tr>  "
                        + "            <th>Track</th>"
                        + "            <th>Album</th>"
                        + "            <th>Artist</th>"
                        + "            <th>Download</th>"
                        + "        </tr>"
                        + "    </thead>"
                        + "    <tbody>";
                for (Album a : listOfAlbums) {
                    for (Music m : a.getListOfMusics()) {
                        emailTemplate += "        <tr>"
                                + "            <td>"
                                + m.getName()
                                + "            </td>"
                                + "            <td>"
                                + a.getName()
                                + "            </td>"
                                + "            <td>"
                                + m.getArtistName()
                                + "            </td>"
                                + "            <td>"
                                + "                <a href='" + mmbl.generateDownloadLink(m.getId(), "128", Boolean.TRUE, 43200L) + "'>.mp3(128kbps)</a>&nbsp;&nbsp;"
                                + "                <a href='" + mmbl.generateDownloadLink(m.getId(), "320", Boolean.TRUE, 43200L) + "'>.mp3(320kbps)</a>&nbsp;&nbsp;"
                                + "                <a href='" + mmbl.generateDownloadLink(m.getId(), "wav", Boolean.TRUE, 43200L) + "'>.wav</a> "
                                + "            </td>"
                                + "        </tr>";
                    }
                }
                emailTemplate += "    </tbody>"
                        + "</table>";
            }
            if (listOfMusics != null && listOfMusics.size() > 0) {
                emailTemplate += "<h2>Tracks</h2>"
                        + "<table border=1>"
                        + "    <thead>"
                        + "        <tr>  "
                        + "            <th>Track</th>"
                        + "            <th>Album</th>"
                        + "            <th>Artist</th>"
                        + "            <th>Download</th>"
                        + "        </tr>"
                        + "    </thead>"
                        + "    <tbody>";
                for (Music m : listOfMusics) {
                    emailTemplate += "        <tr>"
                            + "            <td>"
                            + m.getName()
                            + "            </td>"
                            + "            <td>"
                            + m.getAlbum().getName()
                            + "            </td>"
                            + "            <td>"
                            + m.getArtistName()
                            + "            </td>"
                            + "            <td>"
                            + "                <a href='" + mmbl.generateDownloadLink(m.getId(), "128", Boolean.TRUE, 43200L) + "'>.mp3(128kbps)</a>&nbsp;&nbsp;"
                            + "                <a href='" + mmbl.generateDownloadLink(m.getId(), "320", Boolean.TRUE, 43200L) + "'>.mp3(320kbps)</a>&nbsp;&nbsp;"
                            + "                <a href='" + mmbl.generateDownloadLink(m.getId(), "wav", Boolean.TRUE, 43200L) + "'>.wav</a> "
                            + "            </td>"
                            + "        </tr>";
                }
                emailTemplate += "    </tbody>"
                        + "</table>";
            }
            emailTemplate += "The links in this email are only valid for the next 12 hours.<br/><br/>"
                    + "(If you didn't make this purchase, enjoy some Singaporean sounds anyway — on us!)<br/><br/>"
                    + "Enjoy!<br/>"
                    + "The <a href=\"http://sounds.sg\" style=\"color:inherit;text-decoration:none;\">sounds.sg</a> team<br/>"
                    + "<br/>"
                    + "<hr/>"
                    + "<span style=\"font-size: 70%;color: #969696;\">&copy; 2016 - <a href=\"http://sounds.sg\" style=\"color:inherit;text-decoration:none;\">SOUNDS.SG</a>, ALL RIGHTS RESERVED</span></body>";
            //Send using sendgrid
            if (sgl.sendEmail(buyerEmail, "no-reply@sounds.sg", "Your sounds.sg Purchase", emailTemplate)) {
                result.setDescription("Download links sent.");
                result.setResult(true);
            } else {
                result.setDescription("Failed to send download links to email");
            }
        } catch (NoResultException ex) {
            result.setDescription("Unable to retrieve payment record.");
        } catch (Exception ex) {
            result.setDescription("Internal server error");
            log.info(ex.getMessage());
        }
        return result;
    }

}
