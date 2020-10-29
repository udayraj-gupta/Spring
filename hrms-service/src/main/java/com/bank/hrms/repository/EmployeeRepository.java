package com.bank.hrms.repository;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import com.bank.hrms.entity.Employee;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

	@QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
	Employee findByEmployeeNumber(String employeeNumber);

	Employee findByOfficialEmail(String employeeEmail);

}
