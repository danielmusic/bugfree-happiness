package SessionBean.AccountManagement;

import EntityManager.Account;
import EntityManager.Admin;
import EntityManager.Artist;
import EntityManager.Band;
import EntityManager.Genre;
import EntityManager.Member;
import EntityManager.ReturnHelper;
import SessionBean.CommonInfrastructure.CommonInfrastructureBeanLocal;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.Part;

@Stateless
public class AccountManagementBean implements AccountManagementBeanLocal {

    @EJB
    private CommonInfrastructureBeanLocal cibl;

    public AccountManagementBean() {
    }

    @PersistenceContext
    private EntityManager em;

    @Override
    public ReturnHelper loginAccount(String email, String password) {
        System.out.println("AccountManagementBean: loginAccount() called");
        ReturnHelper result = new ReturnHelper();
        try {
            Query q = em.createQuery("SELECT a FROM Account a where a.email=:email");
            q.setParameter("email", email);
            Account account = (Account) q.getSingleResult();
            String passwordSalt = account.getPasswordSalt();
            String passwordHash = generatePasswordHash(passwordSalt, password);
            if (passwordHash.equals(account.getPasswordHash())) {
                if (account.getIsDisabled()) {
                    result.setResult(false);
                    result.setDescription("Unable to login, account is disabled.");
                    return result;
                }
                System.out.println("loginAccount(): Account with email:" + email + " logged in successfully.");
                em.detach(account);
                account.setPasswordHash(null);
                account.setPasswordSalt(null);
                result.setResult(true);
                result.setDescription("Login successful.");
                return result;
            } else {
                System.out.println("loginAccount(): Login credentials provided were incorrect, password wrong.");
                result.setResult(false);
                result.setDescription("Login credentials provided incorrect.");
                return result;
            }
        } catch (NoResultException ex) {//cannot find account with that email
            System.out.println("loginAccount(): Login credentials provided were incorrect, no such email found.");
            result.setResult(false);
            result.setDescription("Login credentials provided incorrect.");
            return result;
        } catch (Exception ex) {
            System.out.println("loginAccount(): Internal error");
            ex.printStackTrace();
            result.setResult(false);
            result.setDescription("Unable to login, internal server error.");
            return result;
        }
    }

