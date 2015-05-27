/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean.MusicManagement;

import EntityManager.ReturnHelper;
import java.io.File;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author -VeRyLuNaTiC
 */
@WebService(serviceName = "NewWebService")
@Stateless()
public class NewWebService {
    @EJB
    private MusicManagementBeanLocal ejbRef;// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Web Service Operation")

    @WebMethod(operationName = "encodeToMP3")
    public ReturnHelper encodeToMP3(@WebParam(name = "sourceFileName") File sourceFileName, @WebParam(name = "targetFileName") File targetFileName) {
        return ejbRef.encodeToMP3(sourceFileName, targetFileName);
    }

    @WebMethod(operationName = "testAdaptivePayment")
    @Oneway
    public void testAdaptivePayment() {
        ejbRef.testAdaptivePayment();
    }
    
}
