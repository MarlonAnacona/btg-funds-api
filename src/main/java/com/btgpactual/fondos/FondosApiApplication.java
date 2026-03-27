package com.btgpactual.fondos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.btgpactual.fondos.repositories")

public class FondosApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FondosApiApplication.class, args);
	}

}
