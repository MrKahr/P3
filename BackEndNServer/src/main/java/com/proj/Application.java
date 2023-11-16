package com.proj;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import com.proj.model.users.*;
import com.proj.repositoryhandler.UserdbHandler;

@EnableJpaRepositories(basePackages={"com.proj.repositories"})
@SpringBootApplication
public class Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		//SpringApplication.run(AccessingDataMysqlApplication.class, args);
	}

	@Override // Overrides abstract run method
	public void run(String... args) throws Exception {
		
	}

}
