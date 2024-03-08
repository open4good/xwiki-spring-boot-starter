package org.open4goods.xwiki.services;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.open4goods.xwiki.config.XWikiRelations;
import org.open4goods.xwiki.config.XWikiServiceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.xwiki.rest.model.jaxb.Attachment;
import org.xwiki.rest.model.jaxb.Attachments;
import org.xwiki.rest.model.jaxb.Link;
import org.xwiki.rest.model.jaxb.ObjectSummary;
import org.xwiki.rest.model.jaxb.Objects;
import org.xwiki.rest.model.jaxb.Page;
import org.xwiki.rest.model.jaxb.Pages;
import org.xwiki.rest.model.jaxb.Properties;
import org.xwiki.rest.model.jaxb.Property;
import org.xwiki.rest.model.jaxb.SearchResult;
import org.xwiki.rest.model.jaxb.SearchResults;
import org.xwiki.rest.model.jaxb.Wiki;
import org.xwiki.rest.model.jaxb.Wikis;
import org.xwiki.rest.model.jaxb.Xwiki;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import io.micrometer.common.util.StringUtils;

/**
 * This helper handles connection to xwiki server and mapping xwiki object from json response
 * 
 * @author Thierry.Ledan
 */
public class XWikiServiceHelper {

	private final Logger logger = LoggerFactory.getLogger(XWikiServiceHelper.class);

	private RestTemplate restTemplate;
	private XWikiServiceProperties xWikiProps;	

	/**
	 * Used for login in order to create a local RestTemplate object
	 */

	public XWikiServiceHelper(RestTemplate restTemplate, XWikiServiceProperties xWikiProperties) {
		this.restTemplate = restTemplate;
		this.xWikiProps = xWikiProperties;
	}



