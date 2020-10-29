package com.bank.hrms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.hrms.entity.Employee;
import com.bank.hrms.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository repository;
	
	/**
     * Get the Employee details from HRMS Masters
     *
     * @param employeeNumber
     * @return Employee
     */
	public Employee getEmployeeById(String employeeNumber) {
		return repository.findByEmployeeNumber(employeeNumber);
	}
	
	/**
     * Get the Employee details from HRMS Masters
     *
     * @param employeeEmail
     * @return Employee
     */
	public Employee getEmployeeByEmail(String employeeEmail) {
		return repository.findByOfficialEmail(employeeEmail);
	}
	
	/*
	 * public List<Employee> getEmployees(){ return repository.findAll();
	 * 
	 * }
	 */
	
}
