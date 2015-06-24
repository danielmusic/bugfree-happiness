package EntityManager;

import SessionBean.AccountManagement.AccountManagementBeanLocal;
import SessionBean.CommonInfrastructure.CommonInfrastructureBeanLocal;
import java.io.File;
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
    
    @EJB
    private CommonInfrastructureBeanLocal cibl;

    @PostConstruct
    private void startup() {
        try {
            // =========== DO NOT DISABLE THIS START ============
            File theDir = new File("temp");
            // if the directory does not exist, create it
            if (!theDir.exists()) {
                System.out.print("Initiating directories...");
                boolean result = false;
                try {
                    theDir.mkdir();
                    result = true;
                } catch (SecurityException se) {
                    System.out.println("failed");
                    System.out.println("!!!!!!!!!!!!!!!!!!");
                    System.out.println("!!!!!!!!!!!!!!!!!!");        
                    System.out.println("WARNING: FAILED TO INIT DIRECTORIES. File upload functions will not work correctly.");
                    System.out.println("!!!!!!!!!!!!!!!!!!");
                    System.out.println("!!!!!!!!!!!!!!!!!!");
                    se.printStackTrace();
                }
                if (result) {
                    System.out.println("done");
                }
            } else {
                System.out.println("Skipping init of directories, already initated.");
            }
            //TODO init GCS authorization
            System.out.print("Initiating Google Cloud Storage authorization...");
            File file = new File("GCS Test File");
            file.createNewFile();
            cibl.uploadFileToGoogleCloudStorage("/temp/GCS Test File", "GCS Test File", false, false);
            file.delete();
            // =========== DO NOT DISABLE THIS END   ============
            Query q = em.createQuery("SELECT s FROM Account s where s.email=:email");
            q.setParameter("email", "a@a.a");
            List<Account> accounts = q.getResultList();
            // Don't insert anything if database appears to be initiated.
            if (accounts != null && accounts.size() > 0) {
                System.out.println("Skipping init of database, already initated.");
            } else {
                System.out.println("Initiating sample database records...");
                ReturnHelper result;
                result = ambl.registerAccount("Admin", "admin@a.a", "admin", true, false, false);
                Account account = ambl.getAccount("admin@a.a");
                account.setEmailIsVerified(true);
                account.setNewEmailIsVerified(true);
                account.setNewEmail("");
                em.merge(account);
                ambl.registerAccount("Artist", "artist@a.a", "artist", false, true, false);
                account = ambl.getAccount("artist@a.a");
                account.setEmailIsVerified(true);
                account.setNewEmailIsVerified(true);
                account.setNewEmail("");
                em.merge(account);
                account.setEmailIsVerified(true);
                account.setNewEmailIsVerified(true);
                em.merge(account);
                ambl.registerAccount("Member", "member@a.a", "member", false, false, false);
                account = ambl.getAccount("member@a.a");
                account.setEmailIsVerified(true);
                account.setNewEmailIsVerified(true);
                account.setNewEmail("");
                em.merge(account);
                ambl.registerAccount("Member Unverified Email", "member2@a.a", "member", false, false, false);
            }
        } catch (Exception ex) {
            System.out.println("Error initating database");
            ex.printStackTrace();
        }
    }
}
