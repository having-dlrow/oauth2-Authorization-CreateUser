package com.example.oauth2client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@SpringBootApplication(exclude = ReactiveOAuth2ClientAutoConfiguration.class)
public class Oauth2ClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(Oauth2ClientApplication.class, args);
	}

}
