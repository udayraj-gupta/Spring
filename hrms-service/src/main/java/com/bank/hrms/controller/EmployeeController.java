package com.bank.hrms.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bank.hrms.entity.Employee;
import com.bank.hrms.service.EmployeeService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "HRMS Master APIs")
@RestController
public class EmployeeController {
	
	@Autowired
	private EmployeeService service; 
	String response;
	Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	private static final String SUCCESS = "0";
	private static final String FAILED = "1";
	private static final String STATUS_CODE = "statusCode";
	
	@ApiOperation(value = "To fetch by employeeId for employee details from HRMS Masters", response = Employee.class, notes = "Get Request to fetch employee details from HRMS Masters using @Parameter employeeId")
	@GetMapping(value="/employee-number/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String findEmployeeByEmployeeNumber(@ApiParam(value = "Employee Id for the HRMS you need to retireve",required = true) @PathVariable String employeeId) {
		
		Employee employeeData = service.getEmployeeById(employeeId);
		//Employee.builder().ccName("Uday"); //Setter
		JSONObject response = new JSONObject();
		logger.info("HRMS Service call fetch details using Employee Number {}",employeeId);
		if(employeeData != null) {
			response.put(STATUS_CODE, SUCCESS);
			response.put("data", new JSONObject(employeeData));
		}else {
			response.put(STATUS_CODE, FAILED);
			response.put("description", "No Record found");
		}
		return response.toString();
	}
	
	@ApiOperation(value = "To fetch by Email Address for employee details from HRMS Masters", response = Employee.class,notes = "To fetch employee details from HRMS Masters using Official Email Id")
	@GetMapping(value="/employee-email/{officialEmail}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String findEmployeeByEmail(@ApiParam(value = "Email Id for the HRMS you need to retireve",required = true) @PathVariable String officialEmail) {
		Employee employeeData = service.getEmployeeByEmail(officialEmail);
		JSONObject response = new JSONObject();
		logger.info("HRMS Service call fetch details using Email ID {}",officialEmail);
		if(employeeData != null) {
			response.put(STATUS_CODE, SUCCESS);
			response.put("data", new JSONObject(employeeData));
		}else {
			response.put(STATUS_CODE, FAILED);
			response.put("description", "No Record found");
		}
		return response.toString();
	}
	

	@HystrixCommand(fallbackMethod = "circuitBreaker", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000"),
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60") })
	@GetMapping(value="/test", produces = MediaType.APPLICATION_JSON_VALUE)
	public String search() throws URISyntaxException {
	
		RestTemplate restTemplate = new RestTemplate();
	     
	    final String baseUrl = "http://localhost:8080/folders";
	    URI uri = new URI(baseUrl);
	 
	    ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
	
		return result.toString();
	}
	
	
	@SuppressWarnings("unchecked")
	@HystrixCommand(fallbackMethod = "circuitBreakerFallBack", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60") })
	@GetMapping(value="/verification/{mobileNo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> sendOtp(@PathVariable String mobileNo) {
		try{
			return ResponseEntity.ok(response);
		}catch (Exception e) {
			return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public ResponseEntity<String> circuitBreakerFallBack() {
		return ResponseEntity.ok("Service is unavailable. Try after sometime");
	}
	
}
