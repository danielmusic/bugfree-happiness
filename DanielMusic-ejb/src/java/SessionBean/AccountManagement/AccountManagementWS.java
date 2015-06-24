/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean.AccountManagement;

import EntityManager.Account;
import EntityManager.ReturnHelper;
import java.util.Date;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author -VeRyLuNaTiC
 */
@WebService(serviceName = "AccountManagementWS")
@Stateless()
public class AccountManagementWS {

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
    public ReturnHelper registerAccount(@WebParam(name = "name") String name, @WebParam(name = "email") String email, @WebParam(name = "password") String password, @WebParam(name = "isAdmin") boolean isAdmin, @WebParam(name = "isArtist") boolean isArtist, @WebParam(name = "isBand") boolean isBand) {
        return ejbRef.registerAccount(name, email, password, isAdmin, isArtist, isBand);
    }

    @WebMethod(operationName = "checkIfEmailExists")
    public boolean checkIfEmailExists(@WebParam(name = "email") String email) {
        return ejbRef.checkIfEmailExists(email);
    }

    @WebMethod(operationName = "checkIfArtistNameExists")
    public boolean checkIfArtistNameExists(@WebParam(name = "name") String name) {
        return ejbRef.checkIfArtistNameExists(name);
    }

    @WebMethod(operationName = "checkIfBandNameExists")
    public boolean checkIfBandNameExists(@WebParam(name = "name") String name) {
        return ejbRef.checkIfBandNameExists(name);
    }

    @WebMethod(operationName = "checkIfArtistOrBandNameExists")
    public boolean checkIfArtistOrBandNameExists(@WebParam(name = "name") String name) {
        return ejbRef.checkIfArtistOrBandNameExists(name);
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

    @WebMethod(operationName = "updateMemberProfile")
    public ReturnHelper updateMemberProfile(@WebParam(name = "memberID") Long memberID, @WebParam(name = "newName") String newName) {
        return ejbRef.updateMemberProfile(memberID, newName);
    }

    @WebMethod(operationName = "updateArtistProfile")
    public ReturnHelper updateArtistProfile(@WebParam(name = "artistID") Long artistID, @WebParam(name = "genreID") Long genreID, @WebParam(name = "biography") String biography, @WebParam(name = "influences") String influences, @WebParam(name = "contactEamil") String contactEamil, @WebParam(name = "paypalEmail") String paypalEmail, @WebParam(name = "facebookURL") String facebookURL, @WebParam(name = "instagramURL") String instagramURL, @WebParam(name = "twitterURL") String twitterURL) {
        return ejbRef.updateArtistProfile(artistID, genreID, biography, influences, contactEamil, paypalEmail, facebookURL, instagramURL, twitterURL);
    }

    @WebMethod(operationName = "updateArtistName")
    public ReturnHelper updateArtistName(@WebParam(name = "artistID") Long artistID, @WebParam(name = "newName") String newName) {
        return ejbRef.updateArtistName(artistID, newName);
    }

    @WebMethod(operationName = "updateBandProfile")
    public ReturnHelper updateBandProfile(@WebParam(name = "bandID") Long bandID, @WebParam(name = "members") String members, @WebParam(name = "dateFormed") Date dateFormed, @WebParam(name = "genreID") Long genreID, @WebParam(name = "biography") String biography, @WebParam(name = "influences") String influences, @WebParam(name = "contactEamil") String contactEamil, @WebParam(name = "paypalEmail") String paypalEmail, @WebParam(name = "facebookURL") String facebookURL, @WebParam(name = "instagramURL") String instagramURL, @WebParam(name = "twitterURL") String twitterURL) {
        return ejbRef.updateBandProfile(bandID, members, dateFormed, genreID, biography, influences, contactEamil, paypalEmail, facebookURL, instagramURL, twitterURL);
    }

    @WebMethod(operationName = "updateBandName")
    public ReturnHelper updateBandName(@WebParam(name = "bandID") Long bandID, @WebParam(name = "newName") String newName) {
        return ejbRef.updateBandName(bandID, newName);
    }

    @WebMethod(operationName = "deleteAccountProfilePicture")
    public ReturnHelper deleteAccountProfilePicture(@WebParam(name = "accountID") Long accountID) {
        return ejbRef.deleteAccountProfilePicture(accountID);
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
