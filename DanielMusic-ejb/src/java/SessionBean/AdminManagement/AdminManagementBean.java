package SessionBean.AdminManagement;

import EntityManager.Account;
import EntityManager.Artist;
import EntityManager.Genre;
import EntityManager.Member;
import EntityManager.Music;
import EntityManager.ReturnHelper;
import SessionBean.CommonInfrastructure.CommonInfrastructureBeanLocal;
import SessionBean.CommonInfrastructure.SendGridLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.StringEscapeUtils;

@Stateless
public class AdminManagementBean implements AdminManagementBeanLocal {

    private static final String artistBandAccountApprovedSubject = "sounds.SG - Artist/Band Account Approved";
    private static final String artistBandAccountApprovedMsg = "Your account has been approved. Your profile, albums and tracks will now be shown to the public.";
    private static final String artistBandAccountRejectedSubject = "sounds.SG - Artist/Band Account Rejected";
    private static final String artistBandAccountRejectedMsg = "Unfortunately your account has been rejected by our administrators.";

    @EJB
    private CommonInfrastructureBeanLocal cibl;
    @EJB
    private SendGridLocal sgl;

    @PersistenceContext
    private EntityManager em;

    @Override
    public ReturnHelper approveArtistOrBand(Long artistOrBandID) {
        System.out.println("AdminManagementBean: approveArtistOrBand() called");
        ReturnHelper result = new ReturnHelper();
        Query q = em.createQuery("SELECT s FROM Account s where s.id=:id");
        q.setParameter("id", artistOrBandID);
        try {
            Account account = (Account) q.getSingleResult();
            if (!(account instanceof Artist)) {
                result.setResult(false);
                result.setDescription("Account does not support this functionality. It is not an artist or band account.");
            } else if (account.getIsDisabled() == true) {
                result.setResult(false);
                result.setDescription("Account can not be approved as it has been disabled.");
            } else {
                if (account instanceof Artist) {
                    q = em.createQuery("SELECT s FROM Artist s where s.id=:id");
                    q.setParameter("id", artistOrBandID);
                    q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
                    Artist artist = (Artist) q.getSingleResult();
                    artist.setIsApproved(1);
                    em.merge(artist);
                    result.setResult(true);
                    result.setDescription("Artist/Band has been approved.");
                    sgl.sendEmail(artist.getEmail(), "no-reply@sounds.sg", artistBandAccountApprovedSubject, artistBandAccountApprovedMsg);
                }
            }
        } catch (Exception ex) {
            System.out.println("AdminManagementBean: approveArtistOrBand() failed");
            result.setResult(false);
            result.setDescription("Failed to approve artist. Internal server error.");
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public ReturnHelper rejectArtistOrBand(Long artistOrBandID) {
        System.out.println("AdminManagementBean: rejectArtistOrBand() called");
        ReturnHelper result = new ReturnHelper();
        Query q = em.createQuery("SELECT s FROM Account s where s.id=:id");
        q.setParameter("id", artistOrBandID);
        try {
            Account account = (Account) q.getSingleResult();
            if (!(account instanceof Artist)) {
                result.setResult(false);
                result.setDescription("Account does not support this functionality. It is not an artist or band account.");
            } else if (account.getIsDisabled() == true) {
                result.setResult(false);
                result.setDescription("Account can not be rejected as it has been disabled.");
            } else {
                if (account instanceof Artist) {
                    q = em.createQuery("SELECT s FROM Artist s where s.id=:id");
                    q.setParameter("id", artistOrBandID);
                    Artist artist = (Artist) q.getSingleResult();
                    artist.setIsApproved(-1);
                    em.merge(artist);
                    result.setResult(true);
                    result.setDescription("Artist has been rejected.");
                    sgl.sendEmail(artist.getEmail(), "no-reply@sounds.sg", artistBandAccountRejectedSubject, artistBandAccountRejectedMsg);
                }
            }
        } catch (Exception ex) {
            System.out.println("AdminManagementBean: rejectArtistOrBand() failed");
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
                q = em.createQuery("select a from Artist a where a.isBand=true AND a.isDisabled=false and a.emailIsVerified=true and a.isApproved=true");
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
    public List<Artist> listAllBands(Boolean isAdmin) {
        System.out.println("AdminManagementBean: listAllBands() called");
        try {
            Query q;
            if (isAdmin) {
                q = em.createQuery("select b from Artist b");
            } else {
                q = em.createQuery("select b from Artist b where b.isBand=true AND b.isDisabled=false and b.emailIsVerified=true and b.isApproved=true");
            }
            List<Artist> listOfBands = q.getResultList();
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
            genre.setName(StringEscapeUtils.escapeHtml4(name));
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

            genre.setName(StringEscapeUtils.escapeHtml4(newName));
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

    @Override
    public Artist getArtist(Long artistID) {
        System.out.println("AdminManagementBean: getArtist() called");
        Artist artist;
        try {
            Query q = em.createQuery("Select a from Artist a where a.id=:artistID");
            q.setParameter("artistID", artistID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            artist = (Artist) q.getSingleResult();
            System.out.println("AdminManagementBean: getArtist() successfully retrieved");
            return artist;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("AdminManagementBean: getArtist() got exception");
            return null;
        }
    }

    @Override
    public Member getMember(Long memberID) {
        System.out.println("AdminManagementBean: getMember() called");
        Member member;
        try {
            Query q = em.createQuery("Select m from Member m where m.id=:memberID");
            q.setParameter("memberID", memberID);
            q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
            member = (Member) q.getSingleResult();
            System.out.println("AdminManagementBean: getMember() successfully retrieved");
            return member;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("AdminManagementBean: getMember() got exception");
            return null;
        }
    }

    public ReturnHelper exportVerifiedFanAccountEmails() {
        ReturnHelper returnHelper = new ReturnHelper();
        returnHelper.setResult(false);
        try {
            Query q = em.createQuery("SELECT s FROM Account s where s.emailIsVerified=true");
            List<Account> accounts = q.getResultList();
            String accountList = "";
            for (Account account : accounts) {
                if (account instanceof Member) {
                    accountList += account.getEmail() + ",";
                }
            }
            if (sgl.sendEmail("admin@sounds.sg", "no-reply@sounds.sg", "sounds.sg | verified accounts email list", accountList)) {
                returnHelper.setResult(true);
                returnHelper.setDescription("List of emails sent");
            } else {
                returnHelper.setDescription("Unable to send the email, try again later");
            }
        } catch (Exception e) {
            returnHelper.setDescription("Error retriving account records");
        }
        return returnHelper;
    }

}
