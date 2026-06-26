package com.tool_rental.herramientas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class HerramientasApplication {

	public static void main(String[] args) {
		SpringApplication.run(HerramientasApplication.class, args);
	}

}
