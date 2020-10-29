package com.hdfcbank.oce.controller;

import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.hdfcbank.properties.OcePropertiesBean;

@Service
public class RestClient {
	
	
	Logger logger = LoggerFactory.getLogger(RestClient.class);
//	String host="";
//	String userName="";
//	String password="";
	int cacheDuration = 60;
	JSONObject apiResponse;
	
	
	
	@Autowired
	RestClient restClients;
	
	
	@Autowired
	OcePropertiesBean ocePropertiesBean;
	
	
	//Guava Cache implementation start 
		public volatile LoadingCache<String, String> getAllFoldersCache=CacheBuilder.newBuilder().maximumSize(100).expireAfterWrite(cacheDuration, TimeUnit.MINUTES).build(new CacheLoader<String, String>() {
			@Override
			public String load(String key) throws Exception {
				logger.info("Adding Cache Key {}",key);
				return restClients.getAllFolders();
			} 
		});
		
		public volatile LoadingCache<String, JSONArray> getFolderCache=CacheBuilder.newBuilder().maximumSize(2000).expireAfterWrite(cacheDuration, TimeUnit.MINUTES).build(new CacheLoader<String, JSONArray>() {
			@Override
			public JSONArray load(String folderId) throws Exception {
				logger.info("Adding Cache Key {}",folderId);
				return restClients.getFolders(folderId);
			} 
		});
		public volatile LoadingCache<String, String> getFilePublicLinkCache=CacheBuilder.newBuilder().maximumSize(2000).expireAfterWrite(cacheDuration, TimeUnit.MINUTES).build(new CacheLoader<String, String>() {
			@Override
			public String load(String fileId) throws Exception {
				logger.info("Adding Cache Key {}",fileId);
				return restClients.getFilePubLink(fileId);
			} 
		});
	
		
		/**
		 * Execute the given method on the provided URL with mandatory userName & password.
		 * <p>The oceDocRootFolderId is the Root Folder ID Generated in OCE Platform Document
		 * section where all the documents related to Policy & Procedure is stored. Any Modification
		 * in Folder the regenerated ID must be passed to @param oceDocRootFolderId.
		 */
public String getAllFolders() {
		JSONArray responseBody = new JSONArray();
		String oceDocRootFolderId =ocePropertiesBean.getOceDocRootFolderId();
		String endPoint=ocePropertiesBean.getHost()+"/documents/api/1.2/folders/"+oceDocRootFolderId+"/items?orderby=name:asc";
//		TestRestTemplate testRestTemplate = new TestRestTemplate(ocePropertiesBean.getUid(), ocePropertiesBean.getPassword());
		RestTemplate testRestTemplate = new RestTemplate(new HttpsClientRequestFactory());
		testRestTemplate.getInterceptors().add(
				  new BasicAuthorizationInterceptor(ocePropertiesBean.getUid(), ocePropertiesBean.getPassword()));

		logger.info("Remote API Call {}",endPoint);
		ResponseEntity<String> response = null;
		Assert.notNull(endPoint, "URL is required");
		Assert.notNull(ocePropertiesBean.getUid(), "userName is required");
		Assert.notNull(ocePropertiesBean.getPassword(), "password is required");
		try {
			response = testRestTemplate.getForEntity(endPoint, String.class);
		}catch (Exception e) {
			logger.error("Exception Occured",e);
			return "{\"status\":\"-1\",\"message\":\"Exception Occured\"}";
		}
		logger.info("Status {}",response.getStatusCode());
		if(response.getStatusCode().equals(HttpStatus.OK)) {
		apiResponse = new JSONObject(response.getBody());
		apiResponse.getJSONArray("items").forEach(item -> {
		    JSONObject jsonLoopObj = (JSONObject) item;
		    JSONObject tempJsonObject = new JSONObject();
		    try {
			    String[] arrOfStr = jsonLoopObj.getString("name").split("~"); // Delimiter ~ is mandatory as folder naming convention to bifurcate
			    tempJsonObject.put("processName", arrOfStr[0]);
			    tempJsonObject.put("processChampion", arrOfStr[1]);
			    tempJsonObject.put("processOwner", arrOfStr[2]);
			    //tempJsonObject.put("tab", getFolders(jsonLoopObj.getString("id")));
		    }catch (Exception e) {
		    	logger.warn("Delimiter '~' Not found found in the OCE Document folder name format");
		    	tempJsonObject.put("process name", jsonLoopObj.getString("name"));
			}
		    tempJsonObject.put("folderId", jsonLoopObj.getString("id"));
		    responseBody.put(tempJsonObject);
		});
		}else {
			return "{\"status\":\"-1\",\"message\":\""+response.getStatusCode()+"\"}";
		}
		return responseBody.toString();
	}

