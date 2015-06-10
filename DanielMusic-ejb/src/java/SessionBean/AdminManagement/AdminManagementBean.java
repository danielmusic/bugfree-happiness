package SessionBean.AdminManagement;

import EntityManager.Account;
import EntityManager.Artist;
import EntityManager.Genre;
import EntityManager.Member;
import EntityManager.ReturnHelper;
import SessionBean.CommonInfrastructure.CommonInfrastructureBeanLocal;
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
                cibl.sendEmail(artist.getEmail(), "TODO", artistAccountApprovedSubject, artistAccountApprovedMsg);
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
                cibl.sendEmail(artist.getEmail(), "TODO", artistAccountRejectedSubject, artistAccountRejectedMsg);
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
        System.out.println("listAllArtists() called");
        try {
            Query q;
            if (isAdmin) {
                q = em.createQuery("select a from Artist a where a.isDisabled=false ");
            } else {
                q = em.createQuery("select a from Artist a where a.isDisabled=false and a.emailIsVerified=true and a.isApproved=true");
            }
            List<Artist> listOfArtists = q.getResultList();
            System.out.println("listAllArtists() called successfully");
            return listOfArtists;
        } catch (Exception e) {
            System.out.println("Error while calling listAllArtists()");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Member> listAllMembers(Boolean isAdmin) {
        System.out.println("listAllMembers() called");
        try {
            Query q;
            if (isAdmin) {
                q = em.createQuery("select m from Member m where m.isDisabled=false");
            } else {
                q = em.createQuery("select m from Member m where m.isDisabled=false and m.emailIsVerified=true");
            }
            List<Member> listOfMembers = q.getResultList();
            System.out.println("listAllMembers() called successfully");
            return listOfMembers;
        } catch (Exception e) {
            System.out.println("Error while calling listAllMembers()");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Genre> listAllGenres() {
        System.out.println("listAllGenres() called");
        try {
            Query q = em.createQuery("select g from Genre g where g.isDeleted=false");
            List<Genre> listOfGenres = q.getResultList();
            System.out.println("listAllGenres() called successfully");
            return listOfGenres;
        } catch (Exception e) {
            System.out.println("Error while calling listAllGenres()");
            e.printStackTrace();
        }
        return null;
    }
}
