package com.bank.hrms;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableCircuitBreaker
@EnableHystrixDashboard
@EnableSwagger2
@SpringBootApplication
public class HrmsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HrmsServiceApplication.class, args);
	}

	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("com.hdfcbank.hrms"))
				.paths(PathSelectors.any()) //PathSelectors.any()
				.build().apiInfo(metaData());
	}
	
	private ApiInfo metaData() {
		return new ApiInfo(
				"API Documentation",
				"API Documentation for HDFC Bank IT Governance",
				"1.0",
				"Terms of service",
				new springfox.documentation.service.Contact("Clover Infotech pvt ltd", "https://www.cloverinfotech.com/", "udayraj.gupta@cloverinfotech.com" ),
				"Apache License Version 2.0",
				"https://www.apache.org/licenses/LICENSE-2.0",
				Collections.emptyList());
	}
}
