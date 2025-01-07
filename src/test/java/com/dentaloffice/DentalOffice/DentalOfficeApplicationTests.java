package com.dentaloffice.DentalOffice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class DentalOfficeApplicationTests {

	@Test
	void contextLoads() {
	}
	@Test
	void mainMethodTest() {
		// Ensures the main method runs without exceptions
		assertDoesNotThrow(() -> DentalOfficeApplication.main(new String[]{}));
	}

}
