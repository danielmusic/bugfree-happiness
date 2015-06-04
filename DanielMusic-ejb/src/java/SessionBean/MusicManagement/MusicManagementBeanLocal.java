package SessionBean.MusicManagement;

import EntityManager.Music;
import EntityManager.ReturnHelper;
import EntityManager.SearchHelper;
import java.io.File;
import java.util.List;
import javax.ejb.Local;

@Local
public interface MusicManagementBeanLocal {

    public ReturnHelper encodeToMP3(File sourceFileName, File targetFileName); //convert all file formats to mp3. so far tested wav file only

    public void testAdaptivePayment();

    public String generateDownloadLink(String email, Long musicID);

    public List<Music> searchMusicByGenre(Long genreID);
//
//    public List<Music> searchMusicByArtist(String artistName);
//
//    public List<Music> searchMusic(String searchString);

    public SearchHelper search(String searchString);

}
