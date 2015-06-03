/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean.CommonInfrastructure;

import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author -VeRyLuNaTiC
 */
@WebService(serviceName = "NewWebService2")
@Stateless()
public class CommonInfrastructureWS {
    @EJB
    private CommonInfrastructureBeanLocal ejbRef;// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Web Service Operation")

    @WebMethod(operationName = "sendEmail")
    public Boolean sendEmail(@WebParam(name = "destinationEmail") String destinationEmail, @WebParam(name = "senderEmail") String senderEmail, @WebParam(name = "subject") String subject, @WebParam(name = "message") String message) {
        return ejbRef.sendEmail(destinationEmail, senderEmail, subject, message);
    }

    @WebMethod(operationName = "uploadFileToGoogleCloudStorage")
    public Boolean uploadFileToGoogleCloudStorage(@WebParam(name = "remoteDestinationFile") String remoteDestinationFile, @WebParam(name = "localSourceFile") String localSourceFile, @WebParam(name = "isImage") Boolean isImage) {
        return ejbRef.uploadFileToGoogleCloudStorage(remoteDestinationFile, localSourceFile, isImage);
    }

    @WebMethod(operationName = "getMusicFileURLFromGoogleCloudStorage")
    public String getMusicFileURLFromGoogleCloudStorage(@WebParam(name = "filename") String filename) {
        return ejbRef.getMusicFileURLFromGoogleCloudStorage(filename);
    }

    @WebMethod(operationName = "generateUUID")
    public String generateUUID() {
        return ejbRef.generateUUID();
    }
    
}
