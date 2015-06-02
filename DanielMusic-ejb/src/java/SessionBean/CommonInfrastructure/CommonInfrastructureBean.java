package SessionBean.CommonInfrastructure;

import EntityManager.ReturnHelper;
import com.google.api.services.storage.Storage.Channels;
import com.sendgrid.SendGrid;
import java.io.ObjectOutputStream;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class CommonInfrastructureBean implements CommonInfrastructureBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public ReturnHelper sendEmail(String destinationEmail, String senderEmail, String subject, String message) {
        System.out.println("AccountManagementBean: sendEmail() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            SendGrid sendgrid = new SendGrid("sendgrid_api_key");
            SendGrid.Email email = new SendGrid.Email();
            email.addTo(destinationEmail);
            email.setFrom(senderEmail);
            email.setSubject(subject);
            email.setText(message);
            sendgrid.send(email);
            result.setResult(true);
            result.setDescription("Email sent successfully.");
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: sendEmail() failed");
            ex.printStackTrace();
            result.setDescription("Unable to send email because of an internal server error.");
        }
        return result;
    }

    @Override
    public ReturnHelper uploadFileToGoogleCloudStorage(String filename, Object[] content) {
        ReturnHelper result = new ReturnHelper();
//        @SuppressWarnings("resource")
//        GcsOutputChannel outputChannel = gcsService.createOrReplace(fileName, GcsFileOptions.getDefaultInstance());
//        outputChannel.write(ByteBuffer.wrap(content));
//        outputChannel.close();
        GcsOutputChannel outputChannel = gcsService.createOrReplace(fileName, GcsFileOptions.getDefaultInstance());
        @SuppressWarnings("resource")
        ObjectOutputStream oout = new ObjectOutputStream(Channels.newOutputStream(outputChannel));
        oout.writeObject(content);
        oout.close();
    }

    @Override
    public Object getFileFromGoogleCloudStorage(String filename) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
