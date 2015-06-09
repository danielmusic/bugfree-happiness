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

    public List<Artist> listAllArtists(Boolean isAdmim);

    public List<Member> listAllMembers(Boolean isAdmin);

    public List<Genre> listAllGenres();

}
