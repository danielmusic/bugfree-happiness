package SessionBean.AdminManagement;

import EntityManager.Account;
import EntityManager.Artist;
import EntityManager.Band;
import EntityManager.Genre;
import EntityManager.Member;
import EntityManager.Music;
import EntityManager.ReturnHelper;
import SessionBean.CommonInfrastructure.CommonInfrastructureBeanLocal;
import SessionBean.CommonInfrastructure.SendGridLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class AdminManagementBean implements AdminManagementBeanLocal {

    private static final String artistAccountApprovedSubject = "Daniel Music: Artist Account Approved";
    private static final String artistAccountApprovedMsg = "Your artist account has been approved. Your artist profile, albums and tracks will now be shown to the public.";
    private static final String artistAccountRejectedSubject = "Daniel Music: Artist Account Rejected";
    private static final String artistAccountRejectedMsg = "Unfortuantely your artist account can not be approved.";

    @EJB
    private CommonInfrastructureBeanLocal cibl;
    @EJB
    private SendGridLocal sgl;
    
    @PersistenceContext
    private EntityManager em;

    @Override
    public ReturnHelper approveArtist(Long artistID) {
        System.out.println("AdminManagementBean: approveArtist() called");
        ReturnHelper result = new ReturnHelper();
        Query q = em.createQuery("SELECT s FROM Account s where s.id=:id");
        q.setParameter("id", artistID);
        try {
            Account account = (Account) q.getSingleResult();
            if (!(account instanceof Artist)) {
                result.setResult(false);
                result.setDescription("Account does not support this functionality. It is not an artist account.");
            } else if (account.getIsDisabled() == true) {
                result.setResult(false);
                result.setDescription("Account can not be aprroved as it has been disabled.");
            } else {
                q = em.createQuery("SELECT s FROM Artist s where s.id=:id");
                q.setParameter("id", artistID);
                Artist artist = (Artist) q.getSingleResult();
                artist.setIsApproved(1);
                em.merge(artist);
                result.setResult(true);
                result.setDescription("Artist has been approved.");
                sgl.sendEmail(artist.getEmail(), "TODO", artistAccountApprovedSubject, artistAccountApprovedMsg);
            }
        } catch (Exception ex) {
            System.out.println("AdminManagementBean: approveArtist() failed");
            result.setResult(false);
            result.setDescription("Failed to approve artist. Internal server error.");
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public ReturnHelper rejectArtist(Long artistID) {
        System.out.println("AdminManagementBean: rejectArtist() called");
        ReturnHelper result = new ReturnHelper();
        Query q = em.createQuery("SELECT s FROM Account s where s.id=:id");
        q.setParameter("id", artistID);
        try {
            Account account = (Account) q.getSingleResult();
            if (!(account instanceof Artist)) {
                result.setResult(false);
                result.setDescription("Account does not support this functionality. It is not an artist account.");
            } else if (account.getIsDisabled() == true) {
                result.setResult(false);
                result.setDescription("Account can not be rejected as it has been disabled.");
            } else {
                q = em.createQuery("SELECT s FROM Artist s where s.id=:id");
                q.setParameter("id", artistID);
                Artist artist = (Artist) q.getSingleResult();
                artist.setIsApproved(1);
                em.merge(artist);
                result.setResult(true);
                result.setDescription("Artist has been rejected.");
                sgl.sendEmail(artist.getEmail(), "TODO", artistAccountRejectedSubject, artistAccountRejectedMsg);
            }
        } catch (Exception ex) {
            System.out.println("AdminManagementBean: rejectArtist() failed");
            result.setResult(false);
            result.setDescription("Failed to approve artist. Internal server error.");
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Artist> listAllArtists(Boolean isAdmin) {
        System.out.println("AdminManagementBean: listAllArtists() called");
        try {
            Query q;
            if (isAdmin) {
                q = em.createQuery("select a from Artist a");
            } else {
                q = em.createQuery("select a from Artist a where a.isDisabled=false and a.emailIsVerified=true and a.isApproved=true");
            }
            List<Artist> listOfArtists = q.getResultList();
            System.out.println("AdminManagementBean: listAllArtists() called successfully");
            return listOfArtists;
        } catch (Exception e) {
            System.out.println("AdminManagementBean: Error while calling listAllArtists()");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Band> listAllBands(Boolean isAdmin) {
        System.out.println("AdminManagementBean: listAllBands() called");
        try {
            Query q;
            if (isAdmin) {
                q = em.createQuery("select b from Band b");
            } else {
                q = em.createQuery("select b from Band b where b.isDisabled=false and b.emailIsVerified=true and b.isApproved=true");
            }
            List<Band> listOfBands = q.getResultList();
            System.out.println("AdminManagementBean: listAllBands() called successfully");
            return listOfBands;
        } catch (Exception e) {
            System.out.println("AdminManagementBean: Error while calling listAllBands()");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Member> listAllMembers(Boolean isAdmin) {
        System.out.println("AdminManagementBean: listAllMembers() called");
        try {
            Query q;
            if (isAdmin) {
                q = em.createQuery("select m from Member m");
            } else {
                q = em.createQuery("select m from Member m where m.isDisabled=false and m.emailIsVerified=true");
            }
            List<Member> listOfMembers = q.getResultList();
            System.out.println("AdminManagementBean: listAllMembers() called successfully");
            return listOfMembers;
        } catch (Exception e) {
            System.out.println("AdminManagementBean: Error while calling listAllMembers()");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Genre> listAllGenres() {
        System.out.println("AdminManagementBean: listAllGenres() called");
        try {
            Query q;
            q = em.createQuery("SELECT g FROM Genre g");
            List<Genre> listOfGenres = q.getResultList();
            System.out.println("AdminManagementBean: listAllGenres() called successfully");
            return listOfGenres;
        } catch (Exception e) {
            System.out.println("AdminManagementBean: Error while calling listAllGenres()");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ReturnHelper createGenre(String name) {
        System.out.println("AdminManagementBean: createGenre() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            Genre genre = new Genre();
            genre.setName(name);
            em.persist(genre);
            result.setID(genre.getId());
            result.setResult(true);
            result.setDescription("Genre created.");
            return result;
        } catch (Exception ex) {
            System.out.println("AdminManagementBean: createGenre() failed");
            result.setResult(false);
            result.setDescription("Failed to create genre. Internal server error.");
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public ReturnHelper updateGenre(Long genreID, String newName) {
        System.out.println("AdminManagementBean: updateGenre() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            Query q = em.createQuery("SELECT e FROM Genre e where e.id=:id");
            q.setParameter("id", genreID);
            Genre genre = (Genre) q.getSingleResult();

            genre.setName(newName);
            em.merge(genre);
            result.setResult(true);
            result.setDescription("Genre updated.");
            return result;
        } catch (Exception ex) {
            System.out.println("AdminManagementBean: updateGenre() failed");
            result.setResult(false);
            result.setDescription("Failed to update genre. Internal server error.");
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public ReturnHelper deleteGenre(Long genreID) {
        System.out.println("AdminManagementBean: deleteGenre() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            Query q = em.createQuery("SELECT e FROM Genre e where e.id=:id");
            q.setParameter("id", genreID);
            Genre genre = (Genre) q.getSingleResult();
            //Remove genre from the genre list stored in the musics beloning to this genre
            List<Music> musics = genre.getListOfMusics();
            for (Music music : musics) {
                List<Genre> genres = music.getListOfGenres();
                genres.remove(genre);
                music.setListOfGenres(genres);
                em.merge(music);
            }
            em.remove(genre);
            result.setResult(true);
            result.setDescription("Genre deleted.");
            return result;
        } catch (Exception ex) {
            System.out.println("AdminManagementBean: deleteGenre() failed");
            result.setResult(false);
            result.setDescription("Failed to delete genre. Internal server error.");
            ex.printStackTrace();
        }
        return result;
    }

}
