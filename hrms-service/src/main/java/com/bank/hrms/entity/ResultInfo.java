package com.bank.hrms.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@ApiModel(description = "Details about the Result Info")
public class ResultInfo {

	@ApiModelProperty(notes="Status Code")
	private String status;
	@ApiModelProperty(notes="Error Code Id")
	private int errorCode;
	@ApiModelProperty(notes="Response Message")
	private String message;
	
	
}
