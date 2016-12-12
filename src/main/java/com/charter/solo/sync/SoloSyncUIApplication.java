package com.charter.solo.sync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ComponentScan({"com.charter.solo.sync.*","oracle.*"})
@PropertySource("classpath:application.properties")
public class SoloSyncUIApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {

		SpringApplication.run(SoloSyncUIApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
		
		return application.sources(SoloSyncUIApplication.class);
	}
	
	
	@Bean
	RestTemplate getRestTemplate(){
		
		return new RestTemplate();
	}
	
}
