package SessionBean.CommonInfrastructure;

import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;

@WebService(serviceName = "SendGridEmailWS")
@Stateless()
public class SendGridEmailWS {
    @EJB
    private SendGridLocal ejbRef;// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Web Service Operation")

    @WebMethod(operationName = "sendEmail")
    public Boolean sendEmail(@WebParam(name = "destinationEmail") String destinationEmail, @WebParam(name = "senderEmail") String senderEmail, @WebParam(name = "subject") String subject, @WebParam(name = "message") String message) {
        return ejbRef.sendEmail(destinationEmail, senderEmail, subject, message);
    }
    
}
