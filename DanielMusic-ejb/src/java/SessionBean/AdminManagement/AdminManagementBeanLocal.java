package SessionBean.AdminManagement;

import EntityManager.ReturnHelper;
import javax.ejb.Local;

@Local
public interface AdminManagementBeanLocal {
    
    public ReturnHelper approveArtist(Long artistID);
    public ReturnHelper rejectArtist(Long artistID);
}
