package org.open4goods.xwiki.services;


import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.open4goods.xwiki.config.XWikiConstantsRelations;
import org.open4goods.xwiki.config.XWikiConstantsResourcesPath;
import org.open4goods.xwiki.config.XWikiServiceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.xwiki.rest.model.jaxb.Attachment;
import org.xwiki.rest.model.jaxb.Attachments;
import org.xwiki.rest.model.jaxb.ObjectSummary;
import org.xwiki.rest.model.jaxb.Objects;
import org.xwiki.rest.model.jaxb.Page;
import org.xwiki.rest.model.jaxb.PageSummary;
import org.xwiki.rest.model.jaxb.Pages;
import org.xwiki.rest.model.jaxb.SearchResult;
import org.xwiki.rest.model.jaxb.SearchResults;


/**
 * This service handles XWiki READ rest services 
 * 
 * @author Thierry.Ledan
 */            

public class XWikiReadService {

	private static Logger LOGGER = LoggerFactory.getLogger(XWikiReadService.class);
	
	private XWikiServiceProperties xWikiProperties;
	private XWikiConstantsResourcesPath resourcesPathManager;
	private MappingService mappingService;
	private RestTemplateService restTemplateService;
	
	public XWikiReadService (MappingService mappingService, RestTemplateService restTemplateService, XWikiServiceProperties xWikiProperties) throws Exception {
		
		this.xWikiProperties = xWikiProperties;
		this.mappingService = mappingService;
		this.restTemplateService = restTemplateService;
		
		this.resourcesPathManager = new XWikiConstantsResourcesPath(xWikiProperties.getBaseUrl(), xWikiProperties.getApiEntrypoint(), xWikiProperties.getApiWiki());

		//		TODO: check that wiki exists !!
		
//		// get all available wikis and check that the targeted one exists
//		if( xWikiProperties.getApiWiki() == null || ! helper.checkWikiExists(xWikiProperties.getApiWiki()) ) {
//			throw new Exception("The targeted wiki '" + xWikiProperties.getApiWiki() + "' does not exist");
//		}
	}
	
//	public String getBaseUrl() {
//		return resourcesPathManager.getBaseUrl();
//	}
	
	/**
	 * 
	 * @param space
	 * @param name
	 * @return
	 */
	public Page getPage(String space, String name) {
		Page page = null;
		String endpoint = resourcesPathManager.getPageEndpoint(space, name);	
		page = this.mappingService.mapPage(endpoint);
		return page;
	}

	/**
	 * 
	 * @param space
	 * @return
	 */
	public Pages getPages(String space) {
		String uri = resourcesPathManager.getPagesEndpoint(space);
		return this.mappingService.mapPages(uri);
	}
		
	/**
	 * Retrieve all 'Page' associated to a space
	 * with properties and attachments (disabled as default)
	 * @param space
	 * @return A List of 'Page' object, could be empty, never null
	 */
	public List<Page> getPagesList(String space) {
		
		Pages pages = null;
		List<Page> pagesList = new ArrayList<Page>();
		String endpoint = resourcesPathManager.getPagesEndpoint(space);
		
		pages = this.mappingService.mapPages(endpoint);
		
		// Loop on PageSummary list in order to create Page list
		if( pages != null && !pages.getPageSummaries().isEmpty() ) {
			
			Page tempPage = null;
			
			for(PageSummary p: pages.getPageSummaries()) {
				
				String pageUrl = null;
				
				// get page endpoint
				pageUrl =  this.mappingService.getHref(XWikiConstantsRelations.REL_PAGE, p.getLinks());
				
				// add request param to return fields that are disabled by default
				// TODO: mapping issue: field 'properties' does not exists in the object 'ObjectSummary'
				if( tempPage != null ) {
					pagesList.add(tempPage);

					// fetch attachments
					Attachments attachments = this.mappingService.getAttachments(tempPage);
					if( attachments != null && attachments.getAttachments() != null && attachments.getAttachments().size()  > 0 ) {
						// update url (scheme, query params..) according to starter properties
						for(Attachment attachment: attachments.getAttachments()) {
							
							// TODO: updateUrlScheme a déplacer ou déclarer ailleurs que dans restTemplateService
							attachment.setXwikiAbsoluteUrl(this.restTemplateService.updateUrlScheme(attachment.getXwikiAbsoluteUrl()));
							attachment.setXwikiRelativeUrl(this.restTemplateService.updateUrlScheme(attachment.getXwikiRelativeUrl()));
						}
						tempPage.setAttachments(attachments);
					}	
					
					// fetch objects (properties, ...)
					Objects objects = this.mappingService.getPageObjects(tempPage);
					if( objects != null ) {
						tempPage.setObjects(objects);
					}
					
					// fetch classes
					
				}
			}
		}
		return pagesList;
	}
	
	
	/**
	 * Get properties related to Page with name 'pageName' in space 'spaceName'
	 * 
	 * @param spaceName space related to 'page'
	 * @param pageName name of 'page'
	 * @return
	 */
	public Map<String,String> getProperties(String spaceName, String pageName) {
		Map<String,String> props = new HashMap<String, String>();
		String endpoint = resourcesPathManager.getPageEndpoint(spaceName, pageName);
		Page page = this.mappingService.mapPage(endpoint);
		if(page != null) {
			props = this.mappingService.getProperties(page);
		}
		return props;
	}
	
