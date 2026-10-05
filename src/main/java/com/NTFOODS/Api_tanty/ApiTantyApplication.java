package com.NTFOODS.Api_tanty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class ApiTantyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiTantyApplication.class, args);
	}

}
