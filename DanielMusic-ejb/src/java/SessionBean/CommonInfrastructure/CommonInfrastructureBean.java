package SessionBean.CommonInfrastructure;

import EntityManager.ReturnHelper;
import EntityManager.StartupBean;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;
import com.google.api.services.storage.model.ObjectAccessControl;
import com.google.api.services.storage.model.StorageObject;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Part;
import net.sf.jmimemagic.Magic;
import org.apache.commons.codec.binary.Base64;

@Stateless
public class CommonInfrastructureBean implements CommonInfrastructureBeanLocal {

    private static final Logger log = Logger.getLogger(StartupBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    //GCS Upload
    private static final String APPLICATION_NAME = "soundssg";
    private static final String BUCKET_NAME = "sounds.sg";
    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".store/storage_sample");
    private static FileDataStoreFactory dataStoreFactory;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static HttpTransport httpTransport;
    private static Storage client;
    //GCS Download
    private static final String SERVICE_ACCOUNT_EMAIL = "238428794724-r6r603276h0gvud4mr1fb01rsfmhrp3n@developer.gserviceaccount.com";
    private static final String SERVICE_ACCOUNT_PKCS12_FILE_PATH = (System.getProperty("user.home") + "/DanielMusic/Credentials/SoundsSG-2dc89241334d.p12");
    //Windows is C:\Users\<user>\...
    //Linux is /home/admin/...

