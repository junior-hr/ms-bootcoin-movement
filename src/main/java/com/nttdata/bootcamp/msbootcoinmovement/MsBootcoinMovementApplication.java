package com.nttdata.bootcamp.msbootcoinmovement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Class MsBootcoinMovementApplication Main.
 * BootcoinMovement microservice class MsBootcoinMovementApplication.
 */
@SpringBootApplication
@EnableEurekaClient
public class MsBootcoinMovementApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsBootcoinMovementApplication.class, args);
	}

}
