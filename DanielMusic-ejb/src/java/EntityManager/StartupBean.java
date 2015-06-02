package EntityManager;

import SessionBean.AccountManagement.AccountManagementBeanLocal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Singleton
@Startup

public class StartupBean {

    @PersistenceContext(unitName = "DanielMusic-ejbPU")
    private EntityManager em;

    @EJB
    private AccountManagementBeanLocal ambl;

    public void persist(Object object) {
        em.persist(object);
    }

    @PostConstruct
    private void startup() {
        try {
            Query q = em.createQuery("SELECT s FROM Account s where s.email=:email");
            q.setParameter("email", "a@a.a");
            List<Account> accounts = q.getResultList();
            // Don't insert anything if database appears to be initiated.
            if (accounts != null && accounts.size() > 0) {
                System.out.println("Skipping init of database, already initated.");
                return;
            }
            ReturnHelper result;
            result = ambl.registerAccount("Admin", "admin@a.a", "admin", true, false);
            Account account = ambl.getAccount("admin@a.a");
            account.setEmailIsVerified(true);
            account.setNewEmailIsVerified(true);
            account.setNewEmail("admin@a.a");
            em.merge(account);
            ambl.registerAccount("Artist", "artist@a.a", "artist", true, false);
            account = ambl.getAccount("artist@a.a");
            account.setEmailIsVerified(true);
            account.setNewEmailIsVerified(true);
            account.setNewEmail("");
            em.merge(account);
            account.setEmailIsVerified(true);
            account.setNewEmailIsVerified(true);
            em.merge(account);
            ambl.registerAccount("Member", "member@a.a", "member", false, false);
            account = ambl.getAccount("member@a.a");
            account.setEmailIsVerified(true);
            account.setNewEmailIsVerified(true);
            account.setNewEmail("");
            em.merge(account);
            ambl.registerAccount("Member Unverified Email", "member2@a.a", "member", false, false);
        } catch (Exception ex) {
            System.out.println("Error initating database");
            ex.printStackTrace();
        }
    }
}
