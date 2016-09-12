package SessionBean.CommonInfrastructure;

import EntityManager.ReturnHelper;
import javax.ejb.Local;
import javax.servlet.http.Part;

@Local
public interface CommonInfrastructureBeanLocal {
    public ReturnHelper uploadFileToGoogleCloudStorage(String remoteDestinationFile, String localSourceFile, String downloadFilename, Boolean isImage, Boolean publiclyReadable);
    public ReturnHelper deleteFileFromGoogleCloudStorage(String remoteDestinationFile);
    public String getFileURLFromGoogleCloudStorage(String filename, Long expirationInSeconds, String downloadFilename); //filename: eg music/artistID/albumID/musicName.mp3
    public String generateUUID();
    
    public ReturnHelper checkIfImageFitsRequirement(String filename);
    public String getSubmittedFileName(Part part);
}
