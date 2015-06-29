package SessionBean.MusicManagement;

import EntityManager.Album;
import EntityManager.Music;
import EntityManager.ReturnHelper;
import EntityManager.SearchHelper;
import java.io.File;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.servlet.http.Part;

@Local
public interface MusicManagementBeanLocal {

    public ReturnHelper encodeToMP3(File sourceFileName, File targetFileName, int bitrate); //convert all file formats to mp3. so far tested wav file only

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
    public String generateDownloadLink(String fileLocation, Long musicID, Boolean isIncreaseDownloadCount);

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

    /**
     * {@inheritDoc}
     * <p>
     * This is the function for creating music, two copies (128kbps and 320kbps)
     * of the music will be generated and uploaded to cloud storage.
     * </p>
     *
     * @param musicPart The part file.
     * @param albumID The albumID of the music.
     * @param trackNumber The music track number.
     * @param name The name of the music.
     * @param price The price of the music.
     * @param listOfGenreIDs The list of genres this music belongs to.
     * @return @param ReturnHelper
     */
    //still need to handle checking of music length < 10mins
    public ReturnHelper createMusic(Part musicPart, Long albumID, Integer trackNumber, String name, Double price, String lyrics, Integer yearReleased);

    public Music getMusic(Long musicID);

    public ReturnHelper deleteMusic(Long musicID);

    public ReturnHelper editMusic(Long musicID, Integer trackNumber, String name, Double price, String lyrics, String credits);

    public List<Music> ListAllTracksByAlbumID(Long albumID);

    public ReturnHelper createAlbum(Part imagePart, String name, String description, Long artistOrBandID, Integer yearReleased, String credits, Double price);

    public Album getAlbum(Long albumID);

    public List<Album> ListAllAlbumByArtistorBandID(Long artistOrBandAccountID, Boolean showUnpublished, Boolean showUnapproved);

    public ReturnHelper editAlbum(Long albumID, Part imagePart, String name, String description, Integer yearReleased, String credits, Double price);

    public ReturnHelper publishAlbum(Long albumID);

    public ReturnHelper deleteAlbum(Long albumID);

}
