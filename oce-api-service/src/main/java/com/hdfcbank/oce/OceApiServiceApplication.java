package com.hdfcbank.oce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

import com.hdfcbank.properties.OcePropertiesBean;

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties(OcePropertiesBean.class)
public class OceApiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OceApiServiceApplication.class, args);
	}

}
