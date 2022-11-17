package com.example.testsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SearchViewApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchViewApplication.class, args);
	}

}