	/**
	 * Return Response from a REST service endpoint if status code equals to 2xx
	 * Null otherwise (exception, status code not equals to 2xxx)
	 * @param endpoint
	 * @return Response if status code equals to 2xxx, null otherwise
	 */
	public ResponseEntity<String> getRestResponse( String endpoint ){

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
	 * Try to get a Page Object from userEndpoint.
	 * With a specific process if http status code equals 4xx
	 * 
	 * Use a specific resTemplate with user credentials.
	 * 
	 * 
	 * @param endpoint
	 * @return userPage if request succeeds, null otherwise
	 * @throws HttpClientErrorException if non authorized
	 */
	public Page login( RestTemplateBuilder builder,  String userEndpoint, String user, String password ) throws HttpClientErrorException{

		RestTemplate localRestTemplate =  builder.basicAuthentication(user, password).build();
		ResponseEntity<String> response = null;
		Page userPage = null;
		// first clean url: url decoding, check scheme and add query params if needed
		String updatedEndpoint = cleanUrl(userEndpoint);
		logger.info("request xwiki server with endpoint {}", updatedEndpoint);

		if(updatedEndpoint != null) {
			try {
				response = localRestTemplate.getForEntity(updatedEndpoint, String.class);
			} catch(RestClientException rec) {
				logger.warn("RestClientException exception  - uri:{} - error:{}", updatedEndpoint, rec.getMessage());
			} catch(Exception e) {
				logger.warn("Exception while trying to reach endpoint:{} - error:{}", updatedEndpoint, e.getMessage());
			}
			// check response status code
			if (null != response ) {
				if( response.getStatusCode().is4xxClientError() )  {
					throw new HttpClientErrorException(response.getStatusCode());
				} else if(! response.getStatusCode().is2xxSuccessful()) {
					logger.warn("Response returns with status code:{} - for uri:{}", response.getStatusCode(), updatedEndpoint);
					response = null;
				} else {
					userPage = mapPage(updatedEndpoint);
				}
			} 
		}
		return userPage; 
	}

	/**
	 * Map 'Pages' object from json response endpoint
	 * @param String endpoint 
	 * @return a 'Pages' object if response and mapping were successful, null otherwise
	 */
	public Pages mapPages(String endpoint) {

		Pages pages = null;
		// get response from rest server
		ResponseEntity<String> response = getRestResponse(endpoint);
		if (response != null ) {
			pages = deserializePages(response);
		} 
		return pages;
	}

	/**
	 * Map 'Page' object from json response endpoint
	 * @param String endpoint 
	 * @return a 'Page' object if response and mapping were successful, null otherwise
	 */
	public Page mapPage(String endpoint) {

		Page page = null;
		// get response from rest server
		ResponseEntity<String> response = getRestResponse(endpoint);
		if (response != null ) {
			page = deserializePage(response);
		} 
		return page;
	}


	/**
	 * Map 'SearchResults' object from json response endpoint
	 * @param String endpoint 
	 * @return a 'SearchResults' object if response and mapping were successful, null otherwise
	 */
	public SearchResults mapSearchResults(String endpoint) {

		SearchResults results = null;
		ResponseEntity<String> response = getRestResponse(endpoint);
		if( response != null ) {
			results = deserializeSearchResults(response);
		}
		return results;
	}


	/**
	 * Get properties related to 'page'
	 * @param page
	 * @return page's properties
	 */
	public Map<String, String> getProperties(Page page){

		Map<String, String> properties = new HashMap<String, String>();
		Properties propertiesObject = new Properties();
		Objects objects = page.getObjects();
		if( objects == null ) {
			// request to server 
			objects = getPageObjects(page);
		} 
		if( objects != null ) {
			// TODO: hard coding - objects.getObjectSummaries().get(0).getLinks() NOT GOOD , try to find the good summary !!
			String pagePropsEndpoint = getHref(XWikiRelations.REL_PROPERTIES, objects.getObjectSummaries().get(0).getLinks());

			ResponseEntity<String> response = getRestResponse(pagePropsEndpoint);
			if( response != null ) {
				propertiesObject = deserializeProperties(response);
				if( propertiesObject != null ) {
					for(Property prop: propertiesObject.getProperties()) {
						properties.put(prop.getName(), prop.getValue());
					}
				}
			}
		}
		return properties;
	}


	/**
	 *
	 * Get properties related to an object of class 'className' in the ObjectSummary list
	 * 
	 * @param page
	 * @return page's properties
	 */
	public Map<String, String> getProperties(Objects objects, String className){

		Map<String, String> properties = new HashMap<String, String>();
		Properties propertiesObject = new Properties();

		String propertiesEndpoint = null;
		if( objects != null && objects.getObjectSummaries() != null && ! objects.getObjectSummaries().isEmpty() ) {
			for( ObjectSummary object: objects.getObjectSummaries() ) {
				if( object.getClass().equals(className) ) {
					propertiesEndpoint = getHref(XWikiRelations.REL_PROPERTY, object.getLinks());
					break;
				}
			}
		}
		// and finally fetch properties
		if( StringUtils.isNotBlank(propertiesEndpoint) ) {
			ResponseEntity<String> response = getRestResponse(propertiesEndpoint);
			if( response != null ) {
				propertiesObject = deserializeProperties(response);
				if( propertiesObject != null ) {
					for(Property prop: propertiesObject.getProperties()) {
						properties.put(prop.getName(), prop.getValue());
					}
				}
			}
		}
		return properties;
	}




	/**
	 * fetch value for page's field 'objects'
	 * @param page
	 * @return
	 */
	public Objects getPageObjects(Page page) {

		String objectsUrl = getHref(XWikiRelations.REL_OBJECTS, page.getLinks());
		return getObjects(objectsUrl);
	}


	/**
	 * fetch value for page's field 'objects'
	 * @param page
	 * @return
	 */
	public Objects getObjects(String pageEndpoint) {

		Objects objects = null;
		if( StringUtils.isNotEmpty(pageEndpoint) ) {
			ResponseEntity<String> response = getRestResponse(pageEndpoint);
			if( response != null ) {
				try {
					ObjectMapper mapper = new ObjectMapper();
					objects = mapper.readValue(response.getBody(),new TypeReference<Objects>(){});
					logger.debug("Object 'Objects' mapped correctly}");
				}
				catch(Exception e) {
					logger.warn("Unable to map 'Objects' object from json. Error Message:'", e.getMessage());
					logger.warn("Unable to map 'Objects' object from json:{}", response.getBody());
				}
			}
		}
		return objects;
	}

	/**
	 * 
	 * @param page
	 * @return
	 */
	public Attachments getAttachments (Page page) {

		Attachments attachments = null;
		// get the url
		String attachementsUrl = getHref(XWikiRelations.REL_ATTACHMENTS, page.getLinks());
		if( StringUtils.isNotBlank(attachementsUrl) ) {
			ResponseEntity<String> response = null;
			response = getRestResponse(attachementsUrl);
			if( response != null ) {
				try {
					ObjectMapper mapper = new ObjectMapper();
					attachments = mapper.readValue(response.getBody(),new TypeReference<Attachments>(){});
				}
				catch(Exception e) {
					logger.warn("Unable to map 'Attachments' object from json. Error Message:'", e.getMessage());
					logger.warn("Unable to map 'Attachments' object from json:{}", response.getBody());
				}
			}
		}
		return attachments;
	}


	/**
	 * Retrieve attachments associated to a Page
	 * @param response
	 * @return the Attachments list related to Page, empty list if no attachment
	 */
	public List<Attachment> getAttachmentList (Page page) {

		Attachments attachments;
		List<Attachment> attachmentsList = new ArrayList<Attachment>();
		// get the url
		String attachementsUrl = getHref(XWikiRelations.REL_ATTACHMENTS, page.getLinks());
		if( StringUtils.isNotBlank(attachementsUrl) ) {
			ResponseEntity<String> response = null;
			response = getRestResponse(attachementsUrl);
			if( response != null ) {
				try {
					ObjectMapper mapper = new ObjectMapper();
					attachments = mapper.readValue(response.getBody(),new TypeReference<Attachments>(){});
					if( attachments != null ) {
						attachmentsList = attachments.getAttachments();
						logger.debug("Object 'Attachments' mapped correctly}");
					}
				}
				catch(Exception e) {
					logger.warn("Unable to map 'Attachments' object from json. Error Message:'", e.getMessage());
					logger.warn("Unable to map 'Attachments' object from json:{}", response.getBody());
				}
			}
		}
		return attachmentsList;
	}


	/**
	 * return attachment url (../bin/download/....)
	 * @param attachment
	 * @return
	 */
	public String getAttachmentUrl(Attachment attachment) {

		String absoluteUrl = attachment.getXwikiAbsoluteUrl();
		return updateUrlScheme(absoluteUrl);	 
	}


	/**
	 * Retrieve available wikis
	 * @param endpoint
	 * @return
	 */
	public List<Wiki> getAllWikis( String endpoint ){

		List<Wiki> wikisList = new ArrayList<Wiki>();
		ResponseEntity<String> response = getRestResponse(endpoint);
		if(response != null) {
			Wikis wikis = deserializeWikis(response);
			if(wikis != null) {
				wikisList = wikis.getWikis();
			}
		}
		return wikisList;
	}

	/**
	 * Deserialize json response to 'SearchResults' object
	 * @param response
	 * @return a 'SearchResults' object if the mapping was successful, null otherwise
	 */
	public SearchResults deserializeSearchResults(ResponseEntity<String> response) {

		SearchResults results = null;	
		try {
			ObjectMapper mapper = new ObjectMapper();
			results = mapper.readValue(response.getBody(), new TypeReference<SearchResults>() {});
		} catch( Exception e ) {
			ManageMappingExceptions(e, "SearchResults", response.getBody());
		}
		return results;
	}

	/**
	 * Deserialize json response to 'SearchResult' object
	 * @param response
	 * @return a 'SearchResult' object if the mapping was successful, null otherwise
	 */
	public SearchResult deserializeSearchResult(ResponseEntity<String> response) {

		SearchResult result = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			result = mapper.readValue(response.getBody(), new TypeReference<SearchResult>() {});
		} catch( Exception e ) {
			ManageMappingExceptions(e, "SearchResult", response.getBody());
		}
		return result;
	}

