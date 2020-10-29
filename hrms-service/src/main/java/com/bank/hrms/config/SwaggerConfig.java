package com.bank.hrms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Profile({"uat","prod"})
@Configuration
@EnableSwagger2
public class SwaggerConfig {
//	@Bean
//	public Docket api() {
//		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("com.hdfcbank.hrms"))
//				.paths(PathSelectors.any()) //PathSelectors.any()
//				.build().apiInfo(metaData());
//	}
//	
//	private ApiInfo metaData() {
//		return new ApiInfo(
//				"API Documentation",
//				"API Documentation for HDFC Bank IT Governance",
//				"1.0",
//				"Terms of service",
//				new springfox.documentation.service.Contact("Clover Infotech pvt ltd", "https://www.cloverinfotech.com/", "udayraj.gupta@cloverinfotech.com" ),
//				"Apache License Version 2.0",
//				"https://www.apache.org/licenses/LICENSE-2.0",
//				Collections.emptyList());
//	}

}