    @Override
    public ReturnHelper uploadFileToGoogleCloudStorage(String remoteDestinationFile, String localSourceFile, String downloadFilename, Boolean isImage, Boolean publiclyReadable) {
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        log.info("CommonInfrastructureBean: uploadFileToGoogleCloudStorage() called");
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
            Credential credential = authorize();
            client = new Storage.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
            File file = new File(localSourceFile);
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream inputStream = fileInputStream;
            //InputStream inputStream = part.getInputStream();
            String mimeType = "application/octet-stream";
            if (isImage) {
                mimeType = "image/jpeg";
            }
            InputStreamContent mediaContent = new InputStreamContent(mimeType, inputStream);
            mediaContent.setLength(file.length());
            StorageObject objectMetadata = new StorageObject();
            objectMetadata.setBucket(BUCKET_NAME);
            if (publiclyReadable) {
                List<ObjectAccessControl> accessControls = new ArrayList();
                ObjectAccessControl accessControl = new ObjectAccessControl();
                accessControl.setEntity("allUsers");
                accessControl.setRole("READER");
                accessControls.add(accessControl);
                //accessControl.set
                objectMetadata.setAcl(accessControls);
            }
            //Set the filename that the user gets when they download the file
            if (downloadFilename != null) {
                objectMetadata.setContentDisposition("filename=" + downloadFilename);
            }
            Storage.Objects.Insert insertObject = client.objects().insert(BUCKET_NAME, objectMetadata, mediaContent);
            insertObject.setName(remoteDestinationFile);
            if (mediaContent.getLength() > 0 && mediaContent.getLength() <= 2 * 1000 * 1000 /* 2MB */) {
                insertObject.getMediaHttpUploader().setDirectUploadEnabled(true);
            }
            insertObject.execute();
            result.setResult(true);
            result.setDescription("File uploaded.");
            return result;
        } catch (GoogleJsonResponseException ex) {
            log.info("CommonInfrastructureBean: uploadFileToGoogleCloudStorage() failed");
            result.setDescription("Unable to communicate with remote file server, please try again later.");
            log.info(ex.getDetails().toString());
            return result;
        } catch (Exception ex) {
            log.info("CommonInfrastructureBean: uploadFileToGoogleCloudStorage() failed");
            result.setDescription("Internal server error.");
            log.info(ex.getMessage());
            return result;
        }
    }

    @Override
    public String getFileURLFromGoogleCloudStorage(String filename, Long expirationInSeconds, String downloadFilename) {
        log.info("CommonInfrastructureBean: getMusicFileURLFromGoogleCloudStorage() called");
        try {
            PrivateKey pk = loadKeyFromPkcs12(SERVICE_ACCOUNT_PKCS12_FILE_PATH, "notasecret".toCharArray());
            //Encode filename
            filename = URLEncoder.encode(filename, "UTF-8").replace("+", "%20");
            String get_url = this.getSigningURL("GET", filename, pk, expirationInSeconds, downloadFilename);
            URL url = new URL(get_url);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setRequestMethod("GET");
            return get_url;
        } catch (Exception ex) {
            log.info("CommonInfrastructureBean: getMusicFileURLFromGoogleCloudStorage() failed");
            return null;
        }
    }

    @Override
    public String generateUUID() {
        UUID id = UUID.randomUUID();
        return id.toString();
    }

    // Google Cloud Storage Functions
    private static Credential authorize() throws Exception {
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(JSON_FACTORY)
                .setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
                .setServiceAccountPrivateKeyFromP12File(new File(SERVICE_ACCOUNT_PKCS12_FILE_PATH))
                .setServiceAccountScopes(Collections.singleton(StorageScopes.DEVSTORAGE_FULL_CONTROL))
                .setServiceAccountUser(null)
                .build();
        return credential;

    }

    private String getSigningURL(String verb, String filename, PrivateKey privateKey, Long expirationInSeconds, String downloadFilename) throws Exception {
        String url_signature = this.signString(verb + "\n\n\n" + (System.currentTimeMillis() / 1000 + expirationInSeconds) + "\n" + "/" + BUCKET_NAME + "/" + filename, privateKey);
        String signed_url = "https://storage.googleapis.com/" + BUCKET_NAME + "/" + filename
                + "?GoogleAccessId=" + SERVICE_ACCOUNT_EMAIL
                + "&Expires=" + (System.currentTimeMillis() / 1000 + expirationInSeconds)
                + "&Signature=" + URLEncoder.encode(url_signature, "UTF-8")
                + "&response-content-disposition=attachment;filename=\""+ downloadFilename +"\"";
        return signed_url;
    }

    private static PrivateKey loadKeyFromPkcs12(String filename, char[] password) throws Exception {
        FileInputStream fis = new FileInputStream(filename);
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(fis, password);
        return (PrivateKey) ks.getKey("privatekey", password);
    }

    private String signString(String stringToSign, PrivateKey privateKey) throws Exception {
        if (privateKey == null) {
            throw new Exception("Private Key not initalized");
        }
        Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(privateKey);
        signer.update(stringToSign.getBytes("UTF-8"));
        byte[] rawSignature = signer.sign();
        return new String(Base64.encodeBase64(rawSignature, false), "UTF-8");
    }

    @Override
    public ReturnHelper deleteFileFromGoogleCloudStorage(String remoteDestinationFile) {
        log.info("CommonInfrastructureBean: deleteFileFromGoogleCloudStorage() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
            Credential credential = authorize();
            client = new Storage.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
            Storage.Objects.Delete deleteObject = client.objects().delete(BUCKET_NAME, remoteDestinationFile);
            deleteObject.execute();
            result.setDescription("File deleted.");
            result.setResult(true);
            return result;
        } catch (GoogleJsonResponseException ex) {
            if (ex.getDetails().getCode() == 404) {
                result.setDescription("Remote file not found.");
                return result;
            } else {
                result.setDescription("Unable to communicate with remote file server, please try again later.");
                return result;
            }
        } catch (Exception ex) {
            log.info("CommonInfrastructureBean: deleteFileFromGoogleCloudStorage() failed");
            result.setDescription("Internal server error.");
            log.info(ex.getMessage());
            return result;
        }
    }

    @Override
    public ReturnHelper checkIfImageFitsRequirement(String filename) {
        log.info("checkIfImageFitsRequirement() called");
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        try {
            File file = new File(filename);
            BufferedImage bimg = ImageIO.read(file);
            int width = bimg.getWidth();
            int height = bimg.getHeight();
            if (width != height) {
                result.setDescription("Please upload a square image.");
            } else if (width < 300 || height < 300) {
                result.setDescription("Image resolution must be at least 300x300px.");
            } else {
                String mimeType = Magic.getMagicMatch(file, false).getMimeType();
                if (!mimeType.equals("image/png") && !mimeType.equals("image/jpeg")) {
                    result.setDescription("Image must be in either PNG or JPEG format.");
                } else {
                    result.setDescription("Image fits requirements");
                    result.setResult(true);
                }
            }
        } catch (Exception ex) {
            result.setDescription("Unable to check image requirements due to internal server error.");
            ex.printStackTrace();
        }
        return result;
    }

    public String getSubmittedFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('/') + 1).substring(fileName.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }
}