	/**
	 * Get properties from Page 'page'
	 * 
	 * @param page
	 * @return 
	 */
	public Map<String,String> getProperties(Page page) {
		Map<String,String> props = new HashMap<String, String>();
		if(page != null) {
			props = this.mappingService.getProperties(page);
		}
		return props;
	}
	
	
	//////////////////////////////
	//							// 									
	//  USERS - GROUPS - ROLES	//
	//							// 
	//////////////////////////////
	
	/**
	 * Get all groups pageName
	 * discard "XWikiGroupTemplate"  
	 * TODO : XwikiAuthService
	 * @return
	 */
	public List<String> getGroupsName(){
		List<String> groups = new ArrayList<String>();
		SearchResults results = this.mappingService.mapSearchResults(resourcesPathManager.getGroupsEndpoint());
		if( results != null && !results.getSearchResults().isEmpty()) {
			for(SearchResult result: results.getSearchResults()) {
				if( !result.getPageName().contains("XWikiGroupTemplate") ) {
					groups.add(result.getPageName());
				}
			}
		}
		return groups;
	}
	
	
	/**
	 * Get users name for a group
	 * 
	 * scan objects summary, user's name is set in field "headline" with the prefix "XWiki."
	 * TODO : XwikiAuthenticationService
	 * @param groupPageName
	 * @return
	 */
	public List<String> getGroupUsers(String groupPageName) {
		// https://wiki.nudger.fr/rest/wikis/xwiki/spaces/XWiki/pages/SiteEditor/objects?media=json
		List<String> users = new ArrayList<String>();
		Objects objects = this.mappingService.getObjects(resourcesPathManager.getGroupUsers(groupPageName));
		if( objects != null && ! objects.getObjectSummaries().isEmpty() ) {
			for( ObjectSummary objectsummary: objects.getObjectSummaries() ) {
				if( ! objectsummary.getHeadline().isEmpty() ) {
					users.add(objectsummary.getHeadline().replaceAll("XWiki.", ""));
				}
			}
		}
		return users;
	}
	
	
	/**
	 * Get the User (Page object in xwiki) from username
	 * 
	 * @param userName
	 * @return
	 */
	public Page getUser(String userName) {
		Page page = null;
		String endpoint = resourcesPathManager.getPageEndpoint("", userName);	
		page = this.mappingService.mapPage(endpoint);
		return page;
	}
	
	

	
	/**
	 * Get the user's properties
	 * 
	 * @param userName
	 * @return
	 */
	public Map<String,String> getUserProperties(Page user) {
		Map<String,String> properties = new HashMap<String, String>();
		if( user != null ) {
			// first get objects from Page
			String propertiesUri = this.mappingService.getHref(XWikiConstantsRelations.REL_OBJECT, user.getLinks());
			Objects objects = this.mappingService.getObjects(propertiesUri);
			// then get properties from objects (look for an object from class "XWikiUsers")
			properties =  this.mappingService.getProperties(objects, resourcesPathManager.getUsersClassName());
		}
		return properties;
	}

	/**
	 * Download the full XAR wiki file
	 * @param destFile
	 * @throws TechnicalException
	 * @throws InvalidParameterException
	 */
//	public void exportXwikiContent ( File destFile) {
//
//		// Optional Accept header
//		RequestCallback requestCallback = request -> {
//			try (OutputStreamWriter writer = new OutputStreamWriter(request.getBody(), StandardCharsets.UTF_8)) {
//				writer.write("name=all&description=&licence=&author=XWiki.Admin&version=&history=false&backup=true");
//
//			} catch(IOException ioe) {
//
//			} catch(Exception e) {
//
//			}
//		};
//
//		// Streams the response instead of loading it all in memory
//		ResponseExtractor<Void> responseExtractor = response -> {
//			// Here I write the response to a file but do what you like
//			Path path = Paths.get(destFile.getAbsolutePath());
//			Files.copy(response.getBody(), path);
//			return null;
//		};
//
//		if (destFile.exists()) {
//			destFile.delete();
//		}
//		
//		
//		restTemplate.execute(URI.create( resourcesPathManager.getBaseUrl() + "/xwiki/bin/export/XWiki/XWikiPreferences?editor=globaladmin&section=Export"), HttpMethod.POST, requestCallback, responseExtractor);
//	}

}
