package com.proj.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import com.proj.database.AccessingDataMysqlApplication;
import com.proj.database.UserRepository;

@SpringBootApplication
public class Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		//SpringApplication.run(AccessingDataMysqlApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {


	}

}
