package com.bank.management.challenge.application;

import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.bank.management.challenge.infrastructure.repositories.jpa")
@EntityScan("com.bank.management.challenge.infrastructure.entities")
@ComponentScan("com.bank.management.challenge")
public class ChallengeApplication {

	@Generated
	public static void main(String[] args) {
		SpringApplication.run(ChallengeApplication.class, args);
	}

}
