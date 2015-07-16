package SessionBean.ClientManagement;

import EntityManager.Account;
import EntityManager.Album;
import EntityManager.Artist;
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
import java.util.HashSet;
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
    private static final String MAIN_PAYPAL_RECIVING_ACCOUNT = "admin@sounds.sg"; // For reciving the 100% of the amount first before passing it to the artists

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
            secondaryReceiver.setPaymentType("DIGITALGOODS");
            receivers.add(secondaryReceiver);

            //Artist (partial of the total)
            secondaryReceiver = new Receiver();
            secondaryReceiver.setAmount(amount3); //Artist receive this full amount
            secondaryReceiver.setEmail("daniel-artist2@hotmail.com");
            secondaryReceiver.setPaymentType("DIGITALGOODS");
            receivers.add(secondaryReceiver);

            //Daniel (total amount)
            Receiver primaryReceiver = new Receiver();
            primaryReceiver.setAmount(totalAmount);//total amount to be charged
            primaryReceiver.setEmail("admin@sounds.sg");
            primaryReceiver.setPaymentType("DIGITALGOODS");
            primaryReceiver.setPrimary(true);
            receivers.add(primaryReceiver);

            ReceiverList receiverList = new ReceiverList(receivers);
            payRequest.setReceiverList(receiverList);

            RequestEnvelope requestEnvelope = new RequestEnvelope("en_US");
            payRequest.setRequestEnvelope(requestEnvelope);
            payRequest.setActionType("PAY");
            payRequest.setFeesPayer("PRIMARYRECEIVER");
            payRequest.setCancelUrl("http://localhost:8080/DanielMusic-war/#!/payment-cancelled");//Return if payment cancelled
            payRequest.setReturnUrl("http://localhost:8080/DanielMusic-war/#!/payment-success");//Return after payment complete
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
            System.out.println("Open link: https://www.sandbox.paypal.com/webapps/adaptivepayment/flow/pay?paykey=" + payKey);
            //System.out.println("Open this link in browser: https://www.sandbox.paypal.com/webscr?cmd=_ap-payment&paykey=" + payKey);
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
    public String getPaymentLink(Long accountID, String nonMemberEmail, Set<Music> tracksInCart, Set<Album> albumInCart) {
        System.out.println("getPaymentLink() called");
        try {
            //If accountID is not null, is a registered account purchase. Get it's shopping cart content
            //If null, the other 3 arguments should be filled in
            //Calculate the total amount to be paid
            Account account = null;
            Double totalPaymentAmount = 0.0;
//            Set<Music> tracksInCart = null;
//            Set<Album> albumInCart = null;
            List<PaymentHelper> partiesReceivingPayments = new ArrayList();
            Artist currentArtist = null;

            if (accountID != null) {
                // If it's a logged in account, get from cart
                account = em.getReference(Account.class, accountID);
                if (account == null) {
                    return null;
                }
//                // Get his shopping cart
//                ShoppingCart shoppingCart = account.getShoppingCart();
//                tracksInCart = shoppingCart.getListOfMusics();
//                albumInCart = shoppingCart.getListOfAlbums();
//            } else { // if it's a non logged in user, retrieve the list of IDs into the actual entities
//                for (Long current : trackIDs) {
//                    Music music = em.getReference(Music.class, current);
//                    if (music == null) {
//                        return null;
//                    }
//                    tracksInCart.add(music);
//                }
//                for (Long current : albumIDs) {
//                    Album album = em.getReference(Album.class, current);
//                    if (album == null) {
//                        return null;
//                    }
//                    albumInCart.add(album);
//                }
            }

            //Create the list of artist/band to be paid and the amount to be paid for each artist for each music track
            //and calculate the total amount to be paid
            for (Music music : tracksInCart) {
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
                secondaryReceiver.setPaymentType("DIGITALGOODS");
                receivers.add(secondaryReceiver);
            }

            //Daniel (total amount)
            Receiver primaryReceiver = new Receiver();
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
            payRequest.setCancelUrl("http://localhost:8080/DanielMusic-war/#!/payment-cancelled");//Return if payment cancelled
            payRequest.setReturnUrl("http://localhost:8080/DanielMusic-war/#!/Controller?PaymentID=" + payment.getId() + "&UUID=" + payment.getUUID());//Return after payment complete
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
            System.out.println("Open this link in browser: https://www.sandbox.paypal.com/webapps/adaptivepayment/flow/pay?paykey=" + payKey);
            if (payKey != null) {
                //TODO remove sandbox
                return "https://www.sandbox.paypal.com/webapps/adaptivepayment/flow/pay?paykey=" + payKey;
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
                //TODO add the payment to the user list of purchased music
                //Increase all the purchased count by +1
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
            Query q = em.createQuery("Select a from Account a where a.id=:accountID");
            q.setParameter("accountID", accountID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            account = (Account) q.getSingleResult();
           // account = em.getReference(Account.class, accountID);

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
        System.out.println("ClientManagementBean: removeItemFromShoppingCart() called");
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
                System.out.println("ClientManagementBean: removeItemFromShoppingCart(): Removing track from cart...");
                q = em.createQuery("Select m from Music m where m.id=:trackOrAlbumID");
                q.setParameter("trackOrAlbumID", trackOrAlbumID);
                q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                Music m = (Music) q.getSingleResult();
                result = cart.getListOfMusics().remove(m);
                em.flush();

                if (result) {
                    System.out.println("ClientManagementBean: removeItemFromShoppingCart(): Track has been removed from cart");
                    helper.setDescription("Track has been removed from cart successfully.");
                    helper.setResult(true);
                } else {
                    System.out.println("ClientManagementBean: removeItemFromShoppingCart(): Failed to remove track from cart");
                    helper.setDescription("Error while removing track from cart, please try again.");
                    helper.setResult(false);
                }
            } else {
                System.out.println("ClientManagementBean: removeItemFromShoppingCart(): Removing album from cart...");
                q = em.createQuery("Select a from Album a where a.id=:trackOrAlbumID");
                q.setParameter("trackOrAlbumID", trackOrAlbumID);
                q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                Album a = (Album) q.getSingleResult();
                result = cart.getListOfAlbums().remove(a);
                em.flush();

                if (result) {
                    System.out.println("ClientManagementBean: removeItemFromShoppingCart(): Album has been removed from cart");
                    helper.setDescription("A;bum has been removed from cart successfully.");
                    helper.setResult(true);
                } else {
                    System.out.println("ClientManagementBean: removeItemFromShoppingCart(): Failed to remove album from cart");
                    helper.setDescription("Error while removing album from cart, please try again.");
                    helper.setResult(false);
                }
            }
            return helper;
        } catch (Exception e) {
            System.out.println("ClientManagementBean: removeItemFromShoppingCart() exception occurred");
            helper.setDescription("An error occurred while removing item from shopping cart, please try again.");
            helper.setResult(false);
            return helper;
        }
    }

    @Override
    public ReturnHelper addItemToShoppingCart(Long accountID, Long trackOrAlbumID, Boolean isTrack) {
        System.out.println("ClientManagementBean: addItemToShoppingCart() called");
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
                System.out.println("addItemToShoppingCart: inside isTrack");
                q = em.createQuery("Select m from Music m where m.id=:trackOrAlbumID");
                q.setParameter("trackOrAlbumID", trackOrAlbumID);
                q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                music = (Music) q.getSingleResult();
                Set<Music> musicSet = cart.getListOfMusics();
                result = musicSet.add(music);
                cart.setListOfMusics(musicSet);
                em.merge(cart);
            } else {
                System.out.println("addItemToShoppingCart: inside isAlbum");

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
            System.out.println("ClientManagementBean: addItemToShoppingCart() successfully");

            helper.setDescription("Item has been added to cart successfully.");
            helper.setResult(true);
            return helper;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ClientManagementBean: addItemToShoppingCart() failed");
            helper.setDescription("Error occurred while adding item to cart.");
            helper.setResult(false);
            return helper;
        }
    }

    @Override
    public ReturnHelper clearShoppingCart(Long accountID) {
        System.out.println("ClientManagementBean: clearShoppingCart() called");
        ShoppingCart cart;
        ReturnHelper helper = new ReturnHelper();
        try {
            Query q = em.createQuery("Select sc from ShoppingCart sc where sc.account.id=:accountID");
            q.setParameter("accountID", accountID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            cart = (ShoppingCart) q.getSingleResult();

            cart.getListOfAlbums().removeAll(cart.getListOfAlbums());
            cart.getListOfMusics().removeAll(cart.getListOfMusics());
            System.out.println("ClientManagementBean: clearShoppingCart() successfully");

            helper.setDescription("Shopping cart has been cleared successfully.");
            helper.setResult(true);
            return helper;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ClientManagementBean: clearShoppingCart() failed");
            helper.setDescription("Failed to clear shopping cart please try again.");
            helper.setResult(false);
            return helper;
        }
    }

}
