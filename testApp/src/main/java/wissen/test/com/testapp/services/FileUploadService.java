package wissen.test.com.testapp.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import wissen.test.com.testapp.config.FileConfiguration;
import wissen.test.com.testapp.exception.FileStorageException;
import wissen.test.com.testapp.payload.UploadFileResponse;
import wissen.test.com.testapp.repository.FileRepositoryDisk;

/*
 * this is file to upload the data
 */
@Configuration
@Service

public class FileUploadService {

	public static FileConfiguration fileConfiguration;

	private static Integer Max_part_number = Integer.MAX_VALUE;

	private static Integer part_counter = 0;

	private final Path fileStorageLocation;
	
	public   AtomicBoolean flag = new AtomicBoolean(false);

	@Autowired
	FileRepositoryDisk fileRepository;
	@Autowired
	public FileUploadService(FileConfiguration fileConfiguration) {
		this.fileStorageLocation = Paths.get(fileConfiguration.getUploadDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
					ex);
		}
	}

	public boolean upload(MultipartFile file, String fileName, int part_number, boolean EOF) {

		if (part_counter < Max_part_number) {
			if (EOF)
				Max_part_number = part_number;
			part_counter++;
			
		}else {
			flag.set(true);
		}
		writeFile(file, fileName, part_number);
       if(flag.get())
		return true;
       else
    	  return false;
	}

	public void writeFile(MultipartFile file, String fileName, int part_number) {

		String PATH = this.fileStorageLocation.toString();
		String directoryName = PATH.concat(File.separator + fileName);
		String filePart = fileName + "-" + part_number;
		File directory = new File(directoryName);
		if (!directory.exists()) {
			directory.mkdir();
		}

		File tempFile = new File(directoryName + "/" + filePart);
		/*
		 * Delegate the call File Repository to write to File
		 */
		fileRepository.write(file, tempFile);

	}

		
	public UploadFileResponse aggregateChunks(String fileName) {
		
		String PATH = this.fileStorageLocation.toString();
		String inputDirectoryName = PATH.concat(File.separator + fileName);
		String outputDirectoryName = PATH.concat(File.separator + "result");
			File inputDir = new File(inputDirectoryName);
			File outputDir= new File(outputDirectoryName);
			if(!outputDir.exists()) {
				outputDir.mkdirs();
			}
			File outputFile=new File(outputDir.getAbsoluteFile()+ File.separator+fileName);
			       if(outputFile.exists())
			    	outputFile.delete();
			   
				try {
					outputFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			if(inputDir.isDirectory()) {
				
				File[] ff=inputDir.listFiles();
				Collections.sort(Arrays.asList(ff));
				
				try {
					FileOutputStream fos = new FileOutputStream(outputFile,true);
					/*
					 * copy all the intermediate chunk of data from temporary storage to Single large file
					 */
					for(File f:ff)
					 fileRepository.copyByteArray(f,fos);
					try {
						fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//String fileName = fileStorageService.storeFile(file);

	        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
	                .path("/testApp/downloadFile/")
	                .path(fileName)
	                .toUriString();

	        return new UploadFileResponse(fileName, fileDownloadUri);
	    
		
		
		//return null;
	}
	}
