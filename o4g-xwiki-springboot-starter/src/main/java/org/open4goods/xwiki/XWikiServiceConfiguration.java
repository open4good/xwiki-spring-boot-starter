package org.open4goods.xwiki;


import org.open4goods.xwiki.authentication.XwikiAuthenticationProvider;
import org.open4goods.xwiki.config.XWikiServiceProperties;
import org.open4goods.xwiki.services.XWikiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * This class handles xwiki services Beans
 * @author Thierry.Ledan
 */
@AutoConfiguration
@EnableConfigurationProperties(XWikiServiceProperties.class)
public class XWikiServiceConfiguration {
	
	private static final Logger logger = LoggerFactory.getLogger(XWikiServiceConfiguration.class);

	private XWikiServiceProperties xWikiProperties;

	@Autowired RestTemplateBuilder localRestTemplateBuilder;
	
	
	public XWikiServiceConfiguration(XWikiServiceProperties xWikiProps) {
		this.xWikiProperties = xWikiProps;
	}

	@Bean( name = "xwikiAuthenticationProvider" )
	XwikiAuthenticationProvider getAuthenticationProvider(@Autowired XWikiService xwikiService) {
		return new XwikiAuthenticationProvider(xwikiService);
	}
	
	/**
	 * restTemplate dedicated to restful api request
	 * @param builder
	 * @return
	 */
	@Bean( name = "restTemplate" )
	RestTemplate restTemplate(RestTemplateBuilder builder) {
		
		RestTemplate restTemplate =  builder.basicAuthentication(xWikiProperties.getUsername(), xWikiProperties.getPassword()).build();
		//restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(xWikiProperties.getApiEntrypoint()));
		logger.info("RestTemplate created with basic authentication to request XWIKI RESTFUL API SERVER");
		return restTemplate;
	}

	/**
	 * restTemplate dedicated to web request 
	 * @param builder
	 * @return
	 */
	@Bean( name = "webTemplate" )
	RestTemplate webTemplate(RestTemplateBuilder builder) {
		
		RestTemplate webTemplate =  
				builder.basicAuthentication(xWikiProperties.getUsername(), xWikiProperties.getPassword()).
				defaultHeader("accept", "text/html ").
				build();
		logger.info("WebTemplate created with basic authentication and headers to request XWIKI WEB SERVER");
		return webTemplate;
	}
	
	/**
	 * 
	 * @param restTemplate
	 * @param webTemplate
	 * @param xWikiProperties
	 * @return
	 */
	@Bean( name = "xwikiRestService" )
	XWikiService getXwikiRestService( 
			@Qualifier("restTemplate") RestTemplate restTemplate, 
			@Qualifier("webTemplate") RestTemplate webTemplate
			
			
			
			) {
		
		XWikiService xwikiService = null;
		try {
			xwikiService = new XWikiService(restTemplate, webTemplate, xWikiProperties, localRestTemplateBuilder);
		} catch(Exception e) {
			  logger.error("Unable to create XWikiService as bean. error message {}", e.getMessage());
		}
		return xwikiService;
	}
	
	
	

//	@Bean
// TODO  : Test with RestClient or remove it
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
}
