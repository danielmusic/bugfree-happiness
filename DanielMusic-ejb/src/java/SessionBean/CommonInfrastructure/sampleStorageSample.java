package SessionBean.CommonInfrastructure;
/*
 * Copyright (c) 2014 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.googleapis.extensions.java6.auth.oauth2.GooglePromptReceiver;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;
import com.google.api.services.storage.model.Bucket;
import com.google.api.services.storage.model.ObjectAccessControl;
import com.google.api.services.storage.model.StorageObject;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.Part;

/**
 * Main class for the Cloud Storage API command line sample. Demonstrates how to
 * make an authenticated API call using OAuth 2 helper classes.
 */
public class sampleStorageSample {

    /**
     * Be sure to specify the name of your application. If the application name
     * is {@code null} or blank, the application will log a warning. Suggested
     * format is "MyCompany-ProductName/1.0". If you are running the sample on a
     * machine where you have access to a browser, set AUTH_LOCAL_WEBSERVER to
     * true.
     */
    private static final String APPLICATION_NAME = "divine-apogee-96116";
    private static final String BUCKET_NAME = "danielmusictest";
    private static final String CLIENT_SECRET_FILENAME = "Credentials/client_secret_905886242502-22rdr9lsk2d0k15n193ajssf6fpctlrc.apps.googleusercontent.com.json";
    private static final boolean AUTH_LOCAL_WEBSERVER = false;

    /**
     * Directory to store user credentials.
     */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".store/storage_sample");

    /**
     * Global instance of the {@link DataStoreFactory}. The best practice is to
     * make it a single globally shared instance across your application.
     */
    private static FileDataStoreFactory dataStoreFactory;

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport httpTransport;
    
    private static Storage client;

    /**
     * Authorizes the installed application to access user's protected data.
     */
    private static Credential authorize() throws Exception {
        // Load client secrets.
        GoogleClientSecrets clientSecrets = null;
        try {
            clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                    new InputStreamReader(sampleStorageSample.class.getResourceAsStream(String.format("/%s", CLIENT_SECRET_FILENAME))));
            if (clientSecrets.getDetails().getClientId() == null
                    || clientSecrets.getDetails().getClientSecret() == null) {
                throw new Exception("client_secrets not well formed.");
            }
        } catch (Exception e) {
            System.out.println("Problem loading client_secrets.json file. Make sure it exists, you are "
                    + "loading it with the right path, and a client ID and client secret are "
                    + "defined in it.\n" + e.getMessage());
            System.exit(1);
        }

    // Set up authorization code flow.
        // Ask for only the permissions you need. Asking for more permissions will
        // reduce the number of users who finish the process for giving you access
        // to their accounts. It will also increase the amount of effort you will
        // have to spend explaining to users what you are doing with their data.
        // Here we are listing all of the available scopes. You should remove scopes
        // that you are not actually using.
        Set<String> scopes = new HashSet<String>();
        scopes.add(StorageScopes.DEVSTORAGE_FULL_CONTROL);
        scopes.add(StorageScopes.DEVSTORAGE_READ_ONLY);
        scopes.add(StorageScopes.DEVSTORAGE_READ_WRITE);
        
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, scopes)
                .setDataStoreFactory(dataStoreFactory)
                .build();
        // Authorize.
        VerificationCodeReceiver receiver
                = AUTH_LOCAL_WEBSERVER ? new LocalServerReceiver() : new GooglePromptReceiver();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");        
    }
    
    public static void main(String[] args) {
        try {
            // Initialize the transport.
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();

            // Initialize the data store factory.
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);

            // Authorization.
            Credential credential = authorize();

            // Set up global Storage instance.
            client = new Storage.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();

            // Get metadata about the specified bucket.
            Storage.Buckets.Get getBucket = client.buckets().get(BUCKET_NAME);
            getBucket.setProjection("full");
            Bucket bucket = getBucket.execute();
            System.out.println("name: " + BUCKET_NAME);
            System.out.println("location: " + bucket.getLocation());
            System.out.println("timeCreated: " + bucket.getTimeCreated());
            System.out.println("owner: " + bucket.getOwner());

      //Upload file
            // Given
            String filename = "output.mp3";
            File file = new File("temp/test2.jpg");
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream inputStream = fileInputStream;  // object data, e.g., FileInputStream
            //InputStream inputStream = part.getInputStream();  // object data, e.g., FileInputStream
            long byteCount = file.length();  // size of input stream

            //InputStreamContent mediaContent = new InputStreamContent("application/octet-stream", inputStream);
            //image/jpeg
            //audio/mpeg3
            InputStreamContent mediaContent = new InputStreamContent("audio/mpeg3", inputStream);
        // Knowing the stream length allows server-side optimization, and client-side progress
            // reporting with a MediaHttpUploaderProgressListener.
            mediaContent.setLength(byteCount);
            
            StorageObject objectMetadata = new StorageObject();
            boolean useCustomMetadata = false;
            
            if (useCustomMetadata) {
          // If you have custom settings for metadata on the object you want to set
                // then you can allocate a StorageObject and set the values here. You can
                // leave out setBucket(), since the bucket is in the insert command's
                // parameters.
                objectMetadata = new StorageObject()
                        .setName(filename)
                        .setMetadata(ImmutableMap.of("key1", "value1", "key2", "value2"))
                        .setAcl(ImmutableList.of(
                                        new ObjectAccessControl().setEntity("domain-example.com").setRole("READER"),
                                        new ObjectAccessControl().setEntity("905886242502-qo2e7vbkej1tgpvj4trp278iusii11k9@developer.gserviceaccount.com").setRole("OWNER")
                                ))
                        .setContentDisposition("attachment");
            }
            objectMetadata.setBucket(BUCKET_NAME);
            Storage.Objects.Insert insertObject = client.objects().insert(BUCKET_NAME, objectMetadata,
                    mediaContent);
            
            if (!useCustomMetadata) {
          // If you don't provide metadata, you will have specify the object
                // name by parameter. You will probably also want to ensure that your
                // default object ACLs (a bucket property) are set appropriately:
                // https://developers.google.com/storage/docs/json_api/v1/buckets#defaultObjectAcl
                insertObject.setName(filename);
                
            }

        // For small files, you may wish to call setDirectUploadEnabled(true), to
            // reduce the number of HTTP requests made to the server.
            if (mediaContent.getLength() > 0 && mediaContent.getLength() <= 2 * 1000 * 1000 /* 2MB */) {
                insertObject.getMediaHttpUploader().setDirectUploadEnabled(true);
            }
            
            insertObject.execute();

            // List the contents of the bucket.
            Storage.Objects.List listObjects = client.objects().list(BUCKET_NAME);
            com.google.api.services.storage.model.Objects objects;
            do {
                objects = listObjects.execute();
                List<StorageObject> items = objects.getItems();
                if (null == items) {
                    System.out.println("There were no objects in the given bucket; try adding some and re-running.");
                    break;
                }
                for (StorageObject object : items) {
                    System.out.println(object.getName() + " (" + object.getSize() + " bytes)");
                }
                listObjects.setPageToken(objects.getNextPageToken());
            } while (null != objects.getNextPageToken());
            
            //Delete
            //Storage.Objects.Delete deleteObject = client.objects().delete(BUCKET_NAME, objectName)
            //https://developers.google.com/resources/api-libraries/documentation/storage/v1/java/latest/com/google/api/services/storage/Storage.Objects.Delete.html#Storage.Objects.Delete(java.lang.String,%20java.lang.String)
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.exit(1);
    }
}
