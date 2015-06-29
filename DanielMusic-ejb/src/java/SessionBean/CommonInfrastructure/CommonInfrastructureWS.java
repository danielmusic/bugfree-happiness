/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean.CommonInfrastructure;

import EntityManager.ReturnHelper;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author -VeRyLuNaTiC
 */
@WebService(serviceName = "CommonInfrastructureWS")
@Stateless()
public class CommonInfrastructureWS {

    @EJB
    private CommonInfrastructureBeanLocal ejbRef;// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Web Service Operation")

    @WebMethod(operationName = "uploadFileToGoogleCloudStorage")
    public ReturnHelper uploadFileToGoogleCloudStorage(@WebParam(name = "remoteDestinationFile") String remoteDestinationFile, @WebParam(name = "localSourceFile") String localSourceFile, @WebParam(name = "isImage") Boolean isImage, @WebParam(name = "publiclyReadable") Boolean publiclyReadable) {
        return ejbRef.uploadFileToGoogleCloudStorage(remoteDestinationFile, localSourceFile, isImage, publiclyReadable);
    }

    @WebMethod(operationName = "deleteFileFromGoogleCloudStorage")
    public ReturnHelper deleteFileFromGoogleCloudStorage(@WebParam(name = "remoteDestinationFile") String remoteDestinationFile) {
        return ejbRef.deleteFileFromGoogleCloudStorage(remoteDestinationFile);
    }

    @WebMethod(operationName = "getMusicFileURLFromGoogleCloudStorage")
    public String getMusicFileURLFromGoogleCloudStorage(@WebParam(name = "filename") String filename,@WebParam(name = "expirationInSeconds") Long expirationInSeconds) {
        return ejbRef.getFileURLFromGoogleCloudStorage(filename,expirationInSeconds);
    }

    @WebMethod(operationName = "generateUUID")
    public String generateUUID() {
        return ejbRef.generateUUID();
    }

}
