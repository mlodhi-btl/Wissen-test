package wissen.test.com.testapp;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;	
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import wissen.test.com.testapp.exception.CustomFileNotFoudException;
import wissen.test.com.testapp.payload.ChunkRespose;
import wissen.test.com.testapp.payload.UploadFileResponse;
import wissen.test.com.testapp.services.FileDownloadService;
import wissen.test.com.testapp.services.FileUploadService;

@RestController
@RequestMapping("/testApp")
public class testAppController {
	
	@Autowired
	FileUploadService uploadservice;
	
	@Autowired
	FileDownloadService fileDownloadService;
	
	
	private static Logger logger = LoggerFactory.getLogger(testAppController.class);
	
	@GetMapping("/index")
	public ModelAndView displayHome() {
		ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("index");
	    return modelAndView;
		
	}
	
	
	@GetMapping("/upload")
	public ModelAndView uploadFile() {
		ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("uploadFile");
	    return modelAndView;
		
	}
	@PostMapping(value ="/success", consumes = MediaType.ALL_VALUE)
	
	public ChunkRespose fileUpload(@RequestParam("f1") MultipartFile file, @RequestParam("EOF") boolean EOF, @RequestParam("part_number") int part_number, @RequestParam("file") String fileName) throws IOException {   
		
		
		if (uploadservice.upload(file, fileName, part_number, EOF)) {

			return new ChunkRespose(fileName, part_number, "written on temp storage");
		}
		else {
			return new ChunkRespose(fileName, part_number, "failed");
		}
		
	}
	

	
	@GetMapping(value="/success/download/{fileName}")
	public UploadFileResponse prepareForDownLoad(@PathVariable String fileName) {
		return uploadservice.aggregateChunks(fileName);
		
	}
	
	@GetMapping(value="/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> dowloadFile(@PathVariable String fileName, HttpServletRequest request) throws CustomFileNotFoudException {
		return  fileDownloadService.downLoadFile(fileName, request);
		

    }
	
}
