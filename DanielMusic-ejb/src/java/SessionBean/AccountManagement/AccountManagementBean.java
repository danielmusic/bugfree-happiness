package SessionBean.AccountManagement;

import EntityManager.Account;
import EntityManager.ReturnHelper;
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
    public ReturnHelper loginAccount(String username, String password) {
        System.out.println("AccountManagementBean: loginAccount() called");
        ReturnHelper result = new ReturnHelper();
        try {
            Query q = em.createQuery("SELECT a FROM Account a where a.username=:username");
            q.setParameter("username", username);
            Account account = (Account) q.getSingleResult();
            String passwordSalt = account.getPasswordSalt();
            String passwordHash = generatePasswordHash(passwordSalt, password);
            if (passwordHash.equals(account.getPasswordHash())) {
                if (account.getIsDisabled()) {
                    result.setResult(false);
                    result.setDescription("Unable to login, account is disabled.");
                    return result;
                }
                System.out.println("loginAccount(): Account with username:" + username + " logged in successfully.");
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
            System.out.println("loginAccount(): Login credentials provided were incorrect, no such username found.");
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
    public Account getAccount(String username) {
        System.out.println("AccountManagementBean: getAccount() called");
        try {
            Query q = em.createQuery("SELECT a FROM Account a where a.username=:username");
            q.setParameter("username", username);
            Account account = (Account) q.getSingleResult();
            return account;
        } catch (Exception ex) {
            System.out.println("getAccount(): Internal error");
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public ReturnHelper registerAccount(String name, String username, String password, boolean isAdmin, boolean isArtist) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public boolean checkIfUsernameExists(String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String generatePasswordHash(String salt, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String generatePasswordSalt() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ReturnHelper updateAccount(Long accountID, String newName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ReturnHelper updateAccountPassword(Long accountID, String oldPassword, String newPassword) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
