package SessionBean.CommonInfrastructure;

import com.sendgrid.SendGrid;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class CommonInfrastructureBean implements CommonInfrastructureBeanLocal {

    @PersistenceContext
    private EntityManager em;

    private String bucketName = "divine-apogee-96116.appspot.com";

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
    public Boolean uploadFileToGoogleCloudStorage(String remoteDestinationFile, String localSourceFile) {
        System.out.println("AccountManagementBean: uploadFileToGoogleCloudStorage() called");
        try {
            return true;
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: uploadFileToGoogleCloudStorage() failed");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public String getMusicFileURLFromGoogleCloudStorage(String filename) {
        return "Not supported yet.";
    }

}
