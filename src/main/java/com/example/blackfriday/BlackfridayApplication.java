package com.example.blackfriday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class BlackfridayApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlackfridayApplication.class, args);
	}

}
