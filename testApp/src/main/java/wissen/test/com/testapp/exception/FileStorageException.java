package wissen.test.com.testapp.exception;
/* 
 * This is just the illustration to define the custom exception.
 * This is one of unchecked exception example. if disk is crash we cannot  recover the data. 
 * 
 */
public class FileStorageException extends RuntimeException {

	/**
	 *  
	 */
	private static final long serialVersionUID = 5694346794221202606L;
	
	public FileStorageException(String message) {
		super(message);
	}
	
	public FileStorageException(String message, Throwable throwable) {
		
		super(message, throwable);
	}

}