    @Override
    public Account getAccount(String email) {
        System.out.println("AccountManagementBean: getAccount() called");
        try {
            Query q = em.createQuery("SELECT a FROM Account a where a.email=:email");
            q.setParameter("email", email);
            Account account = (Account) q.getSingleResult();
            if (account instanceof Member) {
                q = em.createQuery("SELECT m FROM Member m where m.email=:email");
                q.setParameter("email", email);
                Member member = (Member) q.getSingleResult();
                return member;
            } else if (account instanceof Artist) {
                q = em.createQuery("SELECT a FROM Artist a where a.email=:email");
                q.setParameter("email", email);
                Artist artist = (Artist) q.getSingleResult();
                return artist;
            } else if (account instanceof Band) {
                q = em.createQuery("SELECT a FROM Band a where a.email=:email");
                q.setParameter("email", email);
                Band band = (Band) q.getSingleResult();
                return band;
            } else if (account instanceof Admin) {
                q = em.createQuery("SELECT a FROM Admin a where a.email=:email");
                q.setParameter("email", email);
                Admin admin = (Admin) q.getSingleResult();
                return admin;
            }
            Member member = new Member();
            return member;
        } catch (NoResultException ex) {
            System.out.println("getAccount(): Could not find account with that email");
            return null;
        } catch (Exception ex) {
            System.out.println("getAccount(): Internal error");
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public ReturnHelper registerAccount(String name, String email, String password, boolean isAdmin, boolean isArtist, boolean isBand) {
        System.out.println("AccountManagementBean: registerAccount() called");
        ReturnHelper result = new ReturnHelper();
        try {
            if (checkIfEmailExists(email)) {
                result.setResult(false);
                result.setDescription("Unable to register, email already in use.");
                return result;
            }
            String passwordSalt = generatePasswordSalt();
            String passwordHash = generatePasswordHash(passwordSalt, password);
            if (isAdmin) {
                Admin admin = new Admin();
                admin.setEmail(email);
                admin.setNewEmail(email);
                admin.setPasswordHash(passwordHash);
                admin.setPasswordSalt(passwordSalt);
                admin.setName(name);
                em.persist(admin);
                Query q = em.createQuery("SELECT a FROM Admin a where a.email=:email");
                q.setParameter("email", email);
                admin = (Admin) q.getSingleResult();
                result.setID(admin.getId());
            } else if (isArtist) {
                //Only allow registration if artist name is unique
                if (checkIfArtistOrBandNameExists(name)) {
                    result.setDescription("Artist name cannot be registered as it has already been taken.");
                    return result;
                }
                Artist artist = new Artist();
                artist.setEmail(email);
                artist.setNewEmail(email);
                artist.setPasswordHash(passwordHash);
                artist.setPasswordSalt(passwordSalt);
                artist.setName(name);
                em.persist(artist);
                Query q = em.createQuery("SELECT a FROM Artist a where a.email=:email");
                q.setParameter("email", email);
                artist = (Artist) q.getSingleResult();
                result.setID(artist.getId());
            } else if (isBand) {
                //Only allow registration if artist name is unique
                if (checkIfArtistOrBandNameExists(name)) {
                    result.setDescription("Band name cannot be registered as it has already been taken.");
                    return result;
                }
                Band band = new Band();
                band.setEmail(email);
                band.setNewEmail(email);
                band.setPasswordHash(passwordHash);
                band.setPasswordSalt(passwordSalt);
                band.setName(name);
                em.persist(band);
                Query q = em.createQuery("SELECT a FROM Band a where a.email=:email");
                q.setParameter("email", email);
                band = (Band) q.getSingleResult();
                result.setID(band.getId());
            } else {
                Member member = new Member();
                member.setEmail(email);
                member.setNewEmail(email);
                member.setPasswordHash(passwordHash);
                member.setPasswordSalt(passwordSalt);
                member.setName(name);
                em.persist(member);
                Query q = em.createQuery("SELECT a FROM Member a where a.email=:email");
                q.setParameter("email", email);
                member = (Member) q.getSingleResult();
                result.setID(member.getId());
            }
            generateAndSendVerificationEmail(email);
            result.setResult(true);
            result.setDescription("Account registered successfully.");
            return result;
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: registerAccount() failed");
            ex.printStackTrace();
            result.setResult(false);
            result.setDescription("Failed to register account due to internal server error.");
            return result;
        }
    }

    @Override
    public ReturnHelper enableAccount(Long accountID) {
        System.out.println("AccountManagementBean: enableAccount() called");
        ReturnHelper result = new ReturnHelper();
        Query q = em.createQuery("SELECT s FROM Account s where s.id=:id");
        q.setParameter("id", accountID);
        try {
            Account account = (Account) q.getSingleResult();
            if (account.getIsDisabled() == false) {
                result.setResult(false);
                result.setDescription("Account is not disabled.");
            } else {
                account.setIsDisabled(false);
                em.merge(account);
                result.setResult(true);
                result.setDescription("Account enabled successfully.");
            }
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: enableAccount() failed");
            result.setResult(false);
            result.setDescription("Failed to enable account. Internal server error.");
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public ReturnHelper disableAccount(Long accountID) {
        System.out.println("AccountManagementBean: disableAccount() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        Query q = em.createQuery("SELECT s FROM Account s where s.id=:id");
        q.setParameter("id", accountID);
        try {
            Account account = (Account) q.getSingleResult();
            if (account.getIsDisabled() == true) {
                result.setDescription("Account is already disabled.");
            } else {
                account.setIsDisabled(true);
                em.merge(account);
                result.setResult(true);
                result.setDescription("Account disabled successfully.");
            }
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: enableAccount() failed");
            result.setDescription("Failed to disable account. Internal server error.");
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean checkIfEmailExists(String email) {
        System.out.println("AccountManagementBean: checkIfEmailExists() called");
        Query q = em.createQuery("SELECT a FROM Account a WHERE a.email=:email");
        q.setParameter("email", email);
        try {
            Account account = (Account) q.getSingleResult();
            return true;
        } catch (NoResultException ex) {
            return false;
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: checkIfEmailExists() failed");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean checkIfArtistNameExists(String name) {
        System.out.println("AccountManagementBean: checkIfAritstNameExists() called");
        Query q = em.createQuery("SELECT a FROM Artist a WHERE a.name=:name");
        q.setParameter("name", name);
        try {
            Artist artist = (Artist) q.getSingleResult();
            return true;
        } catch (NoResultException ex) {
            return false;
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: checkIfAritstNameExists() failed");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean checkIfBandNameExists(String name) {
        System.out.println("AccountManagementBean: checkIfBandNameExists() called");
        Query q = em.createQuery("SELECT a FROM Band a WHERE a.name=:name");
        q.setParameter("name", name);
        try {
            Band band = (Band) q.getSingleResult();
            return true;
        } catch (NoResultException ex) {
            return false;
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: checkIfBandNameExists() failed");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean checkIfArtistOrBandNameExists(String name) {
        System.out.println("AccountManagementBean: checkIfArtistOrBandNameExists() called");
        Query q = em.createQuery("SELECT a FROM Artist a WHERE a.name=:name and a.isDisabled=false");
        Query qq = em.createQuery("SELECT b FROM Band b WHERE b.name=:name and b.isDisabled=false");
        q.setParameter("name", name);
        qq.setParameter("name", name);
        try {
            Artist artist = (Artist) q.getSingleResult();
            Band band = (Band) qq.getSingleResult();
            return true;
        } catch (NoResultException ex) {
            return false;
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: checkIfArtistOrBandNameExists() failed");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public ReturnHelper generateAndSendVerificationEmail(String emailAddress) {
        System.out.println("AccountManagementBean: sendVerificationEmail() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            //Check if the account exists & if there is any unverified email tag tied to the account, if not 
            String unauthorizedMsg = "There is no account registered with this email address or the account email has already been verified.";
            if (!checkIfEmailExists(emailAddress)) {
                result.setDescription(unauthorizedMsg);
                return result;
            }
            Query q = em.createQuery("SELECT a FROM Account a WHERE a.email=:email");
            q.setParameter("email", emailAddress);
            Account account = (Account) q.getSingleResult();
            if (account.getNewEmailIsVerified()) {
                result.setDescription(unauthorizedMsg);
                return result;
            }
            //Generate the verification code & store it into DB
            Random r = new Random();
            int verificationCode = r.nextInt(9999);
            account.setVerificationCode(verificationCode + "");
            em.merge(account);
            //Send the verification code
            String verificationInstructions = "Verification instruction";
            Boolean emailSent = false;//cibl.sendEmail(account.getNewEmail(), "no-reply@example.com", "Daniel Music Account Verification", verificationInstructions);
            if (emailSent) {
                result.setResult(true);
                result.setDescription("Verification email sent successfully, you should receieve the email in your email inbox (or spam folder) within the next 5 minutes.");
            } else {
                result.setDescription("Unable to send verificaiton email due to an internal serverr error. Please try again later.");
            }
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: sendVerificationEmail() failed");
            ex.printStackTrace();
            result.setDescription("Unable to send verification email because of an internal server error, please try again later.");
        }
        return result;
    }

    @Override
    public ReturnHelper enterVerificationCode(String emailAddress, String verificationCode) {
        System.out.println("AccountManagementBean: enterVerificationCode() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            //Check if the account exists & if there is any unverified email tag tied to the account, if not 
            String unauthorizedMsg = "There is no account registered with this email address or the account email has already been verified.";
            if (!checkIfEmailExists(emailAddress)) {
                result.setDescription(unauthorizedMsg);
                return result;
            }
            Query q = em.createQuery("SELECT a FROM Account a WHERE a.email=:email");
            q.setParameter("email", emailAddress);
            Account account = (Account) q.getSingleResult();
            if (account.getNewEmailIsVerified()) {
                result.setDescription(unauthorizedMsg);
                return result;
            }
            //Retrieve the verification code, compare and update account
            if (account.getVerificationCode().equalsIgnoreCase(verificationCode)) {
                account.setEmail(account.getNewEmail());
                account.setEmailIsVerified(true);
                account.setNewEmailIsVerified(true);
                account.setNewEmail("");
                em.merge(account);
                result.setResult(true);
                result.setDescription("Verification email sent successfully, you should receieve the email in your email inbox (or spam folder) within the next 5 minutes.");
            } else {
                result.setDescription("Invalid verification code, please try again.");
            }
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: enterVerificationCode() failed");
            ex.printStackTrace();
            result.setDescription("Unable to verify code because of an internal server error, please try again later.");
        }
        return result;
    }

    @Override
    public String generatePasswordHash(String salt, String password) {
        String passwordHash = null;
        try {
            password = salt + password;
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            passwordHash = sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("AccountManagementBean: generatePasswordHash() failed");
            ex.printStackTrace();
        }
        return passwordHash;
    }

    @Override
    public String generatePasswordSalt() {
        byte[] salt = new byte[16];
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.nextBytes(salt);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("AccountManagementBean: generatePasswordSalt() failed");
            ex.printStackTrace();
        }
        return Arrays.toString(salt);
    }

    @Override
    public ReturnHelper updateMemberProfile(Long accountID, String newName) {
        System.out.println("AccountManagementBean: updateAccountProfile() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        Query q = em.createQuery("SELECT s FROM Account s WHERE s.id=:id");
        q.setParameter("id", accountID);
        try {
            Account account = (Account) q.getSingleResult();
            if (newName != null && !newName.equals("")) {
                account.setName(newName);
            } else {
                result.setDescription("Name cannot be empty.");
            }
            em.merge(account);
            result.setResult(true);
            result.setDescription("Profile updated.");
        } catch (NoResultException ex) {
            result.setDescription("Account no longer exists.");
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: updateMemberProfile() failed");
            result.setDescription("Update profile failed, internal server error.");
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public ReturnHelper updateMemberProfilePicture(Long accountID, Part profilePicture) {
        //TODO
        System.out.println("AccountManagementBean: updateMemberProfilePicture() called");
        ReturnHelper result = new ReturnHelper();
        try {
            Account account = new Admin();//todo
            if (profilePicture != null) {
                //Save file to local drive first
                InputStream fileInputStream = profilePicture.getInputStream();
                OutputStream fileOutputStream = new FileOutputStream("/img/profile/" + account.getId() + ".jpg");
                int nextByte;
                while ((nextByte = fileInputStream.read()) != -1) {
                    fileOutputStream.write(nextByte);
                }
                fileOutputStream.close();
                fileInputStream.close();
                //TODO: Upload file to cloud storage
                //Update URL address
                //account.setImageURL("");
            }
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: updateMemberProfilePicture() failed");
            result.setDescription("Update profille failed, internal server error.");
            ex.printStackTrace();
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ReturnHelper updateArtistProfile(Long artistID, Long genreID, String biography, String influences, String contactEamil, String paypalEmail, String facebookURL, String instagramURL, String twitterURL) {
        System.out.println("AccountManagementBean: updateArtistProfile() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            Query q = em.createQuery("SELECT e FROM Artist e WHERE e.id=:id");
            q.setParameter("id", artistID);
            Artist artist = (Artist) q.getSingleResult();
            //Update old genre list
            Genre oldGenre = artist.getGenre();
            if (oldGenre != null) {
                List<Artist> genreArtists = oldGenre.getListOfArtists();
                genreArtists.remove(artist);
                oldGenre.setListOfArtists(genreArtists);
                em.merge(oldGenre);
            }
            //Update new genre list
            q = em.createQuery("SELECT e FROM Genre e WHERE e.id=:id");
            q.setParameter("id", genreID);
            Genre newGenre = (Genre) q.getSingleResult();
            List<Artist> genreArtists = newGenre.getListOfArtists();
            genreArtists.add(artist);
            em.merge(newGenre);
            //Update artist genre
            artist.setGenre(newGenre);
            em.merge(artist);
            result.setResult(true);
            result.setDescription("Profile updated.");
        } catch (NoResultException ex) {
            result.setDescription("Unable to update profile. Genre selected may have been deleted. Please try again.");
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: updateArtistProfile() failed");
            result.setDescription("Update profile failed, internal server error.");
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public ReturnHelper updateArtistName(Long artistID, String newName) {
        System.out.println("AccountManagementBean: updateArtistName() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            Query q = em.createQuery("SELECT e FROM Artist e WHERE e.id=:id");
            q.setParameter("id", artistID);
            Artist artist = (Artist) q.getSingleResult();
            artist.setIsApproved(-2);//Pending
            artist.setName(newName);
            em.merge(artist);
            result.setResult(true);
            result.setDescription("Name updated and is now pending approval.");
        } catch (NoResultException ex) {
            result.setDescription("Artist no longer exists.");
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: updateArtistName() failed");
            result.setDescription("Name change failed, internal server error.");
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public ReturnHelper updateArtistProfilePicture(Long artistID, Part profilePicture) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ReturnHelper updateBandProfile(Long bandID, String members, Date dateFormed, Long genreID, String biography, String influences, String contactEamil, String paypalEmail, String facebookURL, String instagramURL, String twitterURL) {
        System.out.println("AccountManagementBean: updateBandProfile() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            Query q = em.createQuery("SELECT e FROM Band e WHERE e.id=:id");
            q.setParameter("id", bandID);
            Band band = (Band) q.getSingleResult();
            //Update old genre list
            Genre oldGenre = band.getGenre();
            if (oldGenre != null) {
                List<Band> genreBand = oldGenre.getListOfBands();
                genreBand.remove(band);
                oldGenre.setListOfBands(genreBand);
                em.merge(oldGenre);
            }
            //Update new genre list
            q = em.createQuery("SELECT e FROM Genre e WHERE e.id=:id");
            q.setParameter("id", genreID);
            Genre newGenre = (Genre) q.getSingleResult();
            List<Band> genreBands = newGenre.getListOfBands();
            genreBands.add(band);
            em.merge(newGenre);
            //Update band genre
            band.setGenre(newGenre);
            em.merge(band);
            result.setResult(true);
            result.setDescription("Profile updated.");
        } catch (NoResultException ex) {
            result.setDescription("Unable to update profile. Genre selected may have been deleted. Please try again.");
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: updateBandProfile() failed");
            result.setDescription("Update profile failed, internal server error.");
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public ReturnHelper updateBandName(Long bandID, String newName) {
                System.out.println("AccountManagementBean: updateBandName() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            Query q = em.createQuery("SELECT e FROM Band e WHERE e.id=:id");
            q.setParameter("id", bandID);
            Band band = (Band) q.getSingleResult();
            band.setIsApproved(-2);//Pending
            band.setName(newName);
            em.merge(band);
            result.setResult(true);
            result.setDescription("Name updated and is now pending approval.");
        } catch (NoResultException ex) {
            result.setDescription("Band no longer exists.");
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: updateBandName() failed");
            result.setDescription("Name change failed, internal server error.");
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public ReturnHelper updateBandProfilePicture(Long bandID, Part profilePicture) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ReturnHelper deleteAccountProfilePicture(Long accountID) {
        System.out.println("AccountManagementBean: deleteAccountProfilePicture() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        Query q = em.createQuery("SELECT s FROM Account s WHERE s.id=:id");
        q.setParameter("id", accountID);
        try {
            Account account = (Account) q.getSingleResult();
            if (account.getImageURL() != null || !account.getImageURL().equals("")) {
                account.setImageURL("");
                result.setResult(true);
                result.setDescription("Profile picture removed.");
            } else {
                result.setDescription("No profile picture to remove!");
            }
        } catch (NoResultException ex) {
            result.setDescription("Unable to find account with the provided ID.");
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: deleteAccountProfilePicture() failed");
            result.setDescription("Unable to remove profile picture, internal server error.");
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public ReturnHelper updateAccountPassword(Long accountID, String oldPassword, String newPassword) {
        System.out.println("AccountManagementBean: updateAccountPassword() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        Query q = em.createQuery("SELECT a FROM Account a WHERE a.id=:id");
        q.setParameter("id", accountID);
        try {
            Account account = (Account) q.getSingleResult();
            if (!generatePasswordHash(account.getPasswordSalt(), oldPassword).equals(account.getPasswordHash())) {
                result.setDescription("Old password provided is invalid, password not updated.");
            } else {
                account.setPasswordSalt(generatePasswordSalt());
                account.setPasswordHash(generatePasswordHash(account.getPasswordSalt(), newPassword));
                em.merge(account);
                result.setResult(true);
                result.setDescription("Password updated successfully.");
            }
        } catch (NoResultException ex) {
            result.setDescription("Unable to find account with the provided ID.");
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: updateAccountPassword() failed");
            result.setDescription("Unable to update account's password due to internal server error. Please try again later.");
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public ReturnHelper updateAccountEmail(Long accountID, String newEmail) {
        System.out.println("AccountManagementBean: updateAccountEmail() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        Query q = em.createQuery("SELECT s FROM Account s WHERE s.id=:id");
        q.setParameter("id", accountID);
        try {
            Account account = (Account) q.getSingleResult();
            account.setNewEmail(newEmail);
            account.setNewEmailIsVerified(false);
            em.merge(account);
            ReturnHelper verificationCodeResult = generateAndSendVerificationEmail(newEmail);
            if (verificationCodeResult.getResult()) {
                result.setResult(true);
                result.setDescription("Account email verification sent successfully successfully.");
            } else {
                result.setDescription(verificationCodeResult.getDescription());
            }
        } catch (NoResultException ex) {
            result.setDescription("Unable to find account with the provided ID.");
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: updateAccountEmail() failed");
            result.setDescription("Unable to update account's email, internal server error.");
            ex.printStackTrace();
        }
        return result;
    }

}
