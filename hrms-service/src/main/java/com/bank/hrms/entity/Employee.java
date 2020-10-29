package com.bank.hrms.entity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "Details about the Employees")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Cacheable(true)
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) 
@Table(name = "hrms_master")
public class Employee {

	@Id
	@Column(name = "EMPLOYEE_NUMBER")
	@ApiModelProperty(notes="Unique Employee Number/Id")
	private String employeeNumber;
	
	@Column(name = "LAST_NAME")
	private String lastName;
	
	@Column(name = "FIRST_NAME")
	private String firstName;
	
	@Column(name = "EMPLOYEE_MIDDLE_NAME")
	private String middleName;
	
	@ApiModelProperty(notes="Department of Employee working in a Bank")
	@Column(name = "DEPARTMENT")
	private String department;
	
	@Column(name = "TITLE")
	private String title;
	
	@Column(name = "GENDER")
	private String gender;
	
	@ApiModelProperty(notes="Unique Employee Email Id of a Bank")
	@Column(name = "OFFICIAL_EMAIL")
	private String officialEmail;
	
//	@Column(name = "LOCATION_NAME")
//	private String locationName;
//	
//	@Column(name = "TOWN_OR_CITY")
	private String townOrCity;
	
	@Column(name = "EMPLOYEE_STATUS")
	private String employeeStatus;
	
	@Column(name = "GRADE")
	private String grade;
	
	@ApiModelProperty(notes="Band of Employee within a Bank")
	@Column(name = "BAND")
	private String band;
	
//	@Column(name = "MARITAL_STATUS")
//	private String maritalStatus;
//	
//	@Column(name = "REGION")
//	private String region;
//	
//	@Column(name = "CC_NAME")
//	private String ccName;
//	
//	@Column(name = "CC_CODE")
//	private String ccCode;
	
	@Column(name = "LOCATION_CODE")
	private String locationCode;
	
	@Column(name = "SUPERVISOR_EMP_CODE")
	private String supervisorEmpCode;
	
	@Column(name = "SUPERVISOR_NAME")
	private String supervisorName;
	
//	@Column(name = "DATE_START")
//	private String dateStart;
//	
//	@Column(name = "REASON_FOR_EXIT")
//	private String reasonForExit;
//	
//	@Column(name = "FINAL_PROCESS_DATE")
//	private String finalProcessDate;
	
	@ApiModelProperty(notes="SAP HR unique ID for employee record within a Bank")
	@Column(name = "PERNER")
	private String perner;
	
	@Column(name = "RECORD_STATUS")
	private String recordStatus;

}
