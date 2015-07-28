package SessionBean.CommonInfrastructure;

import EntityManager.ReturnHelper;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.extensions.java6.auth.oauth2.GooglePromptReceiver;
import com.google.api.services.storage.StorageScopes;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.codec.binary.Base64;

@Stateless
public class CommonInfrastructureBean implements CommonInfrastructureBeanLocal {

    @PersistenceContext
    private EntityManager em;

    //GCS Upload
    private static final String APPLICATION_NAME = "master-deck-101807";
    private static final String BUCKET_NAME = "sounds.sg";
    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".store/storage_sample");
   private static FileDataStoreFactory dataStoreFactory;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static HttpTransport httpTransport;
    private static Storage client;
    //GCS Download
    private static final String SERVICE_ACCOUNT_EMAIL = "1059174637321-nthr5hhjfq7qc7979ansq0af0gi76d8q@developer.gserviceaccount.com";
    private static final String SERVICE_ACCOUNT_PKCS12_FILE_PATH = (System.getProperty("user.home")+"/DanielMusic/Credentials/SoundsSG-ee074d7c276d.p12");
    //Windows is C:\Users\<user>\...
    //Linux is /home/admin/...

    @Override
    public ReturnHelper uploadFileToGoogleCloudStorage(String remoteDestinationFile, String localSourceFile, Boolean isImage, Boolean publiclyReadable) {
        ReturnHelper result = new ReturnHelper();
        result.setResult(false);
        System.out.println("CommonInfrastructureBean: uploadFileToGoogleCloudStorage() called");
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
            System.out.println("CommonInfrastructureBean: uploadFileToGoogleCloudStorage() failed");
            result.setDescription("Unable to communicate with remote file server, please try again later.");
            System.out.println(ex.getDetails().toString());
            return result;
        } catch (Exception ex) {
            System.out.println("CommonInfrastructureBean: uploadFileToGoogleCloudStorage() failed");
            result.setDescription("Internal server error.");
            ex.printStackTrace();
            return result;
        }
    }

    @Override
    public String getFileURLFromGoogleCloudStorage(String filename, Long expirationInSeconds) {
        System.out.println("CommonInfrastructureBean: getMusicFileURLFromGoogleCloudStorage() called");
        try {
            PrivateKey pk = loadKeyFromPkcs12(SERVICE_ACCOUNT_PKCS12_FILE_PATH, "notasecret".toCharArray());
            String get_url = this.getSigningURL("GET", filename, pk, expirationInSeconds);
            URL url = new URL(get_url);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setRequestMethod("GET");
            return get_url;
        } catch (Exception ex) {
            System.out.println("CommonInfrastructureBean: getMusicFileURLFromGoogleCloudStorage() failed");
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

    private String getSigningURL(String verb, String filename, PrivateKey privateKey, Long expirationInSeconds) throws Exception {
        String url_signature = this.signString(verb + "\n\n\n" + (System.currentTimeMillis() / 1000 + expirationInSeconds) + "\n" + "/" + BUCKET_NAME + "/" + filename, privateKey);
        String signed_url = "https://storage.googleapis.com/" + BUCKET_NAME + "/" + filename
                + "?GoogleAccessId=" + SERVICE_ACCOUNT_EMAIL
                + "&Expires=" + (System.currentTimeMillis() / 1000 + expirationInSeconds)
                + "&Signature=" + URLEncoder.encode(url_signature, "UTF-8");
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
        System.out.println("CommonInfrastructureBean: deleteFileFromGoogleCloudStorage() called");
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
            System.out.println("CommonInfrastructureBean: deleteFileFromGoogleCloudStorage() failed");
            result.setDescription("Internal server error.");
            ex.printStackTrace();
            return result;
        }
    }
}
