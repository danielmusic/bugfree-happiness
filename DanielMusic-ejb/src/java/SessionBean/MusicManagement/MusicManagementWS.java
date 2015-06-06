/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean.MusicManagement;

import EntityManager.Music;
import EntityManager.ReturnHelper;
import EntityManager.SearchHelper;
import java.io.File;
import java.util.List;
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
@WebService(serviceName = "MusicManagementWS")
@Stateless()
public class MusicManagementWS {
    @EJB
    private MusicManagementBeanLocal ejbRef;// Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Web Service Operation")

    @WebMethod(operationName = "encodeToMP3")
    public ReturnHelper encodeToMP3(@WebParam(name = "sourceFileName") File sourceFileName, @WebParam(name = "targetFileName") File targetFileName, @WebParam(name = "bitrate") int bitrate) {
        return ejbRef.encodeToMP3(sourceFileName, targetFileName, bitrate);
    }

    @WebMethod(operationName = "testAdaptivePayment")
    @Oneway
    public void testAdaptivePayment() {
        ejbRef.testAdaptivePayment();
    }

    @WebMethod(operationName = "generateDownloadLink")
    public ReturnHelper generateDownloadLink(@WebParam(name = "email") String email, @WebParam(name = "musicID") Long musicID) {
        return ejbRef.generateDownloadLink(email, musicID);
    }

    @WebMethod(operationName = "searchMusicByGenre")
    public List<Music> searchMusicByGenre(@WebParam(name = "genreID") Long genreID) {
        return ejbRef.searchMusicByGenre(genreID);
    }

    @WebMethod(operationName = "search")
    public SearchHelper search(@WebParam(name = "searchString") String searchString) {
        return ejbRef.search(searchString);
    }
    
}
