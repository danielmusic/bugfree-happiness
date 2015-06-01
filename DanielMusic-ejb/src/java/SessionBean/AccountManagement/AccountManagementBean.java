package SessionBean.AccountManagement;

import EntityManager.Account;
import EntityManager.Admin;
import EntityManager.Artist;
import EntityManager.Member;
import EntityManager.ReturnHelper;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class AccountManagementBean implements AccountManagementBeanLocal {

    public AccountManagementBean() {
    }

    @PersistenceContext
    private EntityManager em;

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
                admin.setPasswordHash(passwordHash);
                admin.setPasswordSalt(passwordSalt);
                admin.setName(name);
                em.persist(admin);
            }else if (isArtist) {
                Artist artist = new Artist();
                artist.setEmail(email);
                artist.setPasswordHash(passwordHash);
                artist.setPasswordSalt(passwordSalt);
                artist.setName(name);
                em.persist(artist);
            } else {
                Member member = new Member();
                member.setEmail(email);
                member.setPasswordHash(passwordHash);
                member.setPasswordSalt(passwordSalt);
                member.setName(name);
                em.persist(member);
            }
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
        Query q = em.createQuery("SELECT s FROM Account s where s.id=:id");
        q.setParameter("id", accountID);
        try {
            Account account = (Account) q.getSingleResult();
            if (account.getIsDisabled() == true) {
                result.setResult(false);
                result.setDescription("Account is already disabled.");
            } else {
                account.setIsDisabled(true);
                em.merge(account);
                result.setResult(true);
                result.setDescription("Account disabled successfully.");
            }
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: enableAccount() failed");
            result.setResult(false);
            result.setDescription("Failed to disable account. Internal server error.");
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean checkIfEmailExists(String email) {
        System.out.println("AccountManagementBean: checkIfEmailExists() called");
        Query q = em.createQuery("SELECT a FROM Account a WHERE a.email:email");
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
    public ReturnHelper updateAccountProfile(Long accountID, String newName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ReturnHelper updateAccountPassword(Long accountID, String oldPassword, String newPassword) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
