package com.exe.sharkauction;

import com.exe.sharkauction.components.configurations.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(AppProperties.class)
public class SharkauctionApplication {

	public static void main(String[] args) {
		SpringApplication.run(SharkauctionApplication.class, args);
	}

}
