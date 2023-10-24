package com.proj.server;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*") // Wildcard is a security risk! Only for testing purposes
public class serverController {

	@GetMapping("/") // GetMapping maps GET requests to return a response
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@GetMapping("/something")
	public String something() {
		return Application.alice.firstName;
	}

	@PutMapping("/responder") // Recieves PUT requests and can respond
	public String responder(@RequestBody ClientNumber number) { // RequestBody takes JSON field(s) and deserializes them to a class (ClientNumber) with corresponding field. (ClienNumber needs a field "value" because we receive one)
		boolean response = number.value % 2 == 0;
		return Boolean.toString(response);
	}
}