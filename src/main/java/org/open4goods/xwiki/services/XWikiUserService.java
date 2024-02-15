package org.open4goods.xwiki.services;


import org.open4goods.xwiki.model.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


public class XWikiUserService {

	private static Logger logger = LoggerFactory.getLogger(XWikiUserService.class);
	
	private RestClient restClient;
	private RestTemplate restTemplate;
	
	public XWikiUserService(RestClient restClient) {
		this.restClient = restClient;
	}
	
	public XWikiUserService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	
	public Users findAllUsers(){
		
		Users users = null;
		ResponseEntity<String> response = restTemplate.getForEntity("rest/wikis/query?q=object:XWiki.XWikiUsers&media=json&start=1&number=1",String.class);
		//System.out.println(response.getBody());
		logger.info(response.getBody());
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			users = objectMapper.readValue(response.getBody(), new TypeReference<Users>() {});
			return users;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return users;
	}
	
	public Users findAllUsers2(){
		
		Users users = null;
		// RestClient 1
//		Users users=  restClient.get()
//				.uri("/rest/wikis/query?q=object:XWiki.XWikiUsers&media=json&start=1&number=1")
//				.retrieve()
//				.body(new ParameterizedTypeReference<Users>() {});		
//		return users;

		// RestClient 2
		String body=  restClient.get()
				.uri("/rest/wikis/query?q=object:XWiki.XWikiUsers&media=json&start=1&number=1")
				.retrieve()
				.body(new ParameterizedTypeReference<String>() {});		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			users = objectMapper.readValue(body, new TypeReference<Users>() {});
			return users;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return users;
		
		
	}
	
	
}
