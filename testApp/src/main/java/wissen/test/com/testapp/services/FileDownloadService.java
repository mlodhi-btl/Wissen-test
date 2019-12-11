package wissen.test.com.testapp.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import wissen.test.com.testapp.config.FileConfiguration;
import wissen.test.com.testapp.exception.CustomFileNotFoudException;
import wissen.test.com.testapp.exception.FileStorageException;
import wissen.test.com.testapp.repository.FileRepositoryDisk;

/*
 * this service is to download the file/data
 */
@Service
public class FileDownloadService {
@Autowired
FileRepositoryDisk fileRepository;
	private Path fileStorageLocation;
   private static final Logger logger =LoggerFactory.getLogger(FileDownloadService.class);
	@Autowired
	public FileDownloadService(FileConfiguration fileConfiguration) {
		this.fileStorageLocation = Paths.get(fileConfiguration.getUploadLocation()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
					ex);
		}
	}
	 
	public ResponseEntity<Resource> downLoadFile(String fileName, HttpServletRequest request) throws CustomFileNotFoudException{
		 
		 Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
		// Load file as Resource
         Resource resource = fileRepository.loadFileAsResource(filePath);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
	}
}
