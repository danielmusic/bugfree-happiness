/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean.ClientManagement;

import EntityManager.ReturnHelper;
import EntityManager.ShoppingCart;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author -VeRyLuNaTiC
 */
@WebService(serviceName = "ClientManagementBeanWS")
@Stateless()
public class ClientManagementBeanWS {
    @EJB
    private ClientManagementBeanLocal ejbRef;// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Web Service Operation")

    @WebMethod(operationName = "getPaymentLink")
    public String getPaymentLink(@WebParam(name = "accountID") Long accountID, @WebParam(name = "nonMemberEmail") String nonMemberEmail, @WebParam(name = "trackIDs") List<Long> trackIDs, @WebParam(name = "albumIDs") List<Long> albumIDs) {
        return ejbRef.getPaymentLink(accountID, nonMemberEmail, trackIDs, albumIDs);
    }

    @WebMethod(operationName = "completePayment")
    public ReturnHelper completePayment(@WebParam(name = "paymentID") Long paymentID, @WebParam(name = "UUID") String UUID) {
        return ejbRef.completePayment(paymentID, UUID);
    }

    @WebMethod(operationName = "getShoppingCart")
    public ShoppingCart getShoppingCart(@WebParam(name = "accountID") Long accountID) {
        return ejbRef.getShoppingCart(accountID);
    }

    @WebMethod(operationName = "removeItemFromShoppingCart")
    public ReturnHelper removeItemFromShoppingCart(@WebParam(name = "accountID") Long accountID, @WebParam(name = "trackOrAlbumID") Long trackOrAlbumID, @WebParam(name = "isTrack") Boolean isTrack) {
        return ejbRef.removeItemFromShoppingCart(accountID, trackOrAlbumID, isTrack);
    }

    @WebMethod(operationName = "addItemToShoppingCart")
    public ReturnHelper addItemToShoppingCart(@WebParam(name = "accountID") Long accountID, @WebParam(name = "trackOrAlbumID") Long trackOrAlbumID, @WebParam(name = "isTrack") Boolean isTrack) {
        return ejbRef.addItemToShoppingCart(accountID, trackOrAlbumID, isTrack);
    }

    @WebMethod(operationName = "clearShoppingCart")
    public ReturnHelper clearShoppingCart(@WebParam(name = "accountID") Long accountID) {
        return ejbRef.clearShoppingCart(accountID);
    }

    @WebMethod(operationName = "testPayment")
    public Boolean testPayment(@WebParam(name = "totalAmount") Double totalAmount, @WebParam(name = "amount2") Double amount2, @WebParam(name = "amount3") Double amount3) {
        return ejbRef.testPayment(totalAmount, amount2, amount3);
    }
    
}
