/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean.AccountManagement;

import EntityManager.Account;
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
@WebService(serviceName = "NewWebService1")
@Stateless()
public class NewWebService1 {
    @EJB
    private AccountManagementBeanLocal ejbRef;// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Web Service Operation")

    @WebMethod(operationName = "loginAccount")
    public ReturnHelper loginAccount(@WebParam(name = "email") String email, @WebParam(name = "password") String password) {
        return ejbRef.loginAccount(email, password);
    }

    @WebMethod(operationName = "getAccount")
    public Account getAccount(@WebParam(name = "email") String email) {
        return ejbRef.getAccount(email);
    }

    @WebMethod(operationName = "disableAccount")
    public ReturnHelper disableAccount(@WebParam(name = "accountID") Long accountID) {
        return ejbRef.disableAccount(accountID);
    }

    @WebMethod(operationName = "enableAccount")
    public ReturnHelper enableAccount(@WebParam(name = "accountID") Long accountID) {
        return ejbRef.enableAccount(accountID);
    }

    @WebMethod(operationName = "registerAccount")
    public ReturnHelper registerAccount(@WebParam(name = "name") String name, @WebParam(name = "email") String email, @WebParam(name = "password") String password, @WebParam(name = "isAdmin") boolean isAdmin, @WebParam(name = "isArtist") boolean isArtist) {
        return ejbRef.registerAccount(name, email, password, isAdmin, isArtist);
    }

    @WebMethod(operationName = "checkIfEmailExists")
    public boolean checkIfEmailExists(@WebParam(name = "email") String email) {
        return ejbRef.checkIfEmailExists(email);
    }

    @WebMethod(operationName = "generateAndSendVerificationEmail")
    public ReturnHelper generateAndSendVerificationEmail(@WebParam(name = "emailAddress") String emailAddress) {
        return ejbRef.generateAndSendVerificationEmail(emailAddress);
    }

    @WebMethod(operationName = "enterVerificationCode")
    public ReturnHelper enterVerificationCode(@WebParam(name = "emailAddress") String emailAddress, @WebParam(name = "verificationCode") String verificationCode) {
        return ejbRef.enterVerificationCode(emailAddress, verificationCode);
    }

    @WebMethod(operationName = "generatePasswordHash")
    public String generatePasswordHash(@WebParam(name = "salt") String salt, @WebParam(name = "password") String password) {
        return ejbRef.generatePasswordHash(salt, password);
    }

    @WebMethod(operationName = "generatePasswordSalt")
    public String generatePasswordSalt() {
        return ejbRef.generatePasswordSalt();
    }


    @WebMethod(operationName = "updateAccountPassword")
    public ReturnHelper updateAccountPassword(@WebParam(name = "accountID") Long accountID, @WebParam(name = "oldPassword") String oldPassword, @WebParam(name = "newPassword") String newPassword) {
        return ejbRef.updateAccountPassword(accountID, oldPassword, newPassword);
    }

    @WebMethod(operationName = "updateAccountEmail")
    public ReturnHelper updateAccountEmail(@WebParam(name = "accountID") Long accountID, @WebParam(name = "newEmail") String newEmail) {
        return ejbRef.updateAccountEmail(accountID, newEmail);
    }
    
}
