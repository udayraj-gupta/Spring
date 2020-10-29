package com.bank.properties;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties
@Validated
@PropertySource("classpath:/com/hdfcbank/properties/oce.properties")
public class OcePropertiesBean {
	@NotEmpty
	String host;
	
	@NotEmpty
	String uid;
	
	@NotEmpty
	String password;
	
	int cacheDurationInMinutes;
	
	@NotEmpty
	@Size(max = 44)
	@Size(min=44)
	String oceDocRootFolderId;
	
	@NotEmpty
	@Size(max = 36)
	@Size(min=36)
	String oceAssetId;
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getCacheDurationInMinutes() {
		return cacheDurationInMinutes;
	}
	public void setCacheDurationInMinutes(int cacheDurationInMinutes) {
		this.cacheDurationInMinutes = cacheDurationInMinutes;
	}
	public String getOceDocRootFolderId() {
		return oceDocRootFolderId;
	}
	public void setOceDocRootFolderId(String oceDocRootFolderId) {
		this.oceDocRootFolderId = oceDocRootFolderId;
	}
	public String getOceAssetId() {
		return oceAssetId;
	}
	public void setOceAssetId(String oceAssetId) {
		this.oceAssetId = oceAssetId;
	}
	
	

}
