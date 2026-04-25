package com.jmoreno_dev.productmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProductmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductmanagerApplication.class, args);
	}

}
