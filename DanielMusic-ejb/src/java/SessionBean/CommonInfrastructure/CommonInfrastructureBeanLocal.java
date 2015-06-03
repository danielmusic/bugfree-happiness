package SessionBean.CommonInfrastructure;

import javax.ejb.Local;

@Local
public interface CommonInfrastructureBeanLocal {

    public Boolean sendEmail(String destinationEmail, String senderEmail, String subject, String message);

    public Boolean uploadFileToGoogleCloudStorage(String remoteDestinationFile, String localSourceFile);

    public String getMusicFileURLFromGoogleCloudStorage(String filename);

    public String generateUUID();
}
