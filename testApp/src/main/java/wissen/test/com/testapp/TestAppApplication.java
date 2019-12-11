package wissen.test.com.testapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import wissen.test.com.testapp.config.FileConfiguration;
import wissen.test.com.testapp.config.TomcatConfiguration;


@SpringBootApplication
@EnableConfigurationProperties({FileConfiguration.class, TomcatConfiguration.class})
public class TestAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestAppApplication.class, args);
	}
	
	
	
}
