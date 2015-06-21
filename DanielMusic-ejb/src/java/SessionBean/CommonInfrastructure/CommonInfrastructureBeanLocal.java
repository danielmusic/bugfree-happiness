package SessionBean.CommonInfrastructure;

import EntityManager.ReturnHelper;
import javax.ejb.Local;

@Local
public interface CommonInfrastructureBeanLocal {
    public Boolean sendEmail(String destinationEmail, String senderEmail, String subject, String message);
    public ReturnHelper uploadFileToGoogleCloudStorage(String remoteDestinationFile, String localSourceFile, Boolean isImage);
    public ReturnHelper deleteFileFromGoogleCloudStorage(String remoteDestinationFile);
    public String getMusicFileURLFromGoogleCloudStorage(String filename); //filename: eg music/artistID/albumID/musicName.mp3
    public String generateUUID();
}
