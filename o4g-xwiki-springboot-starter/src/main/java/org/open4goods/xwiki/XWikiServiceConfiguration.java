package org.open4goods.xwiki;


import org.open4goods.xwiki.authentication.XwikiAuthenticationProvider;
import org.open4goods.xwiki.config.XWikiServiceProperties;
import org.open4goods.xwiki.services.MappingService;
import org.open4goods.xwiki.services.RestTemplateService;
import org.open4goods.xwiki.services.XWikiAuthenticationService;
import org.open4goods.xwiki.services.XWikiReadService;
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
 * This Configuration class handles :
 * 		xwiki services Beans
 * 		restTemplates Beans for specific call
 * 
 * @author Thierry.Ledan
 */
@AutoConfiguration
@EnableConfigurationProperties(XWikiServiceProperties.class)
public class XWikiServiceConfiguration {
	
	private static final Logger logger = LoggerFactory.getLogger(XWikiServiceConfiguration.class);

	private XWikiServiceProperties xWikiProperties;

	@Autowired 
	RestTemplateBuilder localRestTemplateBuilder;
	
	
	public XWikiServiceConfiguration(XWikiServiceProperties xWikiProps) {
		this.xWikiProperties = xWikiProps;
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
//	@Bean( name = "xwikiRestService" )
//	XWikiService getXwikiRestService( 
//			@Qualifier("restTemplate") RestTemplate restTemplate, 
//			@Qualifier("webTemplate") RestTemplate webTemplate
//			) {
//		
//		XWikiService xwikiService = null;
//		try {
//			xwikiService = new XWikiService(restTemplate, webTemplate, xWikiProperties, localRestTemplateBuilder);
//		} catch(Exception e) {
//			  logger.error("Unable to create XWikiService as bean. error message {}", e.getMessage());
//		}
//		return xwikiService;
//	}
	
	/**
	 * restTemplate services
	 * manage specific restTemplate instances with preset headers for following purposes:
	 *  -> requesting the xwiki rest api
	 *  -> requesting the xwiki server for html response
	 *  -> 
	 * 
	 * @param restTemplate
	 * @param webTemplate
	 * @return
	 */
	@Bean( "restTemplateService" )
	RestTemplateService getRestTemplateService( 
			@Qualifier("restTemplate") RestTemplate restTemplate, 
			@Qualifier("webTemplate") RestTemplate webTemplate
			) {
		
		RestTemplateService restTemplateService = null;
		try {
			restTemplateService = new RestTemplateService(restTemplate, webTemplate, xWikiProperties);
		} catch(Exception e) {
			  logger.error("Unable to create XWikiService as bean. error message {}", e.getMessage());
		}
		return restTemplateService;
	}
	
	/**
	 * xwiki objects mapping 
	 * 
	 * @param mappingTemplate
	 * @return
	 */
	@Bean( "mappingService" )
	MappingService getMappingervice( 
			@Qualifier("restTemplateService") RestTemplateService restTemplateService
			) {
		
		MappingService mappingService = null;
		try {
			mappingService = new MappingService(restTemplateService, xWikiProperties);
		} catch(Exception e) {
			  logger.error("Unable to create XWikiService as bean. error message {}", e.getMessage());
		}
		return mappingService;
	}	
	
	/**
	 * rest READ Services 
	 * 
	 * @param mappingTemplate
	 * @return
	 */
	@Bean( name = "xwikiReadService" )
	XWikiReadService getXwikiReadService( 
			@Qualifier("mappingService") MappingService mappingTemplate 
			) {
		
		XWikiReadService XWikiReadService = null;
		try {
			XWikiReadService = new XWikiReadService(mappingTemplate, xWikiProperties);
		} catch(Exception e) {
			  logger.error("Unable to create XWikiService as bean. error message {}", e.getMessage());
		}
		return XWikiReadService;
	}

	/**
	 * Services related to authentication
	 * 
	 * @param mappingTemplate
	 * @param restTemplateService
	 * @return
	 */
	@Bean( name = "xwikiAuthenticationService" )
	XWikiAuthenticationService getXwikiAuthenticationService( 
			@Qualifier("mappingService") MappingService mappingTemplate,
			@Qualifier("restTemplateService") RestTemplateService restTemplateService
			) {
		
		XWikiAuthenticationService xWikiAuthenticationService = null;
		try {
			xWikiAuthenticationService = new XWikiAuthenticationService(mappingTemplate, restTemplateService, xWikiProperties, localRestTemplateBuilder);
		} catch(Exception e) {
			  logger.error("Unable to create XWikiService as bean. error message {}", e.getMessage());
		}
		return xWikiAuthenticationService;
	}
	
	
	/**
	 * Basic authentication provider
	 * 
	 * @param xwikiService
	 * @return
	 */
	@Bean( name = "xwikiAuthenticationProvider" )
	XwikiAuthenticationProvider getAuthenticationProvider(@Autowired XWikiAuthenticationService xwikiAuthService) {
		return new XwikiAuthenticationProvider(xwikiAuthService);
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
