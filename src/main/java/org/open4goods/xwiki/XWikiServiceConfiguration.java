package org.open4goods.xwiki;


import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.nio.charset.Charset;
import java.util.Base64;

import org.open4goods.xwiki.services.XWikiUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoConfiguration
@EnableConfigurationProperties(XWikiServiceProperties.class)
public class XWikiServiceConfiguration {
	
	private static final Logger logger = LoggerFactory.getLogger(XWikiServiceConfiguration.class);

	private final XWikiServiceProperties xWikiProperties;

	public XWikiServiceConfiguration(XWikiServiceProperties xWikiProps) {
		this.xWikiProperties = xWikiProps;
	}
	
	
	@Bean
	RestClient restClient(RestClient.Builder builder) {
		String auth = xWikiProperties.getUsername() + ":" + xWikiProperties.getPassword();
		
		RestClient restClient = builder
				.baseUrl(xWikiProperties.getBaseUrl())
				.defaultHeader("Authorization", "Basic "+Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII"))))
				.build();
		logger.info("RestClient Bean created with basic authentication to " + xWikiProperties.getBaseUrl());
		return restClient;
		
	}
	
	@Bean
	RestTemplate restTemplate(RestTemplateBuilder builder) {
		RestTemplate restTemplate =  builder.basicAuthentication(xWikiProperties.getUsername(), xWikiProperties.getPassword()).build();
		restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(xWikiProperties.getBaseUrl()));
		logger.info("RestTemplate Bean created with basic authentication to " + xWikiProperties.getBaseUrl());
		return restTemplate;
	}

	@Bean
	XWikiUserService xwikiClient(RestTemplate restTemplate) {
		return new XWikiUserService(restTemplate);
	}
	
//	@Bean
//	XWikiUserService xwikiClient(RestClient restClient) {
//		return new XWikiUserService(restClient);
//	}

	
}
