package com.bank.oce;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer  {
//public class ServletInitializer extends SpringBootServletInitializer implements WebApplicationInitializer  {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(OceApiServiceApplication.class);
	}

}
