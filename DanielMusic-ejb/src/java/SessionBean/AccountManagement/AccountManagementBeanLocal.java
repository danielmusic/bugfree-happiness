package SessionBean.AccountManagement;

import EntityManager.Account;
import EntityManager.ReturnHelper;
import java.util.List;
import javax.ejb.Local;

@Local
public interface AccountManagementBeanLocal {
    public ReturnHelper loginAccount(String email, String password);
    public Account getAccount(String email);
    
    public ReturnHelper disableAccount(Long accountID);
    public ReturnHelper enableAccount(Long accountID);
    
    public ReturnHelper registerAccount(String name, String email, String password, boolean isAdmin, boolean isArtist);  
    public boolean checkIfUsernameExists(String email);
    public String generatePasswordHash(String salt, String password);
    public String generatePasswordSalt();
    
    public ReturnHelper updateAccount(Long staffID, String newName);
    public ReturnHelper updateAccountPassword(Long staffID, String oldPassword, String newPassword);
    //public List<Account> listAllAccount();
}
