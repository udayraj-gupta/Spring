package com.bank.oce.filter;
//package com.hdfcbank.oce.filter;
//
//import java.io.IOException;
//import java.util.Enumeration;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//
//import com.hdfcbank.oce.controller.RestClient;
//
//@Component
//public class RequestResponseLoggingFilter implements Filter {
//
//	@Autowired
//	RestClient restClient;
//	Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);
//	  @Override
//	    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
//	  
//		  HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
//	        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
//	        httpResponse.setHeader("Access-Control-Allow-Origin","*");
//			httpResponse.setHeader("Access-Control-Allow-Methods","POST,GET");
//			httpResponse.setHeader("Access-Control-Max-Age","3600");
//			httpResponse.setHeader("Access-Control-Allow-Headers","X-Requested-With");
//			httpResponse.addHeader("X-Frame-Options", "ALLOW");
//	        if (httpRequest.getMethod().equals("OPTIONS")) {
//	        	httpResponse.setStatus(HttpServletResponse.SC_ACCEPTED);
//	            httpResponse.setHeader("Access-Control-Allow-Origin", "*");
//	            httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET");
//	            httpResponse.setHeader("Access-Control-Max-Age", "3600");
//	            httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
//	            return;
//	        }
//	       
//	        Enumeration<String> headerNames = httpRequest.getHeaderNames();
//	        if (headerNames != null) {
//	            while (headerNames.hasMoreElements()) {
//	            	logger.info("Header: " + httpRequest.getHeader(headerNames.nextElement()));
//	            }
//	    }
//			
//			logger.info("Logging Request  {} : {}", httpRequest.getMethod(), httpRequest.getRequestURI());
//			logger.info("Header params, {}",headerNames);
//			if(httpRequest.getHeader("Authorization")!=null || httpRequest.getHeader("authorization")!=null) {
//				HttpStatus activeCheck = restClient.loginCheck(httpRequest.getHeader("authorization"));
//				if(activeCheck.value()==200) {
//					logger.info("API Authorization Granted successfully :{}", activeCheck.value());
//					
//				}else {
//					logger.error("API Authorization Granted Failed :{}", activeCheck.value());
//					httpResponse.sendError(activeCheck.value(),activeCheck.toString());
//					return;
//				}
//			}else {
//				httpResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "Header Authorization missing");
//				logger.error("Header Authorization Missing");
//				return;
//			}
//			chain.doFilter(servletRequest, servletResponse);
//			logger.info("Logging Response :{}", httpResponse.getContentType());
//			
//			
//	    }
//
//	
//
//}
