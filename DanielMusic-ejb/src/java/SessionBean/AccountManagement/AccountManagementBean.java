package SessionBean.AccountManagement;

import EntityManager.Account;
import EntityManager.Admin;
import EntityManager.Artist;
import EntityManager.Member;
import EntityManager.ReturnHelper;
import SessionBean.CommonInfrastructure.CommonInfrastructureBeanLocal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.Part;

@Stateless
public class AccountManagementBean implements AccountManagementBeanLocal {

    public AccountManagementBean() {
    }

    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private CommonInfrastructureBeanLocal cibl;

    @Override
    public ReturnHelper loginAccount(String email, String password) {
        System.out.println("AccountManagementBean: loginAccount() called");
        ReturnHelper result = new ReturnHelper();
        try {
            Query q = em.createQuery("SELECT a FROM Account a where a.email=:email");
            q.setParameter("email", email);
            Account account = (Account) q.getSingleResult();
            String passwordSalt = account.getPasswordSalt();
            String passwordHash = generatePasswordHash(passwordSalt, password);
            if (passwordHash.equals(account.getPasswordHash())) {
                if (account.getIsDisabled()) {
                    result.setResult(false);
                    result.setDescription("Unable to login, account is disabled.");
                    return result;
                }
                System.out.println("loginAccount(): Account with email:" + email + " logged in successfully.");
                em.detach(account);
                account.setPasswordHash(null);
                account.setPasswordSalt(null);
                result.setResult(true);
                result.setDescription("Login successful.");
                return result;
            } else {
                System.out.println("loginAccount(): Login credentials provided were incorrect, password wrong.");
                result.setResult(false);
                result.setDescription("Login credentials provided incorrect.");
                return result;
            }
        } catch (NoResultException ex) {//cannot find account with that email
            System.out.println("loginAccount(): Login credentials provided were incorrect, no such email found.");
            result.setResult(false);
            result.setDescription("Login credentials provided incorrect.");
            return result;
        } catch (Exception ex) {
            System.out.println("loginAccount(): Internal error");
            ex.printStackTrace();
            result.setResult(false);
            result.setDescription("Unable to login, internal server error.");
            return result;
        }
    }

