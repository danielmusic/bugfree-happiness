package SessionBean.CommonInfrastructure;

import EntityManager.ReturnHelper;
import javax.ejb.Local;

@Local
public interface CommonInfrastructureBeanLocal {
    public Boolean sendEmail(String destinationEmail, String senderEmail, String subject, String message);
    public Boolean uploadFileToGoogleCloudStorage(String filename);
    public Object getFileFromGoogleCloudStorage(String filename);
}
