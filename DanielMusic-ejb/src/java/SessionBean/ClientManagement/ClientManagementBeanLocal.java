package SessionBean.ClientManagement;

import EntityManager.ReturnHelper;
import EntityManager.ShoppingCart;
import java.util.List;
import javax.ejb.Local;

@Local
public interface ClientManagementBeanLocal {

    public String getPaymentLink(Long accountID, String nonMemberEmail, List<Long> trackIDs, List<Long> albumIDs);
    public ReturnHelper completePayment(Long paymentID, String UUID);
    public ShoppingCart getShoppingCart(Long accountID);
    public ReturnHelper removeItemFromShoppingCart(Long accountID, Long trackOrAlbumID, Boolean isTrack);
    public ReturnHelper addItemToShoppingCart(Long accountID, Long trackOrAlbumID, Boolean isTrack);
    public ReturnHelper clearShoppingCart(Long accountID);
    
    public Boolean testPayment(Double totalAmount, Double amount2, Double amount3);
}
