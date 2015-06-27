package SessionBean.CommonInfrastructure;

import javax.ejb.Local;

@Local
public interface SendGridLocal {
    public Boolean sendEmail(String destinationEmail, String senderEmail, String subject, String message);
}
