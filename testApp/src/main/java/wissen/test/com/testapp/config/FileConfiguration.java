package wissen.test.com.testapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileConfiguration {

    private String uploadLocation;
	private String uploadDir;

	public String getUploadDir() {
		return uploadDir;
	}

	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}

	public String getUploadLocation() {
		return uploadLocation;
	}

	public void setUploadLocation(String upladLocation) {
		this.uploadLocation = upladLocation;
	}

}