    @Override
    public Account getAccount(String email) {
        System.out.println("AccountManagementBean: getAccount() called");
        try {
            Query q = em.createQuery("SELECT a FROM Account a where a.email=:email");
            q.setParameter("email", email);
            Account account = (Account) q.getSingleResult();
            if (account instanceof Member) {
                q = em.createQuery("SELECT a FROM Member m where m.email=:email");
                q.setParameter("email", email);
                Member member = (Member) q.getSingleResult();
                return member;
            } else if (account instanceof Artist) {
                q = em.createQuery("SELECT a FROM Artist a where a.email=:email");
                q.setParameter("email", email);
                Artist artist = (Artist) q.getSingleResult();
                return artist;
            } else if (account instanceof Admin) {
                q = em.createQuery("SELECT a FROM Admin a where a.email=:email");
                q.setParameter("email", email);
                Admin admin = (Admin) q.getSingleResult();
                return admin;
            }
            Member member = new Member();
            return member;
        } catch (Exception ex) {
            System.out.println("getAccount(): Internal error");
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public ReturnHelper registerAccount(String name, String email, String password, boolean isAdmin, boolean isArtist) {
        System.out.println("AccountManagementBean: registerAccount() called");
        ReturnHelper result = new ReturnHelper();
        try {
            if (checkIfEmailExists(email)) {
                result.setResult(false);
                result.setDescription("Unable to register, email already in use.");
                return result;
            }
            String passwordSalt = generatePasswordSalt();
            String passwordHash = generatePasswordHash(passwordSalt, password);
            if (isAdmin) {
                Admin admin = new Admin();
                admin.setEmail(email);
                admin.setNewEmail(email);
                admin.setPasswordHash(passwordHash);
                admin.setPasswordSalt(passwordSalt);
                admin.setName(name);
                em.persist(admin);
                Query q = em.createQuery("SELECT a FROM Admin a where a.email=:email");
                q.setParameter("email", email);
                admin = (Admin) q.getSingleResult();
                result.setID(admin.getId());
            } else if (isArtist) {
                Artist artist = new Artist();
                artist.setEmail(email);
                artist.setNewEmail(email);
                artist.setPasswordHash(passwordHash);
                artist.setPasswordSalt(passwordSalt);
                artist.setName(name);
                em.persist(artist);
                Query q = em.createQuery("SELECT a FROM Artist a where a.email=:email");
                q.setParameter("email", email);
                artist = (Artist) q.getSingleResult();
                result.setID(artist.getId());
            } else {
                Member member = new Member();
                member.setEmail(email);
                member.setNewEmail(email);
                member.setPasswordHash(passwordHash);
                member.setPasswordSalt(passwordSalt);
                member.setName(name);
                em.persist(member);
                Query q = em.createQuery("SELECT a FROM Member a where a.email=:email");
                q.setParameter("email", email);
                member = (Member) q.getSingleResult();
                result.setID(member.getId());
            }
            generateAndSendVerificationEmail(email);
            result.setResult(true);
            result.setDescription("Account registered successfully.");
            return result;
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: registerAccount() failed");
            ex.printStackTrace();
            result.setResult(false);
            result.setDescription("Failed to register account due to internal server error.");
            return result;
        }
    }

    @Override
    public ReturnHelper enableAccount(Long accountID) {
        System.out.println("AccountManagementBean: enableAccount() called");
        ReturnHelper result = new ReturnHelper();
        Query q = em.createQuery("SELECT s FROM Account s where s.id:id");
        q.setParameter("id", accountID);
        try {
            Account account = (Account) q.getSingleResult();
            if (account.getIsDisabled() == false) {
                result.setResult(false);
                result.setDescription("Account is not disabled.");
            } else {
                account.setIsDisabled(false);
                em.merge(account);
                result.setResult(true);
                result.setDescription("Account enabled successfully.");
            }
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: enableAccount() failed");
            result.setResult(false);
            result.setDescription("Failed to enable account. Internal server error.");
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public ReturnHelper disableAccount(Long accountID) {
        System.out.println("AccountManagementBean: disableAccount() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        Query q = em.createQuery("SELECT s FROM Account s where s.id=:id");
        q.setParameter("id", accountID);
        try {
            Account account = (Account) q.getSingleResult();
            if (account.getIsDisabled() == true) {
                result.setDescription("Account is already disabled.");
            } else {
                account.setIsDisabled(true);
                em.merge(account);
                result.setResult(true);
                result.setDescription("Account disabled successfully.");
            }
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: enableAccount() failed");
            result.setDescription("Failed to disable account. Internal server error.");
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean checkIfEmailExists(String email) {
        System.out.println("AccountManagementBean: checkIfEmailExists() called");
        Query q = em.createQuery("SELECT a FROM Account a WHERE a.email=:email");
        q.setParameter("email", email);
        try {
            Account account = (Account) q.getSingleResult();
        } catch (NoResultException ex) {
            return false;
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: checkIfEmailExists() failed");
            ex.printStackTrace();
        }
        return true;
    }

    @Override
    public ReturnHelper generateAndSendVerificationEmail(String emailAddress) {
        System.out.println("AccountManagementBean: sendVerificationEmail() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            //Check if the account exists & if there is any unverified email tag tied to the account, if not 
            String unauthorizedMsg = "There is no account registered with this email address or the account email has already been verified.";
            if (!checkIfEmailExists(emailAddress)) {
                result.setDescription(unauthorizedMsg);
                return result;
            }
            Query q = em.createQuery("SELECT a FROM Account a WHERE a.email=:email");
            q.setParameter("email", emailAddress);
            Account account = (Account) q.getSingleResult();
            if (account.getNewEmailIsVerified()) {
                result.setDescription(unauthorizedMsg);
                return result;
            }
            //Generate the verification code & store it into DB
            Random r = new Random();
            int verificationCode = r.nextInt(9999);
            account.setVerificationCode(verificationCode + "");
            em.merge(account);
            //Send the verification code
            String verificationInstructions = "Verification instruction";
            Boolean emailSent = cibl.sendEmail(account.getNewEmail(), "no-reply@example.com", "Daniel Music Account Verification", verificationInstructions);
            if (emailSent) {
                result.setResult(true);
                result.setDescription("Verification email sent successfully, you should receieve the email in your email inbox (or spam folder) within the next 5 minutes.");
            } else {
                result.setDescription("Unable to send verificaiton email due to an internal serverr error. Please try again later.");
            }
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: sendVerificationEmail() failed");
            ex.printStackTrace();
            result.setDescription("Unable to send verification email because of an internal server error, please try again later.");
        }
        return result;
    }

    @Override
    public ReturnHelper enterVerificationCode(String emailAddress, String verificationCode) {
        System.out.println("AccountManagementBean: enterVerificationCode() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            //Check if the account exists & if there is any unverified email tag tied to the account, if not 
            String unauthorizedMsg = "There is no account registered with this email address or the account email has already been verified.";
            if (!checkIfEmailExists(emailAddress)) {
                result.setDescription(unauthorizedMsg);
                return result;
            }
            Query q = em.createQuery("SELECT a FROM Account a WHERE a.email=:email");
            q.setParameter("email", emailAddress);
            Account account = (Account) q.getSingleResult();
            if (account.getNewEmailIsVerified()) {
                result.setDescription(unauthorizedMsg);
                return result;
            }
            //Retrieve the verification code, compare and update account
            if (account.getVerificationCode().equalsIgnoreCase(verificationCode)) {
                account.setEmail(account.getNewEmail());
                account.setEmailIsVerified(true);
                account.setNewEmailIsVerified(true);
                account.setNewEmail("");
                em.merge(account);
                result.setResult(true);
                result.setDescription("Verification email sent successfully, you should receieve the email in your email inbox (or spam folder) within the next 5 minutes.");
            } else {
                result.setDescription("Invalid verification code, please try again.");
            }
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: enterVerificationCode() failed");
            ex.printStackTrace();
            result.setDescription("Unable to verify code because of an internal server error, please try again later.");
        }
        return result;
    }

    @Override
    public String generatePasswordHash(String salt, String password) {
        String passwordHash = null;
        try {
            password = salt + password;
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            passwordHash = sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("AccountManagementBean: generatePasswordHash() failed");
            ex.printStackTrace();
        }
        return passwordHash;
    }

    @Override
    public String generatePasswordSalt() {
        byte[] salt = new byte[16];
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.nextBytes(salt);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("AccountManagementBean: generatePasswordSalt() failed");
            ex.printStackTrace();
        }
        return Arrays.toString(salt);
    }

    @Override
    public ReturnHelper updateAccountProfile(Long accountID, String newName, Part profilePicture, String description) {
        System.out.println("AccountManagementBean: updateAccountProfile() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        Query q = em.createQuery("SELECT s FROM Account s WHERE s.id=:id");
        q.setParameter("id", accountID);
        try {
            Account account = (Account) q.getSingleResult();
            if (newName != null && !newName.equals("")) {
                account.setName(newName);
            }
            if (profilePicture != null) {

            }
            if (description != null && !description.equals("")) {
                account.setDescription(description);
            }
            em.merge(account);
            result.setResult(true);
            result.setDescription("Account updated successfully.");
        } catch (NoResultException ex) {
            result.setDescription("Unable to find account with the provided ID.");
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: updateStaffName() failed");
            result.setDescription("Unable to update account's name, internal server error.");
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public ReturnHelper updateAccountPassword(Long accountID, String oldPassword, String newPassword) {
        System.out.println("AccountManagementBean: updateAccountPassword() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        Query q = em.createQuery("SELECT a FROM Account a WHERE a.id=:id");
        q.setParameter("id", accountID);
        try {
            Account account = (Account) q.getSingleResult();
            if (!generatePasswordHash(account.getPasswordSalt(), oldPassword).equals(account.getPasswordHash())) {
                result.setDescription("Old password provided is invalid, password not updated.");
            } else {
                account.setPasswordSalt(generatePasswordSalt());
                account.setPasswordHash(generatePasswordHash(account.getPasswordSalt(), newPassword));
                em.merge(account);
                result.setResult(true);
                result.setDescription("Password updated successfully.");
            }
        } catch (NoResultException ex) {
            result.setDescription("Unable to find account with the provided ID.");
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: updateAccountPassword() failed");
            result.setDescription("Unable to update account's password due to internal server error. Please try again later.");
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public ReturnHelper updateAccountEmail(Long accountID, String newEmail) {
        System.out.println("AccountManagementBean: updateAccountEmail() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        Query q = em.createQuery("SELECT s FROM Account s WHERE s.id=:id");
        q.setParameter("id", accountID);
        try {
            Account account = (Account) q.getSingleResult();
            account.setNewEmail(newEmail);
            account.setNewEmailIsVerified(false);
            em.merge(account);
            ReturnHelper verificationCodeResult = generateAndSendVerificationEmail(newEmail);
            if (verificationCodeResult.getResult()) {
                result.setResult(true);
                result.setDescription("Account email verification sent successfully successfully.");
            } else {
                result.setDescription(verificationCodeResult.getDescription());
            }
        } catch (NoResultException ex) {
            result.setDescription("Unable to find account with the provided ID.");
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: updateAccountEmail() failed");
            result.setDescription("Unable to update account's email, internal server error.");
            ex.printStackTrace();
        }
        return result;
    }
}
