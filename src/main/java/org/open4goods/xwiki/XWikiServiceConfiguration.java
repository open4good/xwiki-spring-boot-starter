package org.open4goods.xwiki;


import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.open4goods.xwiki.authentication.XwikiAuthenticationProvider;
import org.open4goods.xwiki.config.XWikiServiceProperties;
import org.open4goods.xwiki.services.XWikiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles xwiki services Beans
 * @author Thierry.Ledan
 */
@AutoConfiguration
@EnableConfigurationProperties(XWikiServiceProperties.class)
public class XWikiServiceConfiguration {
	
	private static final Logger logger = LoggerFactory.getLogger(XWikiServiceConfiguration.class);

	private final XWikiServiceProperties xWikiProperties;

	public XWikiServiceConfiguration(XWikiServiceProperties xWikiProps) {
		this.xWikiProperties = xWikiProps;
	}
	
	
//	@Bean
//	RestClient restClient(RestClient.Builder builder) {
//		String auth = xWikiProperties.getUsername() + ":" + xWikiProperties.getPassword();
//		
//		RestClient restClient = builder
//				.baseUrl(xWikiProperties.getBaseUrl())
//				.defaultHeader("Authorization", "Basic "+Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII"))))
//				.build();
//		logger.info("RestClient Bean created with basic authentication to " + xWikiProperties.getBaseUrl());
//		return restClient;
//		
//	}
	
	@Bean
	RestTemplate restTemplate(RestTemplateBuilder builder) {
		RestTemplate restTemplate =  builder.basicAuthentication(xWikiProperties.getUsername(), xWikiProperties.getPassword()).build();
		//restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(xWikiProperties.getApiEntrypoint()));
		logger.info("RestTemplate Bean created with basic authentication to " + xWikiProperties.getBaseUrl());
		return restTemplate;
	}

	
	@Bean
	XWikiService getRestService(RestTemplate restTemplate, XWikiServiceProperties xWikiProperties) {
		XWikiService pageService = null;
		try {
			pageService = new XWikiService(restTemplate, xWikiProperties);
		} catch(Exception e) {
			logger.error("Unable to create XWikiService as bean. error message {}", e.getMessage());
		}
		return pageService;
	}
	
	@Bean
	XwikiAuthenticationProvider getAuthenticationProvider(XWikiService xwikiService) {
		return new XwikiAuthenticationProvider(xwikiService);
	}
	
}
