package SessionBean.CommonInfrastructure;

import javax.ejb.Local;

@Local
public interface CommonInfrastructureBeanLocal {
    public Boolean sendEmail(String destinationEmail, String senderEmail, String subject, String message);
    public Boolean uploadFileToGoogleCloudStorage(String remoteDestinationFile, String localSourceFile, Boolean isImage);
    public Boolean deleteFileFromGoogleCloudStorage(String remoteDestinationFile);
    public String getMusicFileURLFromGoogleCloudStorage(String filename); //filename: eg music/artistID/albumID/musicName.mp3
    public String generateUUID();
}