	/**
	 * Execute the given method on the provided URL with mandatory userName & password.
	 * <p>To perform the search you need to use the OAuth token when calling the folder search API
	 */
public String fulltextSearch(String query) {
	if(query.length()>2 && !query.toLowerCase().equals("uday")){
	System.out.println("Search keyword : "+query);
	JSONArray searchResult = new JSONArray();
	String endPoint=ocePropertiesBean.getHost()+"/documents/api/1.2/folders/search/items?fulltext="+query+"*";
//	TestRestTemplate testRestTemplate = new TestRestTemplate(ocePropertiesBean.getUid(), ocePropertiesBean.getPassword());
	RestTemplate testRestTemplate = new RestTemplate(new HttpsClientRequestFactory());
	testRestTemplate.getInterceptors().add(
			  new BasicAuthorizationInterceptor(ocePropertiesBean.getUid(), ocePropertiesBean.getPassword()));
	ResponseEntity<String> response = testRestTemplate.getForEntity(endPoint, String.class);
	apiResponse = new JSONObject(response.getBody());
	if(Integer.parseInt(apiResponse.getString("totalCount"))>0) {
		apiResponse.getJSONArray("items").forEach(item -> {
			JSONObject tempJsonObject = new JSONObject();
			JSONObject jsonItems = (JSONObject) item;
			if(jsonItems.getString("type").equalsIgnoreCase("file") && !jsonItems.getString("name").contains(".html") && !jsonItems.getString("name").contains(".css") && !jsonItems.getString("name").contains(".js")) {
			tempJsonObject.put("name", jsonItems.getString("name"));
			tempJsonObject.put("id", jsonItems.getString("id"));
			tempJsonObject.put("url", ocePropertiesBean.getHost()+"/documents/file/"+jsonItems.getString("id"));
			searchResult.put(tempJsonObject);
			}
		});
		if (searchResult.isEmpty()) {
			return "{\"count\":\"0\",\"errorCode\":\"0\",\"totalCount\":\"0\"}";
		}else {
			return searchResult.toString();
		}
	}else {
		return apiResponse.toString();
	}
	}else {
		return "{\"status\":\"1\",\"message\":\"invalid keyword\"}";
	}
	
}

//To fetch sub folders
public JSONArray getFolders(String folderId) {
	JSONArray subFolders = new JSONArray();
	String endPoint=ocePropertiesBean.getHost()+"/documents/api/1.2/folders/"+folderId+"/items?orderby=name:asc";
//	TestRestTemplate testRestTemplate = new TestRestTemplate(ocePropertiesBean.getUid(), ocePropertiesBean.getPassword());
	RestTemplate testRestTemplate = new RestTemplate(new HttpsClientRequestFactory());
	testRestTemplate.getInterceptors().add(
			  new BasicAuthorizationInterceptor(ocePropertiesBean.getUid(), ocePropertiesBean.getPassword()));
	ResponseEntity<String> response = testRestTemplate.getForEntity(endPoint, String.class);
	if(response.getStatusCode().equals(HttpStatus.OK))
		apiResponse = new JSONObject(response.getBody());
//		logger.info("API response folders with folder id {}",apiResponse.toString());
		
		if(apiResponse!=null && !apiResponse.isEmpty()) {
		apiResponse.getJSONArray("items").forEach(item -> {
		    JSONObject obj = (JSONObject) item;
		    JSONObject tempJsonObject = new JSONObject();
		    if(obj.getString("type").equalsIgnoreCase("folder")) {
		    	tempJsonObject.put("tabName", obj.getString("name"));
			    tempJsonObject.put("folderId", obj.getString("id"));
			    JSONArray childDirFiles = new JSONArray();
			    childDirFiles = getFiles(obj.getString("id"));
			    tempJsonObject.put("files", childDirFiles);
			    subFolders.put(tempJsonObject);
		    }
		});
		}else {
			logger.info("Empty Folders");
			JSONObject tempJsonObject = new JSONObject();
			tempJsonObject.put("status", "Empty");
			subFolders.put(tempJsonObject);
		}
	return subFolders;
}

//To get the file ID & URL
public JSONArray getFiles(String folderId) {
	JSONArray subFolders = new JSONArray();
	String endPoint=ocePropertiesBean.getHost()+"/documents/api/1.2/folders/"+folderId+"/items";
//	TestRestTemplate testRestTemplate = new TestRestTemplate(ocePropertiesBean.getUid(), ocePropertiesBean.getPassword());
	RestTemplate testRestTemplate = new RestTemplate(new HttpsClientRequestFactory());
	testRestTemplate.getInterceptors().add(
			  new BasicAuthorizationInterceptor(ocePropertiesBean.getUid(), ocePropertiesBean.getPassword()));
	ResponseEntity<String> response = testRestTemplate.getForEntity(endPoint, String.class);
	if(response.getStatusCode().equals(HttpStatus.OK))
		apiResponse = new JSONObject(response.getBody());
		if(apiResponse!=null && !apiResponse.isEmpty() && apiResponse.has("items")) {
		apiResponse.getJSONArray("items").forEach(item -> {
		    JSONObject obj = (JSONObject) item;
		    JSONObject fileJsonObject = new JSONObject();
		    if(obj.getString("type").equalsIgnoreCase("file")) {
		    	fileJsonObject.put("fileName", obj.getString("name"));
			    fileJsonObject.put("url", ocePropertiesBean.getHost()+"/documents/file/"+obj.getString("id"));
			    fileJsonObject.put("fileId",obj.getString("id"));
			    subFolders.put(fileJsonObject);
		    }
		});
		}else {
			logger.info("Empty Files : {}",folderId);
		}
	return subFolders;
}

//API to create appLinks ->. Method will generate the fresh access token & appLinkId
//public String createFileAppLink(String fileId) {
//	String endPoint=ocePropertiesBean.getHost()+"/documents/api/1.2/applinks/file/"+fileId;
//	TestRestTemplate testRestTemplate = new TestRestTemplate(ocePropertiesBean.getUid(), ocePropertiesBean.getPassword());
//	JSONObject requestBody = new JSONObject();
//	requestBody.put("assignedUser", "U179CF404BD30246A4DB6AE435D20D5DECF1");
//	requestBody.put("role", "manager");
//	requestBody.put("userLocale", "English");
//	HttpEntity<String> request = new HttpEntity<>(requestBody.toString());
//	ResponseEntity<String> response = testRestTemplate.postForEntity(endPoint, request, String.class);
//	System.out.println(response.getBody());
//	apiResponse = new JSONObject(response.getBody());
//	return getFilePublicLink(apiResponse.getString("accessToken"),apiResponse.getString("appLinkID"),fileId);
//	}

//	API to fetch file public access url
public String getFilePublicLink(String accessToken,String appLinkID,String fileId) {
	
	String endPoint=ocePropertiesBean.getHost()+"/documents/api/1.2/files/"+fileId+"/previewPath?version=1";
	TestRestTemplate testRestTemplate = new TestRestTemplate(ocePropertiesBean.getUid(), ocePropertiesBean.getPassword());
	HttpHeaders headers = new HttpHeaders();
	  headers.set("accessToken", accessToken);
	  headers.set("appLinkID", appLinkID);
	  ResponseEntity<String> response = testRestTemplate.exchange(endPoint,HttpMethod.GET, new HttpEntity<Object>(headers), String.class);
	return new JSONObject(response.getBody()).getString("previewUrl");
	}

public HttpStatus loginCheck(String accessToken) {
	
	String endPoint=ocePropertiesBean.getHost()+"/content/management/api/v1.1/items/"+ocePropertiesBean.getOceAssetId();
//	TestRestTemplate testRestTemplate = new TestRestTemplate(ocePropertiesBean.getUid(), ocePropertiesBean.getPassword());
	RestTemplate testRestTemplate = new RestTemplate(new HttpsClientRequestFactory());
	testRestTemplate.getInterceptors().add(
			  new BasicAuthorizationInterceptor(ocePropertiesBean.getUid(), ocePropertiesBean.getPassword()));
	HttpHeaders headers = new HttpHeaders();
	headers.set("Authorization", accessToken); //Authorization Bearer IDCS_TOKEN
	ResponseEntity<String> response = testRestTemplate.exchange(endPoint,HttpMethod.GET, new HttpEntity<Object>(headers), String.class);
	logger.info("OCE Login Status : {}",response.getStatusCode());
	return response.getStatusCode();
	}

public String getFilePubLink(String fileId) {
	String endPoint=ocePropertiesBean.getHost()+"/documents/api/1.2/publiclinks/file/"+fileId;
	String responseBody="";
	JSONObject localJsonObj =  new JSONObject();
	
//	TestRestTemplate testRestTemplate = new TestRestTemplate(ocePropertiesBean.getUid(), ocePropertiesBean.getPassword());
	RestTemplate testRestTemplate = new RestTemplate(new HttpsClientRequestFactory());
	testRestTemplate.getInterceptors().add(
			  new BasicAuthorizationInterceptor(ocePropertiesBean.getUid(), ocePropertiesBean.getPassword()));
	HttpHeaders headers = new HttpHeaders();
	ResponseEntity<String> response = testRestTemplate.exchange(endPoint,HttpMethod.GET, new HttpEntity<Object>(headers), String.class);
	logger.info("Publink Check : {}",response);
	if(response.getStatusCode().equals(HttpStatus.OK) && new JSONObject(response.getBody()).getString("errorCode").equals("0")) {
		apiResponse = new JSONObject(response.getBody());
		logger.info("publiclinks available : {}",apiResponse.has("items"));
		if(!apiResponse.has("items")) { //Sometimes Viewer role is available but not Download Role
			logger.info("Line 261 creating publink");
			responseBody = createFilePubLink(fileId);
		}else {
			apiResponse.getJSONArray("items").forEach(item -> {
				JSONObject jsonItems = (JSONObject) item;
				if(jsonItems.getString("role").equalsIgnoreCase("downloader") && jsonItems.getString("linkID").length()==44) {
					localJsonObj.put("url", ocePropertiesBean.getHost()+"/documents/link/"+jsonItems.getString("linkID")+"/file/"+fileId);
					localJsonObj.put("status", "0");
				}
			});
			if(localJsonObj.has("url")) { //create publink if downloaer role not available
				responseBody=localJsonObj.toString();
			}else {
				logger.info("Line 274 creating publink");
				responseBody = createFilePubLink(fileId);
			}
		}
		
		
	}else if(response.getStatusCode().equals(HttpStatus.NOT_FOUND) && new JSONObject(response.getBody()).getString("errorCode").equals("-16")) {
		logger.info("publiclinks Not Available");
		logger.info("Line 282 creating publink");
		responseBody = createFilePubLink(fileId);
	}
	return responseBody;
}

public String createFilePubLink(String fileId) {
	logger.info("Creating Publink for File Id : {}",fileId);
	String endPoint=ocePropertiesBean.getHost()+"/documents/api/1.2/publiclinks/file/"+fileId;
//	TestRestTemplate testRestTemplate = new TestRestTemplate(ocePropertiesBean.getUid(), ocePropertiesBean.getPassword());
	RestTemplate testRestTemplate = new RestTemplate(new HttpsClientRequestFactory());
	testRestTemplate.getInterceptors().add(
			  new BasicAuthorizationInterceptor(ocePropertiesBean.getUid(), ocePropertiesBean.getPassword()));
	JSONObject requestBody = new JSONObject();
	JSONObject localJsonObj =  new JSONObject();
	requestBody.put("assignedUsers", "@everybody");
	requestBody.put("role", "downloader");
	requestBody.put("expirationTime", "2026-01-01T00:00:01Z");
	HttpEntity<String> request = new HttpEntity<>(requestBody.toString());
	ResponseEntity<String> response = testRestTemplate.postForEntity(endPoint, request, String.class);
	
	if(response.getStatusCode().equals(HttpStatus.OK) && new JSONObject(response.getBody()).getString("errorCode").equals("0")) {
		logger.info("publiclinks Generated");
		localJsonObj.put("url",  ocePropertiesBean.getHost()+"/documents/link/"+new JSONObject(response.getBody()).getString("linkID")+"/file/"+fileId);
		localJsonObj.put("status", "0");
		return localJsonObj.toString();
	}else {
		localJsonObj.put("message", "Invalid Id");
		localJsonObj.put("status", "-1");
		return localJsonObj.toString();
	}
	}
}




