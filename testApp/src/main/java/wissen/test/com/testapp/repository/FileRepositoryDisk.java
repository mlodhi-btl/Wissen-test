package wissen.test.com.testapp.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import wissen.test.com.testapp.exception.CustomFileNotFoudException;
import wissen.test.com.testapp.exception.FileStorageException;

/*
 * This class will perform read/write operation  on the data drive on server, where the application is running 
 */
@Repository
public class FileRepositoryDisk {
	

	/*
	 * read the file/data
	 */
		
	public Resource loadFileAsResource(Path filePath) throws CustomFileNotFoudException {
        try {
            
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new CustomFileNotFoudException("File not found " + filePath.getFileName());
            }
        } catch (MalformedURLException ex) {
            throw new CustomFileNotFoudException("File not found " + filePath.getFileName());
        }
    }
 
	
		 /**
	     * This method uses apache commons to read
	     * file content into a byte array
	     * @param file
	     */
	    public  void copyByteArray(File file, FileOutputStream fos){
	        try(FileInputStream fis = new FileInputStream(file)) {

	            byte[] bArray = IOUtils.toByteArray(fis);
	            fos.write(bArray);
	           
	        } catch (IOException e) {
	            e.printStackTrace();
	        }        
	    }
	/*
	 * write the file/data 
	 */
	
	public void writeFile() {
		
	}
	
	
	public void write(MultipartFile file, File tempFile) {
		
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		  try { 
			  InputStream is = file.getInputStream();
		  
		  Path targetLocation = Paths.get(tempFile.getAbsolutePath());
		  
		  Files.copy(is, targetLocation, StandardCopyOption.REPLACE_EXISTING);
		  
		  
		   is.close(); 
		  } 
		  catch (IOException ex) { // TODO Auto-generated catch block
		  throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex); 
		  
		  }
		

	}

}
