package com.sydney.au.ethicalaivalidation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.sydney.au.ethicalaivalidation.domain")
@EnableJpaRepositories("com.sydney.au.ethicalaivalidation.repository")
public class EthicalaivalidationApplication {

	public static void main(String[] args) {
		SpringApplication.run(EthicalaivalidationApplication.class, args);
	}

}
