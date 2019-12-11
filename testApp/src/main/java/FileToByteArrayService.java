import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
public class FileToByteArrayService {

    public static void main(String[] args) {
    	
    	
        File file = new File("test"); 
        if(!file.exists()) {
        	file.mkdir();
        }
        
        File output= new File("output//result");
        if(!output.exists()) {
        	output.mkdir();
        }
        
        
        File[] files=file.listFiles();
        
        for(File f: files) {
        // Using ApacheCommons methods
        File out = new File(output.getPath()+File.separator+f.getName().substring(1, f.getName().indexOf("-")));
        if(!out.exists()) {
        	try {
				out.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        try {
			FileOutputStream fos = new FileOutputStream(out,true);
			 readToByteArrayUsingCommons(f,fos); 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        }
    }
    
    /**
     * This method uses apache commons to read
     * file content into a byte array
     * @param file
     */
    private static void readToByteArrayUsingCommons(File file, FileOutputStream fos){
        try(FileInputStream fis = new FileInputStream(file)) {
        	
        	
            // Using IOUtils method, it takes FileInputStream 
            // object as param
            byte[] bArray = IOUtils.toByteArray(fis);
            fos.write(bArray);
			/*
			 * for (int i = 0; i < bArray.length; i++){ System.out.print((char) bArray[i]);
			 * }
			 */
            // Using FileUtils method, it takes file object
            // as param
			/*
			 * bArray = FileUtils.readFileToByteArray(file); //displaying byte array content
			 * for (int i = 0; i < bArray.length; i++){ System.out.print((char) bArray[i]);
			 * }
			 */
            
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }
}