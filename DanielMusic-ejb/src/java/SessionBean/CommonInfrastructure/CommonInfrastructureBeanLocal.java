package SessionBean.CommonInfrastructure;

import EntityManager.ReturnHelper;
import javax.ejb.Local;

@Local
public interface CommonInfrastructureBeanLocal {
    public ReturnHelper sendEmail(String destinationEmail, String senderEmail, String subject, String message);
    public ReturnHelper uploadFileToGoogleCloudStorage(String filename, Object content[]);
    public Object getFileFromGoogleCloudStorage(String filename);
}
