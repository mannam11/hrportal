package com.app.hrportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HrportalApplication {

	public static void main(String[] args) {
		SpringApplication.run(HrportalApplication.class, args);
	}

    @GetMapping("/health")
    public String healthCheck(){
        return "OK";
    }
}
