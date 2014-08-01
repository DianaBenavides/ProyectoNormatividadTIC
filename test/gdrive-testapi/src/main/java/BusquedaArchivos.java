import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BusquedaArchivos {
  
  private static final String SERVICE_ACCOUNT_EMAIL = "702654314965-t0p2hkp97c9hhviknb5d0g9dkk0o6m7d@developer.gserviceaccount.com";
  private static final String SERVICE_ACCOUNT_PKCS12_FILE_PATH = "Test DRIVE API-eb59aaa02cde.p12";

 
  
  public static Drive getDriveService() throws GeneralSecurityException,
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
  
  private static List<File> retrieveAllFiles(Drive service) throws IOException {
	    List<File> result = new ArrayList<File>();
	    Files.List request = service.files().list();

	    do {
	      try {
	        FileList files = request.execute();

	        result.addAll(files.getItems());
	        request.setPageToken(files.getNextPageToken());
	      } catch (IOException e) {
	        System.out.println("An error occurred: " + e);
	        request.setPageToken(null);
	      }
	    } while (request.getPageToken() != null &&
	             request.getPageToken().length() > 0);

	    return result;
	  }
  
  private static List<File> retrieveFilesByTitle(Drive service, String title) throws IOException {
	    List<File> result = new ArrayList<File>();
	    Files.List request = service.files().list().setQ("title contains '"+title+"'");

	    do {
	      try {
	        FileList files = request.execute();

	        result.addAll(files.getItems());
	        request.setPageToken(files.getNextPageToken());
	      } catch (IOException e) {
	        System.out.println("An error occurred: " + e);
	        request.setPageToken(null);
	      }
	    } while (request.getPageToken() != null &&
	             request.getPageToken().length() > 0);

	    return result;
	  }
  
  private static List<File> retrieveFilesByProperty(Drive service, String property, String value) throws IOException {
	    List<File> result = new ArrayList<File>();
	    Files.List request = service.files().list().setQ("properties has { key='"+property+"' and value contains '"+value+"' and visibility='PUBLIC'}");

	    do {
	      try {
	        FileList files = request.execute();

	        result.addAll(files.getItems());
	        request.setPageToken(files.getNextPageToken());
	      } catch (IOException e) {
	        System.out.println("An error occurred: " + e);
	        request.setPageToken(null);
	      }
	    } while (request.getPageToken() != null &&
	             request.getPageToken().length() > 0);

	    return result;
	  }
  
  private static List<File> retrieveFilesByContent(Drive service, String content) throws IOException {
	    List<File> result = new ArrayList<File>();
	    Files.List request = service.files().list().setQ("fullText contains '"+content+"'");

	    do {
	      try {
	        FileList files = request.execute();

	        result.addAll(files.getItems());
	        request.setPageToken(files.getNextPageToken());
	      } catch (IOException e) {
	        System.out.println("An error occurred: " + e);
	        request.setPageToken(null);
	      }
	    } while (request.getPageToken() != null &&
	             request.getPageToken().length() > 0);

	    return result;
	  }
  public static void main(String[] args) throws IOException {

	  Drive service = null;
	  
	  try {
		  service = BusquedaArchivos.getDriveService();
	  } catch (GeneralSecurityException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
	  } catch (URISyntaxException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
	  }

	  String query = "keyWord";
	  List<File> files = retrieveFilesByProperty(service, query, "TIPO");

	  for (File file : files) {
		  System.out.println(file.getId() + "- " + file.getTitle());
	  }     
	   
  }
}