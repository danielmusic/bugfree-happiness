package SessionBean.AdminManagement;

import EntityManager.Artist;
import EntityManager.Genre;
import EntityManager.Member;
import EntityManager.Music;
import EntityManager.ReturnHelper;
import java.util.List;
import javax.ejb.Local;

@Local
public interface AdminManagementBeanLocal {

    public ReturnHelper approveArtist(Long artistID);

    public ReturnHelper rejectArtist(Long artistID);
    /**
     * {@inheritDoc}
     * <p>
     * This method returns you a list of artists
     * </p>
     *
     * @param isAdmin Set this to 'true' if it is an admin function
     * @return List{Artist} otherwise return null if error
     */
    public List<Artist> listAllArtists(Boolean isAdmin);
    /**
     * {@inheritDoc}
     * <p>
     * This method returns you a list of members
     * </p>
     *
     * @param isAdmin Set this to 'true' if it is an admin function
     * @return List{Member} otherwise return null if error
     */
    public List<Member> listAllMembers(Boolean isAdmin);
    /**
     * {@inheritDoc}
     * <p>
     * This method returns you a list of genres, if isAdmin is false then it will return those not deleted genres
     * </p>
     *
     * @param isAdmin Set this to 'true' if it is an admin function
     * @return List{Genre} otherwise return null if error
     */
    public List<Genre> listAllGenres(Boolean isAdmin);
    
    
    public ReturnHelper createGenre(String name);
    public List<Music> listMusicInGenre(Long genreID);
    public ReturnHelper updateGenre(Long genreID, String newName);
    public ReturnHelper addMusicToGenre(Long genreID, Long musicID);
    public ReturnHelper removeMusicFromGenre(Long genreID, Long musicID);
    public ReturnHelper deleteGenre(Long genreID);
    

}
