package SessionBean.AccountManagement;

import EntityManager.Account;
import EntityManager.Artist;
import EntityManager.ReturnHelper;
import java.util.List;
import javax.ejb.Local;
import javax.servlet.http.Part;

@Local
public interface AccountManagementBeanLocal {
    public ReturnHelper loginAccount(String email, String password);
    public Account getAccount(String email);
    
    public ReturnHelper disableAccount(Long accountID);
    public ReturnHelper enableAccount(Long accountID);
    
    public ReturnHelper registerAccount(String name, String email, String password, boolean isAdmin, boolean isArtist, boolean isBand);  
    public boolean checkIfEmailExists(String email);
    public boolean checkIfArtistNameExists(String name);
    public boolean checkIfBandNameExists(String name);
    public boolean checkIfArtistOrBandNameExists(String name);
    public ReturnHelper generateAndSendVerificationEmail(String emailAddress);
    public ReturnHelper enterVerificationCode(String emailAddress, String verificationCode);
    public String generatePasswordHash(String salt, String password);
    public String generatePasswordSalt();
    
    public ReturnHelper updateAccountProfile(Long accountID, String newName, Part profilePicture, String description);
    public ReturnHelper deleteAccountProfilePicture(Long accountID);
    public ReturnHelper updateAccountPassword(Long accountID, String oldPassword, String newPassword);
    public ReturnHelper updateAccountEmail(Long accountID, String newEmail);
    //public List<Account> listAllAccount();
    
}
