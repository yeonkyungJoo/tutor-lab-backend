package com.tutor.tutorlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class TutorlabApplication {

	public static void main(String[] args) {
		SpringApplication.run(TutorlabApplication.class, args);
	}

}
