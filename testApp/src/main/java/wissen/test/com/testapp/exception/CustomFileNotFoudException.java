package wissen.test.com.testapp.exception;

import java.io.FileNotFoundException;
/*
 * This is just the illustration to define the custom exception.
 * 
 *this is checked exception. on this exception, we can retry with other option.
 */
public class CustomFileNotFoudException  extends FileNotFoundException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CustomFileNotFoudException(String message) {
		super(message);
		
		System.out.println("this is File not found argumented  custom exeception ");
	}
	public CustomFileNotFoudException() {
		super();
		System.out.println("Custom File Not Found default custome exception ");
	}
	

}
