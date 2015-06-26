package SessionBean.AccountManagement;

import EntityManager.Account;
import EntityManager.ReturnHelper;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.servlet.http.Part;

@Local
public interface AccountManagementBeanLocal {
    public ReturnHelper loginAccount(String email, String password);
    public Account getAccount(String email);
    public Account getAccount(Long accountID);
    
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
    
    public ReturnHelper updateMemberProfile(Long memberID, String newName, Part profilePicture);
    public ReturnHelper updateMemberProfilePicture(Long accountID, Part profilePicture);
    
    public ReturnHelper updateArtistProfile(Long artistID, Long genreID, String biography, String influences, String contactEamil, String paypalEmail, String facebookURL, String instagramURL, String twitterURL, String websiteURL, Part profilePicture);
    public ReturnHelper updateArtistName(Long artistID, String newName);
    public ReturnHelper updateArtistProfilePicture(Long artistID, Part profilePicture);
    
    public ReturnHelper updateBandProfile(Long bandID, String members, Date dateFormed, Long genreID, String biography, String influences, String contactEamil, String paypalEmail, String facebookURL, String instagramURL, String twitterURL, String websiteURL, Part profilePicture);
    public ReturnHelper updateBandName(Long bandID, String newName);
    public ReturnHelper updateBandProfilePicture(Long bandID, Part profilePicture);
    
    public ReturnHelper deleteAccountProfilePicture(Long accountID);
    public ReturnHelper updateAccountPassword(Long accountID, String oldPassword, String newPassword);
    public ReturnHelper updateAccountEmail(Long accountID, String newEmail);
    //public List<Account> listAllAccount();
    
}
