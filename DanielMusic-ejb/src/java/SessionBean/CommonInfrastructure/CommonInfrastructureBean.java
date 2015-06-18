package SessionBean.CommonInfrastructure;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.extensions.java6.auth.oauth2.GooglePromptReceiver;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;
import com.google.api.services.storage.model.StorageObject;
import com.sendgrid.SendGrid;
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
import java.util.HashSet;
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
    private static final String APPLICATION_NAME = "divine-apogee-96116";
    private static final String BUCKET_NAME = "danielmusictest";
    private static final String CLIENT_SECRET_FILENAME = "C:\\Credentials\\client_secret_905886242502-22rdr9lsk2d0k15n193ajssf6fpctlrc.apps.googleusercontent.com.json";
    private static final boolean AUTH_LOCAL_WEBSERVER = false;
    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".store/storage_sample");
    private static FileDataStoreFactory dataStoreFactory;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static HttpTransport httpTransport;
    private static Storage client;
    //GCS Download
    private static final String SERVICE_ACCOUNT_EMAIL = "905886242502-8jv7f7qopknh74kmjb1ka4vdnrvk6no1@developer.gserviceaccount.com";
    private static final String SERVICE_ACCOUNT_PKCS12_FILE_PATH = "C:\\Credentials\\DanielMusic-1536a289ea67.p12";
    private static final long expiration = 60;//60s

    @Override
    public Boolean sendEmail(String destinationEmail, String senderEmail, String subject, String message) {
        System.out.println("CommonInfrastructureBean: sendEmail() called");
        try {
            SendGrid sendgrid = new SendGrid("sendgrid_api_key");
            SendGrid.Email email = new SendGrid.Email();
            email.addTo(destinationEmail);
            email.setFrom(senderEmail);
            email.setSubject(subject);
            email.setText(message);
            sendgrid.send(email);
            return true;
        } catch (Exception ex) {
            System.out.println("CommonInfrastructureBean: sendEmail() failed");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean uploadFileToGoogleCloudStorage(String remoteDestinationFile, String localSourceFile, Boolean isImage) {
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
            Storage.Objects.Insert insertObject = client.objects().insert(BUCKET_NAME, objectMetadata, mediaContent);
            insertObject.setName(remoteDestinationFile);
            if (mediaContent.getLength() > 0 && mediaContent.getLength() <= 2 * 1000 * 1000 /* 2MB */) {
                insertObject.getMediaHttpUploader().setDirectUploadEnabled(true);
            }
            insertObject.execute();
            return true;
        } catch (Exception ex) {
            System.out.println("CommonInfrastructureBean: uploadFileToGoogleCloudStorage() failed");
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public String getMusicFileURLFromGoogleCloudStorage(String filename) {
        System.out.println("CommonInfrastructureBean: getMusicFileURLFromGoogleCloudStorage() called");
        try {
            PrivateKey pk = loadKeyFromPkcs12(SERVICE_ACCOUNT_PKCS12_FILE_PATH, "notasecret".toCharArray());
            String get_url = this.getSigningURL("GET", filename, pk);
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
        GoogleClientSecrets clientSecrets = null;
        try {
            clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                    new InputStreamReader(sampleStorageSample.class.getResourceAsStream(String.format("/%s", CLIENT_SECRET_FILENAME))));
            if (clientSecrets.getDetails().getClientId() == null || clientSecrets.getDetails().getClientSecret() == null) {
                throw new Exception("client_secrets not well formed.");
            }
        } catch (Exception e) {
            System.out.println("Problem loading GCS client_secrets.json file. Make sure it exists, you are "
                    + "loading it with the right path, and a client ID and client secret are "
                    + "defined in it.\n" + e.getMessage());
            System.exit(1);
        }
        Set<String> scopes = new HashSet<String>();
        scopes.add(StorageScopes.DEVSTORAGE_FULL_CONTROL);
        scopes.add(StorageScopes.DEVSTORAGE_READ_ONLY);
        scopes.add(StorageScopes.DEVSTORAGE_READ_WRITE);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, scopes)
                .setDataStoreFactory(dataStoreFactory)
                .build();
        VerificationCodeReceiver receiver = AUTH_LOCAL_WEBSERVER ? new LocalServerReceiver() : new GooglePromptReceiver();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private String getSigningURL(String verb, String filename, PrivateKey privateKey) throws Exception {
        String url_signature = this.signString(verb + "\n\n\n" + (System.currentTimeMillis() / 1000 + expiration) + "\n" + "/" + BUCKET_NAME + "/" + filename, privateKey);
        String signed_url = "https://storage.googleapis.com/" + BUCKET_NAME + "/" + filename
                + "?GoogleAccessId=" + SERVICE_ACCOUNT_EMAIL
                + "&Expires=" + (System.currentTimeMillis() / 1000 + expiration)
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
    public Boolean deleteFileFromGoogleCloudStorage(String remoteDestinationFile) {
        System.out.println("CommonInfrastructureBean: deleteFileFromGoogleCloudStorage() called");
        try {

            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
            Credential credential = authorize();
            client = new Storage.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
            Storage.Objects.Delete deleteObject = client.objects().delete(BUCKET_NAME, remoteDestinationFile);
            deleteObject.execute();
            return true;
        } catch (Exception ex) {
            System.out.println("CommonInfrastructureBean: deleteFileFromGoogleCloudStorage() failed");
            return null;
        }
    }
}
