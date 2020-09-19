package com.mohit.gcd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class GcdApplication {
	public static void main(String[] args) {
		SpringApplication.run(GcdApplication.class, args);
	}
}
