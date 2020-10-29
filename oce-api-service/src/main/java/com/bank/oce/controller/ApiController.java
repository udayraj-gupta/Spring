package com.bank.oce.controller;

import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
public class ApiController {
		
	
	@Autowired
	RestClient restClient;
	Logger logger = LoggerFactory.getLogger(ApiController.class);
	
//	,consumes =MediaType.APPLICATION_JSON_VALUE
	@RequestMapping(value="/folders",produces = MediaType.APPLICATION_JSON_VALUE)
	public String getAllFolders(HttpServletRequest request, HttpServletResponse httpresponse){
		try {
			return restClient.getAllFoldersCache.get("folders").toString();
		} catch (ExecutionException e) {
			logger.warn("Cache calling failed..."+e);
			e.printStackTrace();
			return restClient.getAllFolders();
		}
	}
	
	@RequestMapping(value="/folder/{id}",consumes =MediaType.APPLICATION_JSON_VALUE , produces = MediaType.APPLICATION_JSON_VALUE)
	public String getFolder(HttpServletRequest request, HttpServletResponse httpresponse,@PathVariable String id) {
		try {
			return restClient.getFolderCache.get(id).toString();
			} catch (ExecutionException e) {
				logger.warn("Cache calling failed..."+e);
				e.printStackTrace();
				return restClient.getFolders(id).toString();
			}
	}
	
//	@RequestMapping(value="/files/{fileId}", produces = MediaType.APPLICATION_JSON_VALUE)
//	public String getFiles(@PathVariable String fileId) {
//		return restClient.getFiles(fileId).toString();
//	}
//	@RequestMapping(value="/applink/{fileId}", produces = MediaType.APPLICATION_JSON_VALUE)
//	public String downloadFile(@PathVariable String fileId) {
//		return restClient.createFileAppLink(fileId).toString();
//	}
	
	@RequestMapping(value="/publink/{fileId}", consumes =MediaType.APPLICATION_JSON_VALUE ,produces = MediaType.APPLICATION_JSON_VALUE)
	public String filePubLink(HttpServletRequest request, HttpServletResponse httpresponse,@PathVariable String fileId) {
		try {
			return restClient.getFilePublicLinkCache.get(fileId).toString();
			} catch (ExecutionException e) {
				logger.warn("Cache calling failed..."+e);
				e.printStackTrace();
				return restClient.getFilePubLink(fileId).toString();
			}
	}
	
	@RequestMapping(value="/search/{query}", consumes =MediaType.APPLICATION_JSON_VALUE ,produces = MediaType.APPLICATION_JSON_VALUE)
	public String textSearch(HttpServletRequest request, HttpServletResponse httpresponse,@PathVariable String query){
			return restClient.fulltextSearch(query);
	}
	
	@GetMapping("/ping")
	public String testMethod() {
		return "OK";
		
	}

}
