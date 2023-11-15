package com.proj;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import com.proj.director.UserManager.*;
import com.proj.model.users.*;

@EnableJpaRepositories(basePackages={"com.proj.function.UserRepository"})
@SpringBootApplication
public class Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		//SpringApplication.run(AccessingDataMysqlApplication.class, args);
	}

	@Override // Overrides abstract run method
	public void run(String... args) throws Exception {
		
		//@Autowired
		//UserManager userManager;

		User user = new Guest("Fisk", "password"); 
		//userManager.save(user);

	}

}
