package com.proj.unitTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Order;

@SpringBootTest(classes = {ApplicationTests.class})
class ApplicationTests {

	@Test
	@Order(1)
	void contextLoads() {
	}

}
