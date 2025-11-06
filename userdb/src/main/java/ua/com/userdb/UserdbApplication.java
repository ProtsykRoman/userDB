package ua.com.userdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "ua.com.userdb")
public class UserdbApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserdbApplication.class, args);
	}

}
