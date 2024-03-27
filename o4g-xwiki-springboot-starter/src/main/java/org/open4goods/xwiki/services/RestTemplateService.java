package org.open4goods.xwiki.services;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import org.open4goods.xwiki.config.XWikiConstantsResourcesPath;
import org.open4goods.xwiki.config.XWikiServiceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Http services to request the XWiki server
 * 
 * @author Thierry.Ledan
 *
 */
public class RestTemplateService {


	private RestTemplate restTemplate;
	private RestTemplate webTemplate;
	private XWikiServiceProperties properties;
	private XWikiConstantsResourcesPath resourcesPathManager;
	
	private static Logger logger = LoggerFactory.getLogger(RestTemplateService.class);
	
	public RestTemplateService(RestTemplate restTemplate, RestTemplate webTemplate, XWikiServiceProperties properties) {
		this.restTemplate = restTemplate;
		this.webTemplate = webTemplate;
		this.properties = properties;
		this.resourcesPathManager = new XWikiConstantsResourcesPath(this.properties.getBaseUrl(), this.properties.getApiEntrypoint(), this.properties.getApiWiki());
	}
	
	/**
	 * Return Response from a REST service endpoint if status code equals to 2xx
	 * Null otherwise (exception, status code not equals to 2xxx)
	 * @param endpoint
	 * @return Response if status code equals to 2xxx, null otherwise
	 */
	public  ResponseEntity<String> getRestResponse( String endpoint ){

		ResponseEntity<String> response = null;
		// first clean url: url decoding, check scheme and add query params if needed
		String updatedEndpoint = cleanUrl(endpoint);
		logger.info("request xwiki server with endpoint {}", updatedEndpoint);

		if(updatedEndpoint != null) {
			try {
				response = restTemplate.getForEntity(updatedEndpoint, String.class);
			} catch(RestClientException rec) {
				logger.warn("RestClientException exception  - uri:{} - error:{}", updatedEndpoint, rec.getMessage());
			} catch(Exception e) {
				logger.warn("Exception while trying to reach endpoint:{} - error:{}", updatedEndpoint, e.getMessage());
			}
			// check response status code
			if (null != response && ! response.getStatusCode().is2xxSuccessful()) {
				logger.warn("Response returns with status code:{} - for uri:{}", response.getStatusCode(), updatedEndpoint);
				response = null;
			} 
		}
		return response;
	}
	

	/**
	 * 
	 * @param viewUrl
	 * @return
	 */
	public ResponseEntity<String> getWebResponse( String xwikiWebUrl ){

		ResponseEntity<String> response = null;
		logger.info("request xwiki web server with url {}", xwikiWebUrl);
		if(xwikiWebUrl != null) {
			try {
				response = webTemplate.getForEntity(xwikiWebUrl, String.class);
			} catch(Exception e) {
				logger.warn("Exception while trying to reach url:{} - error:{}", xwikiWebUrl, e.getMessage());
			}
			// check response status code
			if (null != response && ! response.getStatusCode().is2xxSuccessful()) {
				logger.warn("Response returns with status code:{} - for uri:{}", response.getStatusCode(), xwikiWebUrl);
				response = null;
			} 
		}
		return response;
	}
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public ResponseEntity<byte[]> downloadAttachment(String url) {
		
		ResponseEntity<byte[]> response = null;
		if(url != null) {
			try {
				response = webTemplate.getForEntity(url, byte[].class);
			} catch(Exception e) {
				logger.warn("Exception while trying to reach url:{} - error:{}", url, e.getMessage());
			}
			// check response status code
			if (null != response && ! response.getStatusCode().is2xxSuccessful()) {
				logger.warn("Response returns with status code:{} - for uri:{}", response.getStatusCode(), url);
				response = null;
			} 
		}
		
		return response;
		
	}
	
	
	/**
	 * Clean url: 
	 * 				URLDecoding  
	 * 				Force https if httpsOnly set
	 * 				Add 'media' query param from properties
	 * @param url
	 * @param key
	 * @param value
	 * @return this url with query param key=value if process succedded, null otherwise
	 */
	public String cleanUrl(String url) {

		String clean = "";
		try {
			if( url != null ) {
				String secureUrl = updateUrlScheme(url);
				String decoded = URLDecoder.decode(secureUrl, Charset.defaultCharset());
				// add request param if needed
				clean = addQueryParam(decoded, "media", this.properties.getMedia());
			}
		} catch (Exception e) {
			logger.warn("Exception while updating url {}. Error Message: {}",url,  e.getMessage());
		}
		return clean;
	}


	/**
	 * Update http scheme in https if needed
	 * @param url
	 * @return
	 */
	public String updateUrlScheme(String url) {

		String updated = url;
		if(url != null && this.properties.isHttpsOnly()) {
			updated = url.replaceFirst("http:", "https:");
		}
		return updated;
	}

	/**
	 * Add query params to url
	 * @param url
	 * @param key
	 * @param value
	 * @return this url with query param key=value if process succedded, null otherwise
	 */
	public String addQueryParam(String url, String key, String value) {

		String uriWithParams = null;
		try{
			URI uri = UriComponentsBuilder.fromUriString(url)
					.queryParam(key, value)
					.build()
					.toUri();

			if(uri != null) {
				// decode again after adding query params
				uriWithParams = URLDecoder.decode(uri.toString(), Charset.defaultCharset());
			}

		} catch(Exception e) {
			logger.warn("Unable to add query params {}={} to uri {}. Error Message:{}",key, value, url, e.getMessage());
		}       
		return uriWithParams;

	}
}
