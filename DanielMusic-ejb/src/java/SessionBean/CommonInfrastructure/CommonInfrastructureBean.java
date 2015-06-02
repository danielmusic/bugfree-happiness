package SessionBean.CommonInfrastructure;

import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.sendgrid.SendGrid;
import java.io.ObjectOutputStream;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class CommonInfrastructureBean implements CommonInfrastructureBeanLocal {

    @PersistenceContext
    private EntityManager em;

    private final GcsService gcsService = GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());
    private final String bucketName = "divine-apogee-96116.appspot.com";

    @Override
    public Boolean sendEmail(String destinationEmail, String senderEmail, String subject, String message) {
        System.out.println("AccountManagementBean: sendEmail() called");
        try {
            SendGrid sendgrid = new SendGrid("sendgrid_api_key");
            SendGrid.Email email = new SendGrid.Email();
            email.addTo(destinationEmail);
            email.setFrom(senderEmail);
            email.setSubject(subject);
            email.setText(message);
            sendgrid.send(email);
            return true;
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: sendEmail() failed");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean uploadFileToGoogleCloudStorage(String filename, String pathToFile) {
        System.out.println("AccountManagementBean: uploadFileToGoogleCloudStorage() called");
        try {
            GcsFilename gcsFilename = new GcsFilename(bucketName, filename);
            GcsOutputChannel outputChannel = gcsService.createOrReplace(gcsFilename, GcsFileOptions.getDefaultInstance());
            @SuppressWarnings("resource")
            ObjectOutputStream oout = new ObjectOutputStream(Channels.newOutputStream(outputChannel));
            Path path = Paths.get(filename);
            byte[] file = Files.readAllBytes(path);
            oout.writeObject(file);
            oout.close();
            return true;
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: uploadFileToGoogleCloudStorage() failed");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Object getFileFromGoogleCloudStorage(String filename) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