	/**
	 * Deserialize json response to 'Pages' object
	 * @param response
	 * @return a 'Pages' object if the mapping was successful, null otherwise
	 */
	public Pages deserializePages(ResponseEntity<String> response) {

		Pages pages = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			pages = mapper.readValue(response.getBody(),new TypeReference<Pages>(){});
			logger.debug("Object 'Pages' mapped correctly}");
		}
		catch(Exception e) {
			ManageMappingExceptions(e, "Pages", response.getBody());
		}
		return pages;
	}

	/**
	 * Deserialize json response to 'Page' object
	 * @param response
	 * @return a 'Page' object if the mapping was successful, null otherwise
	 */
	public Page deserializePage(ResponseEntity<String> response) {

		Page page = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			page = mapper.readValue(response.getBody(),new TypeReference<Page>(){});
			logger.debug("Object 'Page' mapped correctly}");
		}
		catch(Exception e) {
			ManageMappingExceptions(e, "Page", response.getBody());
		}
		return page;
	}


	/**
	 * Deserialize json response to 'Properties' object
	 * @param response
	 * @return a 'Properties' object if the mapping was successful, null otherwise
	 */
	private Properties deserializeProperties(ResponseEntity<String> response) {		

		Properties properties = null;
		if( response != null ) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				properties = mapper.readValue(response.getBody(),new TypeReference<Properties>(){});
				logger.debug("Object 'Map<String,String>' mapped correctly}");
			}
			catch(Exception e) {
				ManageMappingExceptions(e, "Map<String,String>", response.getBody());
			}
		}
		return properties;	
	}

	/**
	 * Deserialize json response to 'Xwiki' object
	 * @param response
	 * @return a 'Xwiki' object if the mapping was successful, null otherwise
	 */
	public Xwiki deserializeXwiki(ResponseEntity<String> response) {

		Xwiki xWiki = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			xWiki = mapper.readValue(response.getBody(),new TypeReference<Xwiki>(){});
			logger.debug("Object 'Xwiki' mapped correctly}");
		}	
		catch(Exception e) {
			ManageMappingExceptions(e, "Xwiki", response.getBody());
		}
		return xWiki;
	}

	/**
	 * Deserialize json response to 'Wikis' object
	 * @param response
	 * @return a 'Wikis' object if the mapping was successful, null otherwise
	 */
	public Wikis deserializeWikis(ResponseEntity<String> response) {

		Wikis wikis = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			wikis = mapper.readValue(response.getBody(),new TypeReference<Wikis>(){});
			logger.debug("Object 'Wikis' mapped correctly}");
		}
		catch(Exception e) {
			ManageMappingExceptions(e, "Wikis", response.getBody());
		}
		return wikis;
	}

	/**
	 * Deserialize json response to 'Wiki' object
	 * @param response
	 * @return a 'Wiki' object if the mapping was successful, null otherwise
	 */
	public Wiki deserializeWiki(ResponseEntity<String> response) {

		Wiki wikis = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			wikis = mapper.readValue(response.getBody(),new TypeReference<Wiki>(){});
			logger.debug("Object 'Wiki' mapped correctly}");
		}
		catch(Exception e) {
			ManageMappingExceptions(e, "Wiki", response.getBody());
		}
		return wikis;
	}


	/**
	 * Return true if 'wiki' exists in available wikis list
	 * @param wiki
	 * @return
	 */
	public boolean checkWikiExists(String targetWiki) {

		boolean exists = false;
		Xwiki xwiki = null;
		String wikisHref = null;
		// get response from rest server
		String endpoint = xWikiProps.getApiEntrypoint();
		ResponseEntity<String> response = getRestResponse(endpoint);
		if (response != null ) {
			xwiki = deserializeXwiki(response);
			if(xwiki != null) {
				for(Link link: xwiki.getLinks()) {
					if(link.getRel().equals(XWikiRelations.REL_WIKIS)) {
						wikisHref = link.getHref();
						break;
					}
				}
				// get available wikis
				List<Wiki> wikis = getAllWikis(wikisHref);
				if(wikis != null) {
					for(Wiki wiki: wikis) {
						if(wiki.getName().equals(targetWiki)) {
							exists = true;
							break;
						}
					}
				}
			}
		} 
		return exists;
	}
	/**
	 * 
	 * @param rel
	 * @return 'href' link from 'rel' link
	 */
	public String getHref(String rel, List<Link> links) {

		String href = null;
		try {

			for(Link link: links) {
				if(link.getRel().equals(rel)) {
					href = link.getHref();
				} 
			}
		} catch(Exception e) {
			logger.warn("Exception while retrieving 'href' from link {}. Error Message: {}",rel,  e.getMessage());
		}
		return href;
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
				clean = addQueryParam(decoded, "media", xWikiProps.getMedia());
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
		if(url != null && xWikiProps.isHttpsOnly()) {
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


	/**
	 * Manage error messages depending on exception type
	 * @param e the exception
	 * @param typeToMap the Type to Map
	 * @param responseBody rest response body
	 */
	private void ManageMappingExceptions(Exception e, String typeToMap, String responseBody) {

		if(e instanceof UnrecognizedPropertyException) {
			logger.warn("Unable to map '{}' object from json: Unrecognized property:'{}' in Object:'{}'. Known properties:'{}'", 
					typeToMap,
					((UnrecognizedPropertyException)e).getPropertyName(),
					((UnrecognizedPropertyException)e).getReferringClass().toString(), 
					((UnrecognizedPropertyException)e).getKnownPropertyIds().toString());
		} else {
			logger.warn("Unable to map '{}' object from json. Error Message:", 
					typeToMap,
					e.getMessage());
			logger.warn("Unable to map '{}' object from json:{}", 
					typeToMap,
					responseBody);
		}
	}
}
