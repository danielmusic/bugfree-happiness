package SessionBean.ClientManagement;

import EntityManager.Account;
import EntityManager.Album;
import EntityManager.Music;
import EntityManager.Payment;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ClientManagementBean implements ClientManagementBeanLocal {

    @EJB
    private CommonInfrastructureBeanLocal cibl;

    @PersistenceContext(unitName = "DanielMusic-ejbPU")
    private EntityManager em;

    @Override
    public String getPaymentLink(Long accountID, String nonMemberEmail, List<Long> trackIDs, List<Long> albumIDs) {
        try {
            //If accountID is not null, is a registered account purchase. Get it's shopping cart content
            //If null, the other 3 arguments should be filled in
            //Calculate the total amount to be paid
            Account account = null;
            Double totalPaymentAmount = 0.0;
            if (accountID != null) {
                // If it's a logged in account transaction
                account = em.getReference(Account.class, accountID);
                if (account == null) {
                    return null;
                }
                // Get his shopping cart, update trackIDs and albumIDs and total price
                trackIDs = new ArrayList();
                albumIDs = new ArrayList();
                ShoppingCart shoppingCart = account.getShoppingCart();
                List<Music> tracksInCart = shoppingCart.getListOfMusics();
                List<Album> albumInCart = shoppingCart.getListOfAlbums();
                for (Music music : tracksInCart) {
                    trackIDs.add(music.getId());
                    totalPaymentAmount = totalPaymentAmount + music.getPrice();
                }
                for (Album album : albumInCart) {
                    albumIDs.add(album.getId());
                    totalPaymentAmount = totalPaymentAmount + album.getPrice();
                }
            } else { // if it's a non logged in user
                //Do nothing, just take the list of IDs from the arguments intead
            }

            //Create a payment record in database (without marking it as successful first)
            String UUID = cibl.generateUUID();
            Payment payment = new Payment(totalPaymentAmount, UUID);
            em.persist(payment);
            //Create PayPal request
            PayRequest payRequest = new PayRequest();
            List<Receiver> receivers = new ArrayList<Receiver>();
            //Artist (partial of the total)
            Receiver secondaryReceiver = new Receiver();
            secondaryReceiver.setAmount(0.50);
            secondaryReceiver.setEmail("daniel-artist@hotmail.com");
            receivers.add(secondaryReceiver);

            //Daniel (total amount)
            Receiver primaryReceiver = new Receiver();
            primaryReceiver.setAmount(totalPaymentAmount);
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ReturnHelper completePayment(Long paymentID, String UUID) {
        //Delete shopping cart
        //Controller will forward to 
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
