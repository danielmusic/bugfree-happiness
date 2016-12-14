package SessionBean.CommonInfrastructure;

import javax.ejb.Stateless;
import com.sendgrid.SendGrid;

@Stateless
public class SendGridEmail implements SendGridLocal {

    //SendGrid API Key
    private String SENDGRID_USER = "API@.sg";
    private String SENDGRID_PASSWORD = "";
    private String SENDGRID_API_KEY = "";

    @Override
    public Boolean sendEmail(String destinationEmail, String senderEmail, String subject, String message) {
        System.out.println("CommonInfrastructureBean: sendEmail() called");
        try {
            //SendGrid sendgrid = new SendGrid(SENDGRID_USER, SENDGRID_PASSWORD);
            SendGrid sendgrid = new SendGrid(SENDGRID_API_KEY);
            SendGrid.Email email = new SendGrid.Email();
            email.addTo(destinationEmail);
            email.setFrom(senderEmail);
            email.setFromName("sounds.sg");
            email.setSubject(subject);
            email.setHtml(message);
            SendGrid.Response response = sendgrid.send(email);
            if (!response.getStatus()) {
                System.out.println("CommonInfrastructureBean: sendEmail() failed");
                System.out.println(response.getMessage());
            }
            return response.getStatus();
        } catch (Exception ex) {
            System.out.println("CommonInfrastructureBean: sendEmail() failed");
            ex.printStackTrace();
            return false;
        }
    }
}
