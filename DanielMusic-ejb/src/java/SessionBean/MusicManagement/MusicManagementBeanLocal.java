package SessionBean.MusicManagement;

import EntityManager.Music;
import EntityManager.ReturnHelper;
import EntityManager.SearchHelper;
import java.io.File;
import java.util.List;
import javax.ejb.Local;
import javax.servlet.http.Part;

@Local
public interface MusicManagementBeanLocal {

    public ReturnHelper encodeToMP3(File sourceFileName, File targetFileName, int bitrate); //convert all file formats to mp3. so far tested wav file only

    public void testAdaptivePayment();

    /**
     * {@inheritDoc}
     * <p>
     * Generate a temporary download link signed URL for member to download the
     * mp3 music.
     * </p>
     *
     * @param email Email of the member requesting the download.
     * @param musicID ID of the music entity.
     * @return ReturnHelper: Contains the generated download link in the
     * description
     */
    public ReturnHelper generateDownloadLink(String email, Long musicID);

    /**
     * {@inheritDoc}
     * <p>
     * For the discovery of music by genre arranged in reverse chronological
     * order.
     * </p>
     *
     * @param genreID The ID of the genre that you wish to discover by.
     * @return List{Music}
     */
    public List<Music> searchMusicByGenre(Long genreID);
//
//    public List<Music> searchMusicByArtist(String artistName);
//
//    public List<Music> searchMusic(String searchString);

    /**
     * {@inheritDoc}
     * <p>
     * This is the general search function which returns a SearchHelper.
     * </p>
     *
     * @param searchString The search input for searching.
     * @return SearchHelper: List{Album} listOfAlbums, List{Artist}
     * listOfArtists, List{Music} listOfMusics
     */
    public SearchHelper search(String searchString);

    public ReturnHelper createMusic(Part musicPart, Long albumID, Integer trackNumber, String name, String artistName, Double price, List<Long> listOfGenreIDs);
}
