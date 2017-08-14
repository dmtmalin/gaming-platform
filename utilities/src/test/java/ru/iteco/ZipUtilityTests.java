package ru.iteco;

import org.junit.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations= "classpath:application-test.properties")
public class ZipUtilityTests {

	@SpringBootApplication
	static class TestConfiguration {
	}

	@Test
	public void contextLoads() {
	}

}
