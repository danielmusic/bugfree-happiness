package EntityManager;

import SessionBean.AccountManagement.AccountManagementBeanLocal;
import SessionBean.AdminManagement.AdminManagementBeanLocal;
import SessionBean.CommonInfrastructure.CommonInfrastructureBeanLocal;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private static final Logger log = Logger.getLogger(StartupBean.class.getName());

    @PersistenceContext(unitName = "DanielMusic-ejbPU")
    private EntityManager em;

    @EJB
    private AccountManagementBeanLocal accountManagementBeanLocal;

    @EJB
    private CommonInfrastructureBeanLocal commonInfrastructureBeanLocal;

    @EJB
    private AdminManagementBeanLocal adminManagementBeanLocal;

    @PostConstruct
    private void startup() {
        try {
            // =========== DO NOT DISABLE THIS START ============
            File theDir = new File("temp");
            // if the directory does not exist, create it
            if (!theDir.exists()) {
                log.info("Initiating directories...");
                boolean result = false;
                try {
                    theDir.mkdir();
                    result = true;
                } catch (SecurityException se) {
                    log.log(Level.SEVERE, "WARNING: FAILED TO INIT DIRECTORIES. File upload functions will not work correctly.");
                    log.log(Level.SEVERE, se.getMessage());
                }
                if (result) {
                    log.info("done");
                }
            } else {
                log.info("Skipping init of directories, already initated.");
            }
            //init GCS authorization
            System.out.print("Initiating Google Cloud Storage authorization...");
            File file = new File("GCS Test File");
            file.createNewFile();
            commonInfrastructureBeanLocal.uploadFileToGoogleCloudStorage("temp/GCS Test File", "GCS Test File", false, false);
            file.delete();
            // =========== DO NOT DISABLE THIS END   ============
            Query q = em.createQuery("SELECT s FROM Account s");
            List<Account> accounts = q.getResultList();
            // Don't insert anything if database appears to be initiated.
            if (accounts != null && accounts.size() > 0) {
                log.info("Skipping init of database, already initated.");
            } else {
                log.info("Initiating sample database records...");
                ReturnHelper result;
                result = accountManagementBeanLocal.registerAccount("Admin", "admin@sounds.sg", "admin", true, false, false);
                Account account = accountManagementBeanLocal.getAccount("admin@sounds.sg");
                account.setEmailIsVerified(true);
                account.setNewEmailIsVerified(true);
                account.setNewEmail("");
                em.merge(account);
                Artist artist = null;
                accountManagementBeanLocal.registerAccount("Artist", "artist@sounds.sg", "artist", false, true, false);
                account = accountManagementBeanLocal.getAccount("artist@sounds.sg");
                account.setEmailIsVerified(true);
                account.setNewEmailIsVerified(true);
                account.setNewEmail("");
                em.merge(account);
                artist = (Artist) account;
                em.merge(artist);
                accountManagementBeanLocal.registerAccount("Artist 2", "artist2@sounds.sg", "artist", false, true, false);
                account = accountManagementBeanLocal.getAccount("artist2@sounds.sg");
                account.setEmailIsVerified(true);
                account.setNewEmailIsVerified(true);
                account.setNewEmail("");
                em.merge(account);
                artist = (Artist) account;
                artist.setIsApproved(1);
                em.merge(artist);
                accountManagementBeanLocal.registerAccount("Band", "band@sounds.sg", "band", false, false, true);
                account = accountManagementBeanLocal.getAccount("band@sounds.sg");
                account.setEmailIsVerified(true);
                account.setNewEmailIsVerified(true);
                account.setNewEmail("");
                em.merge(account);
                accountManagementBeanLocal.registerAccount("Member", "member@sounds.sg", "member", false, false, false);
                account = accountManagementBeanLocal.getAccount("member@sounds.sg");
                account.setEmailIsVerified(true);
                account.setNewEmailIsVerified(true);
                account.setNewEmail("");
                em.merge(account);
                accountManagementBeanLocal.registerAccount("Member Unverified Email", "member2@sounds.sg", "member", false, false, false);
                adminManagementBeanLocal.createGenre("Rock");
                adminManagementBeanLocal.createGenre("Electronic");
                adminManagementBeanLocal.createGenre("Jazz");
            }
        } catch (Exception ex) {
            log.log(Level.SEVERE, "Error initating database");
            log.log(Level.SEVERE, ex.getMessage());
        }
    }
}
