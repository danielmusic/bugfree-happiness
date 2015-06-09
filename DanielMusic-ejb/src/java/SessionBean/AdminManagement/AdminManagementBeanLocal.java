package SessionBean.AdminManagement;

import EntityManager.Artist;
import EntityManager.Genre;
import EntityManager.Member;
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
     * This method returns you a list of genres
     * </p>
     *
     * @return List{Genre} otherwise return null if error
     */
    public List<Genre> listAllGenres();

}
