package SessionBean.AccountManagement;

import EntityManager.Account;
import EntityManager.Admin;
import EntityManager.Artist;
import EntityManager.Genre;
import EntityManager.Member;
import EntityManager.ReturnHelper;
import EntityManager.ShoppingCart;
import SessionBean.CommonInfrastructure.CommonInfrastructureBeanLocal;
import SessionBean.CommonInfrastructure.SendGridLocal;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
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

    @EJB
    private SendGridLocal sgl;

    public AccountManagementBean() {
    }

    @PersistenceContext
    private EntityManager em;

    private static final String UNAUTHORIZED_EMAIL_VERIFICATION_ATTEMPT = "There is no account registered with this email address or the account email has already been verified.";
    private static final String UNAUTHORIZED_RESET_PASSWORD_ATTEMPT = "There is no password reset request for this account or the password reset code is invalid or has expired.";

    @Override
    public ReturnHelper loginAccount(String email, String password) {
        System.out.println("AccountManagementBean: loginAccount() called");
        ReturnHelper result = new ReturnHelper();
        try {
            Query q = em.createQuery("SELECT a FROM Account a where a.email=:email");
            q.setParameter("email", email);
            Account account = (Account) q.getSingleResult();
            Boolean loginSuccess = validatePassword(password, account.getPassword());
            if (loginSuccess) {
                if (account.getIsDisabled()) {
                    result.setResult(false);
                    result.setDescription("Unable to login, account is disabled.");
                    return result;
                }
                System.out.println("loginAccount(): Account with email:" + email + " logged in successfully.");
                em.detach(account);
                account.setPassword(null);
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

    private static boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
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
    public Account getAccount(Long id) {
        System.out.println("AccountManagementBean: getAccount() called");
        try {
            Query q = em.createQuery("SELECT a FROM Account a where a.id=:id");
            q.setParameter("id", id);
            Account account = (Account) q.getSingleResult();
            if (account instanceof Member) {
                q = em.createQuery("SELECT m FROM Member m where m.id=:id");
                q.setParameter("id", id);
                Member member = (Member) q.getSingleResult();
                return member;
            } else if (account instanceof Artist) {
                q = em.createQuery("SELECT a FROM Artist a where a.id=:id");
                q.setParameter("id", id);
                Artist artist = (Artist) q.getSingleResult();
                return artist;
            } else if (account instanceof Admin) {
                q = em.createQuery("SELECT a FROM Admin a where a.id=:id");
                q.setParameter("id", id);
                Admin admin = (Admin) q.getSingleResult();
                return admin;
            }
            Member member = new Member();
            return member;
        } catch (NoResultException ex) {
            System.out.println("getAccount(): Could not find account with that id");
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
                admin.setPassword(passwordHash);
                admin.setName(name);
                em.persist(admin);
                Query q = em.createQuery("SELECT a FROM Admin a where a.email=:email");
                q.setParameter("email", email);
                admin = (Admin) q.getSingleResult();
                result.setID(admin.getId());
                ShoppingCart cart = new ShoppingCart();
                cart.setAccount(admin);
                em.persist(cart);
            } else if (isArtist || isBand) {
                //Only allow registration if artist name is unique
                if (checkIfArtistNameExists(name)) {
                    result.setDescription("Artist or band name cannot be registered as it has already been taken.");
                    return result;
                }
                Artist artist = new Artist();
                artist.setEmail(email);
                artist.setPassword(passwordHash);
                artist.setName(name);
                if (isArtist) {
                    artist.setIsBand(false);
                } else if (isBand) {
                    artist.setIsBand(true);
                }
                em.persist(artist);
                Query q = em.createQuery("SELECT a FROM Artist a where a.email=:email");
                q.setParameter("email", email);
                artist = (Artist) q.getSingleResult();
                result.setID(artist.getId());
                ShoppingCart cart = new ShoppingCart();
                cart.setAccount(artist);
                em.persist(cart);
            } else {
                Member member = new Member();
                member.setEmail(email);
                member.setPassword(passwordHash);
                member.setName(name);
                em.persist(member);
                Query q = em.createQuery("SELECT a FROM Member a where a.email=:email");
                q.setParameter("email", email);
                member = (Member) q.getSingleResult();
                result.setID(member.getId());
                ShoppingCart cart = new ShoppingCart();
                cart.setAccount(member);
                em.persist(cart);
            }
            generateAndSendVerificationEmail(result.getID(), email, false);
            result.setResult(true);
            result.setDescription("Account registered successfully. You may now login");
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
    public boolean checkIfNewEmailExists(String newEmail) {
        System.out.println("AccountManagementBean: checkIfNewEmailExists() called");
        Query q = em.createQuery("SELECT a FROM Account a WHERE a.newEmail=:newEmail");
        q.setParameter("newEmail", newEmail);
        try {
            Account account = (Account) q.getSingleResult();
            return true;
        } catch (NoResultException ex) {
            return false;
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: checkIfNewEmailExists() failed");
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
    public ReturnHelper generateAndSendVerificationEmail(Long accountID, String email, Boolean changingEmail) {
        System.out.println("AccountManagementBean: generateAndSendVerificationEmail() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            //Check if the account exists & if there is any unverified email tag tied to the account, if not 
            String unauthorizedMsg = "There is no account registered with this email address or the account email has already been verified.";
            if (!changingEmail && !checkIfEmailExists(email)) {
                result.setDescription(unauthorizedMsg);
                return result;
            }
            Query q = em.createQuery("SELECT a FROM Account a WHERE a.id=:accountID");
            q.setParameter("accountID", accountID);
            Account account = (Account) q.getSingleResult();

            //Trying to verify already verified initial email
            if (!changingEmail && account.getEmailIsVerified()) {
                result.setDescription(unauthorizedMsg);
                return result;
            } else if (changingEmail && account.getNewEmailIsVerified()) {
                //new email
                result.setDescription(unauthorizedMsg);
                return result;
            }
            //Generate the verification code & store it into DB
            int verificationCode = (int) (Math.random() * 9000) + 1000;
            if (changingEmail) {
                account.setNewEmailVerificationCode(verificationCode + "");
            } else { //initial registration
                account.setInitialEmailVerificationCode(verificationCode + "");
            }
            em.merge(account);
            //Send the verification code
            String verificationInstructions = "";
            Boolean emailSent = false;
            if (changingEmail) {
                verificationInstructions = "You have request for a change of account email.<br/><br/>"
                        + "Your verification code is: <b>" + verificationCode + "</b><br/>"
                        + "Visit this link to key in the code: todo <br/><br/>"
                        // need to login first before they can key
                        + "If this email change was not initated by you, please ignore this email."
                        + "TODO";
                emailSent = sgl.sendEmail(account.getNewEmail(), "no-reply@sounds.sg", "Sounds.SG Account Verification", verificationInstructions);
            } else {
                verificationInstructions = "Thank you for registering at Sounds.SG <br/><br/>"
                        + "Your verification code is: <b>" + verificationCode + "</b><br/>"
                        + "Visit this link to key in the code: todo <br/><br/>"
                        + "If you did not sign up for an account at Sounds.SG, please ignore this email."
                        + "TODO";
                emailSent = sgl.sendEmail(account.getEmail(), "no-reply@sounds.sg", "Sounds.SG Account Verification", verificationInstructions);
            }
            if (emailSent) {
                result.setResult(true);
                result.setDescription("Verification email sent, you should receieve the email in your email inbox (or spam folder) within the next 5 minutes.");
            } else {
                result.setDescription("Unable to send verificaiton email due to issues with our email servers. Please try again later.");
            }
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: generateAndSendVerificationEmail() failed");
            ex.printStackTrace();
            result.setDescription("Unable to send verification email because of an internal server error, please try again later.");
        }
        return result;
    }

    @Override
    public ReturnHelper enterEmailVerificationCode(String email, String verificationCode) {
        System.out.println("AccountManagementBean: enterEmailVerificationCode() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            //Check if the account exists & if there is any unverified email tag tied to the account, if not 
            if (!checkIfEmailExists(email)) {
                result.setDescription(UNAUTHORIZED_EMAIL_VERIFICATION_ATTEMPT);
                return result;
            }
            Query q = em.createQuery("SELECT a FROM Account a WHERE a.email=:email");
            q.setParameter("email", email);
            Account account = (Account) q.getSingleResult();
            if (account.getEmailIsVerified()) {
                result.setDescription(UNAUTHORIZED_EMAIL_VERIFICATION_ATTEMPT);
                return result;
            }
            //Retrieve the verification code, compare and update account
            if (account.getInitialEmailVerificationCode().equalsIgnoreCase(verificationCode)) {
                account.setEmailIsVerified(true);
                account.setNewEmailIsVerified(true);
                account.setNewEmail("");
                account.setNewEmailVerificationCode(null);
                account.setNewEmailVerificationCodeGeneratedDate(null);
                em.merge(account);
                result.setResult(true);
                result.setDescription("Email verified.");
            } else {
                result.setDescription("Invalid verification code.");
            }
        } catch (NoResultException ex) {
            result.setDescription(UNAUTHORIZED_EMAIL_VERIFICATION_ATTEMPT);
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: enterEmailVerificationCode() failed");
            ex.printStackTrace();
            result.setDescription("Unable to verify code because of an internal server error, please try again later.");
        }
        return result;
    }

    @Override
    public ReturnHelper enterNewEmailVerificationCode(String newEmailAddress, String verificationCode) {
        System.out.println("AccountManagementBean: enterNewEmailVerificationCode() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            //Check if the account exists & if there is any unverified email tag tied to the account
            Query q = em.createQuery("SELECT a FROM Account a WHERE a.newEmail=:email");
            q.setParameter("email", newEmailAddress);
            Account account = (Account) q.getSingleResult();
            if (account.getNewEmailIsVerified()) {
                result.setDescription(UNAUTHORIZED_EMAIL_VERIFICATION_ATTEMPT);
                return result;
            }
            //Retrieve the verification code, compare and update account
            if (account.getNewEmailVerificationCode().equalsIgnoreCase(verificationCode)) {
                account.setEmail(account.getNewEmail());
                account.setEmailIsVerified(true);
                account.setNewEmailIsVerified(true);
                account.setNewEmail("");
                account.setNewEmailVerificationCode(null);
                account.setNewEmailVerificationCodeGeneratedDate(null);
                em.merge(account);
                result.setResult(true);
                result.setDescription("New email verified. You may now use your new email to login.");
            } else {
                result.setDescription("Invalid verification code, please try again.");
            }
        } catch (NoResultException ex) {
            result.setDescription(UNAUTHORIZED_EMAIL_VERIFICATION_ATTEMPT);
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: enterEmailVerificationCode() failed");
            ex.printStackTrace();
            result.setDescription("Unable to verify code because of an internal server error, please try again later.");
        }
        return result;
    }

    @Override
    public ReturnHelper generateAndSendForgetPasswordEmail(String email) {
        System.out.println("AccountManagementBean: generateAndSendForgetPasswordEmail() called");
        //todo
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            //Check if the account exists
            String unauthorizedMsg = "There is no account registered with this email address.";
            if (!checkIfEmailExists(email)) {
                result.setDescription(unauthorizedMsg);
                return result;
            }
            Query q = em.createQuery("SELECT a FROM Account a WHERE a.email=:email");
            q.setParameter("email", email);
            Account account = (Account) q.getSingleResult();
            //Generate the verification code & store it into DB
            SecureRandom random = new SecureRandom();
            String resetCode = new BigInteger(130, random).toString(32);
            account.setPasswordResetCode(resetCode);
            account.setForgetPassword(true);
            em.merge(account);
            //Send the verification code
            String resetInstructions = "You have request for a password reset.<br/><br/>"
                    + "Your password reset code is: <b>" + resetCode + "</b><br/>"
                    + "Visit this link to key in the code: todo <br/><br/>"
                    + "If this password reset was not initated by you, please ignore this email."
                    + "TODO";
            Boolean emailSent = sgl.sendEmail(account.getEmail(), "no-reply@sounds.sg", "Sounds.SG Password Reset", resetInstructions);
            if (emailSent) {
                result.setResult(true);
                result.setDescription("Password reset code sent, you should receieve the email in your email inbox (or spam folder) within the next 5 minutes.");
            } else {
                result.setDescription("Unable to send password reset code due issues with our email servers. Please try again later.");
            }
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: generateAndSendForgetPasswordEmail() failed");
            ex.printStackTrace();
            result.setDescription("Unable to passowrd reset coode because of an internal server error, please try again later.");
        }
        return result;
    }

    @Override
    public ReturnHelper enterForgetPasswordCode(String email, String passwordResetCode) {
        System.out.println("AccountManagementBean: enterForgetPasswordCode() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            //Check if the account exists & if there is any unverified email tag tied to the account
            if (!checkIfEmailExists(email)) {
                result.setDescription(UNAUTHORIZED_EMAIL_VERIFICATION_ATTEMPT);
                return result;
            }
            Query q = em.createQuery("SELECT a FROM Account a WHERE a.email=:email");
            q.setParameter("email", email);
            Account account = (Account) q.getSingleResult();
            if (account.getForgetPassword() == false) {
                result.setDescription(UNAUTHORIZED_RESET_PASSWORD_ATTEMPT);
                return result;
            }
            //Check if code has expired
            int hoursDiff = getHoursDifference(new Date(), account.getPasswordResetCodeGeneratedDate());
            if (hoursDiff > 1) {
                result.setDescription(UNAUTHORIZED_RESET_PASSWORD_ATTEMPT);
            } else if (account.getPasswordResetCode().equals(passwordResetCode)) {
                em.merge(account);
                result.setResult(true);
                result.setDescription("Code verified");
                result.setID(account.getId());
            } else {
                result.setDescription("Invalid verification code, please try again.");
            }
        } catch (NoResultException ex) {
            result.setDescription(UNAUTHORIZED_EMAIL_VERIFICATION_ATTEMPT);
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: enterForgetPasswordCode() failed");
            ex.printStackTrace();
            result.setDescription("Unable to verify code because of an internal server error, please try again later.");
        }
        return result;
    }

    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static int getHoursDifference(Date date1, Date date2) {
        final int MILLI_TO_HOUR = 1000 * 60 * 60;
        return (int) (date1.getTime() - date2.getTime()) / MILLI_TO_HOUR;
    }

    @Override
    public String generatePasswordHash(String salt, String password) {
        String passwordHash = null;
        try {
//            password = salt + password;
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            md.update(password.getBytes());
//            byte[] bytes = md.digest();
//            StringBuilder sb = new StringBuilder();
//            for (int i = 0; i < bytes.length; i++) {
//                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
//            }
//            passwordHash = sb.toString();
            int iterations = 1000;
            char[] chars = password.toCharArray();
            byte[] saltInBytes = salt.getBytes();

            PBEKeySpec spec = new PBEKeySpec(chars, saltInBytes, iterations, 64 * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return iterations + ":" + toHex(saltInBytes) + ":" + toHex(hash);
        } catch (Exception ex) {
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
        //return Arrays.toString(salt);
        return salt.toString();
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    @Override
    public ReturnHelper updateMemberProfile(Long accountID, String newName, Part profilePicture) {
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
            if (profilePicture != null) {
                result = updateMemberProfilePicture(accountID, profilePicture);
                if (!result.getResult()) {
                    return result;
                }
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
        System.out.println("AccountManagementBean: updateMemberProfilePicture() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            Query q = em.createQuery("SELECT a FROM Account a where a.id=:accountID");
            q.setParameter("accountID", accountID);
            Account account = (Account) q.getSingleResult();
            Member member = null;
            if (account instanceof Member) {
                q = em.createQuery("SELECT m FROM Member m where m.id=:accountID");
                q.setParameter("accountID", accountID);
                member = (Member) q.getSingleResult();
            } else {
                result.setDescription("Internal server error, invalid account type.");
                return result;
            }
            String tempFileLocation = "temp/profilepic_" + account.getId() + ".jpg";
            if (profilePicture != null) {
                //Save file to local drive first
                InputStream fileInputStream = profilePicture.getInputStream();
                OutputStream fileOutputStream = new FileOutputStream(tempFileLocation);
                int nextByte;
                while ((nextByte = fileInputStream.read()) != -1) {
                    fileOutputStream.write(nextByte);
                }
                fileOutputStream.close();
                fileInputStream.close();
                //Upload to GCS
                String imageLocation = "/images/member/profile/profilepictures" + account.getId() + "/" + new Date();
                result = cibl.uploadFileToGoogleCloudStorage(imageLocation, tempFileLocation, true, true);
                //Delete away local file
                File file = new File(tempFileLocation);
                file.delete();
                if ((result != null)) {
                    if (result.getResult()) {
                        member.setImageURL(imageLocation);
                        em.merge(member);
                        result.setDescription("Profile picture updated.");
                        result.setResult(true);
                    } else {
                        result.setDescription("Failed to update profile picture, please try again later.");
                    }
                }
            }
        } catch (NoResultException ex) {
            System.out.println("AccountManagementBean: updateMemberProfilePicture() failed");
            result.setDescription("Account not found.");
            return result;
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: updateMemberProfilePicture() failed");
            result.setDescription("Update profille failed, internal server error.");
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public ReturnHelper updateArtistProfile(Long artistID, Long genreID, String biography, String influences, String contactEamil, String paypalEmail, String facebookURL, String instagramURL, String twitterURL, String websiteURL, Part profilePicture) {
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
            //Update other fields
            artist.setBiography(biography);
            artist.setInfluences(influences);
            artist.setContactEmail(contactEamil);
            artist.setPaypalEmail(paypalEmail);
            artist.setFacebookURL(facebookURL);
            artist.setInstagramURL(instagramURL);
            artist.setTwitterURL(twitterURL);
            artist.setWebsiteURL(websiteURL);
            em.merge(artist);
            if (profilePicture != null) {
                result = updateArtistProfilePicture(artistID, profilePicture);
                if (!result.getResult()) {
                    return result;
                }
            }
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
        System.out.println("AccountManagementBean: updateArtistProfilePicture() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            Query q = em.createQuery("SELECT a FROM Account a where a.id=:accountID");
            q.setParameter("accountID", artistID);
            Account account = (Account) q.getSingleResult();
            Artist artist = null;
            if (account instanceof Artist) {
                q = em.createQuery("SELECT m FROM Artist m where m.id=:accountID");
                q.setParameter("accountID", artistID);
                artist = (Artist) q.getSingleResult();
            } else {
                result.setDescription("Internal server error, invalid account type.");
                return result;
            }
            String tempFileLocation = "temp/profilepic_" + account.getId() + ".jpg";
            if (profilePicture != null) {
                //Save file to local drive first
                InputStream fileInputStream = profilePicture.getInputStream();
                OutputStream fileOutputStream = new FileOutputStream(tempFileLocation);
                int nextByte;
                while ((nextByte = fileInputStream.read()) != -1) {
                    fileOutputStream.write(nextByte);
                }
                fileOutputStream.close();
                fileInputStream.close();
                //Upload to GCS
                String imageLocation = "/images/artist/profile/profilepictures" + account.getId() + "/" + new Date();
                result = cibl.uploadFileToGoogleCloudStorage(imageLocation, tempFileLocation, true, true);
                //Delete away local file
                File file = new File(tempFileLocation);
                file.delete();
                if ((result != null)) {
                    if (result.getResult()) {
                        artist.setImageURL(imageLocation);
                        em.merge(artist);
                        result.setDescription("Profile picture updated.");
                        result.setResult(true);
                    } else {
                        result.setDescription("Failed to update profile picture, please try again later.");
                    }
                }
            }
        } catch (NoResultException ex) {
            System.out.println("AccountManagementBean: updateArtistProfilePicture() failed");
            result.setDescription("Account not found.");
            return result;
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: updateArtistProfilePicture() failed");
            result.setDescription("Update profille failed, internal server error.");
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    public ReturnHelper updateBandProfile(Long bandID, String members, Date dateFormed, Long genreID, String biography, String influences, String contactEamil, String paypalEmail, String facebookURL, String instagramURL, String twitterURL, String websiteURL, Part profilePicture) {
        System.out.println("AccountManagementBean: updateBandProfile() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            Query q = em.createQuery("SELECT e FROM Artist e WHERE e.id=:id");
            q.setParameter("id", bandID);
            Artist band = (Artist) q.getSingleResult();
            //Update old genre list
            Genre oldGenre = band.getGenre();
            if (oldGenre != null) {
                List<Artist> genreBand = oldGenre.getListOfArtists();
                genreBand.remove(band);
                oldGenre.setListOfArtists(genreBand);
                em.merge(oldGenre);
            }
            //Update new genre list
            q = em.createQuery("SELECT e FROM Genre e WHERE e.id=:id");
            q.setParameter("id", genreID);
            Genre newGenre = (Genre) q.getSingleResult();
            List<Artist> genreBands = newGenre.getListOfArtists();
            genreBands.add(band);
            em.merge(newGenre);
            //Update band genre
            band.setGenre(newGenre);
            //Update other fields
            band.setBandMembers(members);
            band.setBandDateFormed(dateFormed);
            band.setBiography(biography);
            band.setInfluences(influences);
            band.setContactEmail(contactEamil);
            band.setPaypalEmail(paypalEmail);
            band.setFacebookURL(facebookURL);
            band.setInstagramURL(instagramURL);
            band.setTwitterURL(twitterURL);
            band.setWebsiteURL(websiteURL);
            em.merge(band);
            if (profilePicture != null) {
                result = updateArtistProfilePicture(bandID, profilePicture);
                if (!result.getResult()) {
                    return result;
                }
            }
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
            if (!validatePassword(oldPassword, account.getPassword())) {
                result.setDescription("Old password provided is invalid, password not updated.");
            } else {
                account.setForgetPassword(false);
                account.setPassword(generatePasswordHash(generatePasswordSalt(), newPassword));
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
    public ReturnHelper updateAccountPassword(Long accountID, String newPassword) {
        System.out.println("AccountManagementBean: updateAccountPassword() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        Query q = em.createQuery("SELECT a FROM Account a WHERE a.id=:id");
        q.setParameter("id", accountID);
        try {
            Account account = (Account) q.getSingleResult();
            account.setPassword(generatePasswordHash(generatePasswordSalt(), newPassword));
            em.merge(account);
            result.setResult(true);
            result.setDescription("Password updated successfully.");
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
            if (account.getEmail().equalsIgnoreCase(newEmail)) {
                result.setDescription("New email is the same as your old email!");
                return result;
            }
            if (checkIfEmailExists(newEmail) || checkIfNewEmailExists(newEmail)) {
                result.setDescription("New email is in used by another user.");
                return result;
            }
            account.setNewEmail(newEmail);
            account.setNewEmailIsVerified(false);
            em.merge(account);
            ReturnHelper verificationCodeResult = generateAndSendVerificationEmail(accountID, newEmail, true);
            if (verificationCodeResult.getResult()) {
                result.setResult(true);
                result.setDescription("Account email verification sent.");
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

    @Override
    public ReturnHelper cancelUpdateAccountEmail(Long accountID) {
        System.out.println("AccountManagementBean: cancelUpdateAccountEmail() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        Query q = em.createQuery("SELECT s FROM Account s WHERE s.id=:id");
        q.setParameter("id", accountID);
        try {
            Account account = (Account) q.getSingleResult();
            account.setNewEmail("");
            account.setNewEmailIsVerified(false);
            account.setNewEmailVerificationCode(null);
            account.setNewEmailVerificationCodeGeneratedDate(null);
            em.merge(account);
            result.setResult(true);
            result.setDescription("Email change process cancelled.");
        } catch (NoResultException ex) {
            result.setDescription("Unable to find account with the provided ID.");
        } catch (Exception ex) {
            System.out.println("AccountManagementBean: cancelUpdateAccountEmail() failed");
            result.setDescription("Unable to cancel update account's email, internal server error.");
            ex.printStackTrace();
        }
        return result;
    }

}
