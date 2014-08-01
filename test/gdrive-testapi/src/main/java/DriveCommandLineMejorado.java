import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential;
import com.google.api.client.googleapis.services.CommonGoogleClientRequestInitializer;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Properties;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import com.google.api.services.drive.model.Property;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class DriveCommandLineMejorado {
  
  /** The API Key of the project */
  private static final String API_KEY = "AIzaSyDi2E7MURvvMdE4GHmiIFUftE900-FSfzA";
  private static final String SERVICE_ACCOUNT_EMAIL = "702654314965-t0p2hkp97c9hhviknb5d0g9dkk0o6m7d@developer.gserviceaccount.com";
  private static final String SERVICE_ACCOUNT_PKCS12_FILE_PATH = "Test DRIVE API-eb59aaa02cde.p12";

  /**
   * Build and returns a Drive service object authorized with the
   * application's service accounts.
   *
   * @return Drive service object that is ready to make requests.
   */
  public static Drive getDriveService() throws GeneralSecurityException,
      IOException, URISyntaxException {
	
    HttpTransport httpTransport = new NetHttpTransport();
    JsonFactory jsonFactory = new JacksonFactory();
    AppIdentityCredential credential =
        new AppIdentityCredential.Builder(Arrays.asList(DriveScopes.DRIVE)).build();
    //AppIdentityCredential credential = new AppIdentityCredential.Builder().build()
    
    GoogleClientRequestInitializer keyInitializer =
        new CommonGoogleClientRequestInitializer(API_KEY);
    Drive service = new Drive.Builder(httpTransport, jsonFactory, null)
        .setHttpRequestInitializer(credential)
        .setGoogleClientRequestInitializer(keyInitializer)
        .setApplicationName("vibrant-shell-639@appspot.gserviceaccount.com ")
        .build();
    return service;
  }
  
  public static Drive getDriveServiceEmail() throws GeneralSecurityException,
  IOException, URISyntaxException {
HttpTransport httpTransport = new NetHttpTransport();
JacksonFactory jsonFactory = new JacksonFactory();
GoogleCredential credential = new GoogleCredential.Builder()
    .setTransport(httpTransport)
    .setJsonFactory(jsonFactory)
    .setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
    .setServiceAccountScopes(Arrays.asList(DriveScopes.DRIVE))
    .setServiceAccountPrivateKeyFromP12File(
          new java.io.File(SERVICE_ACCOUNT_PKCS12_FILE_PATH))
    .build();
Drive service = new Drive.Builder(httpTransport, jsonFactory, null)
    .setHttpRequestInitializer(credential)
    .setApplicationName("vibrant-shell-639@appspot.gserviceaccount.com ")
    .build();

return service;
}
  
  public static void main(String[] args) throws IOException {

	    Drive service = null;
		try {
			service = DriveCommandLineMejorado.getDriveServiceEmail();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Getting Folders from the DRIVE
		List<File> files = service.files().list().setQ("mimeType = 'application/vnd.google-apps.folder'").execute().getItems();

		File f  =files.get(0);//getting first file from the folder list
		
		//for (File file : files) {
			//ystem.out.println(file.getId() + "- " + file.getTitle());
		//}
	    //Insert a file  
	   File body = new File();
	    body.setTitle("Decreto 2888 de 2009");
	    body.setDescription("Decreto 2888 de 2009");
	    body.setParents(Arrays.asList(new ParentReference().setId(f.getId())));
	    body.setMimeType("application/pdf");
	    Property prop1 = new Property();
	    prop1.setKey("keyWord");
	    prop1.setValue("tipo circular");
	    prop1.setVisibility("PUBLIC");
	    List<Property> listaProp = new ArrayList<Property>();
	    listaProp.add(prop1);
	    body.setProperties(listaProp);
			
			
	    
	    java.io.File fileContent = new java.io.File("Decreto 2888 de 2009.pdf");
	    FileContent mediaContent = new FileContent("application/pdf", fileContent);

	    File file = service.files().insert(body, mediaContent).execute();
	    System.out.println("File ID: " + file.getId());
  }
}