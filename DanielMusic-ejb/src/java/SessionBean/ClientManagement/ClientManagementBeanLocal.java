package SessionBean.ClientManagement;

import EntityManager.Album;
import EntityManager.Music;
import EntityManager.ReturnHelper;
import java.util.List;
import javax.ejb.Local;

@Local
public interface ClientManagementBeanLocal {
    
    public String getPaymentLink(Long accountID, String nonMemberEmail, List<Long> trackIDs, List<Long> albumIDs);
    public ReturnHelper completePayment(Long paymentID, String UUID);

}
