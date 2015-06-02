package SessionBean.MusicManagement;

import EntityManager.Music;
import EntityManager.ReturnHelper;
import EntityManager.SearchHelper;
import java.io.File;
import java.util.List;
import javax.ejb.Local;

@Local
public interface MusicManagementBeanLocal {

    public ReturnHelper encodeToMP3(File sourceFileName, File targetFileName);

    public void testAdaptivePayment();

    public String generateDownloadLink(Long accountID, Long musicID);

    public List<Music> searchMusicByGenre(Long genreID);
//
//    public List<Music> searchMusicByArtist(String artistName);
//
//    public List<Music> searchMusic(String searchString);

    public SearchHelper search(String searchString);

}
