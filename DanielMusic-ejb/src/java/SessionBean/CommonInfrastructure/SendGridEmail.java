package SessionBean.CommonInfrastructure;

import javax.ejb.Stateless;
import com.sendgrid.*;

@Stateless
public class SendGridEmail implements SendGridLocal {

    //SendGrid API Key
    private static final String SENDGRID_API_USER = "";
    private static final String SENDGRID_API_PASSWORD = "";

    @Override
    public Boolean sendEmail(String destinationEmail, String senderEmail, String subject, String message) {
        System.out.println("CommonInfrastructureBean: sendEmail() called");
        try {
            SendGrid sendgrid = new SendGrid(SENDGRID_API_USER, SENDGRID_API_PASSWORD);
            SendGrid.Email email = new SendGrid.Email();
            email.addTo(destinationEmail);
            email.setFrom(senderEmail);
            email.setFromName("Sounds.sg");
            email.setSubject(subject);
            email.setText(message);
            SendGrid.Response respose = sendgrid.send(email);
            return true;
        } catch (Exception ex) {
            System.out.println("CommonInfrastructureBean: sendEmail() failed");
            ex.printStackTrace();
            return false;
        }
    }
}
