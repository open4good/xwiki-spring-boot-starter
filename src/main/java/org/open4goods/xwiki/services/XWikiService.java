package org.open4goods.xwiki.services;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.open4goods.xwiki.config.XWikiRelations;
import org.open4goods.xwiki.config.XWikiResourcesPath;
import org.open4goods.xwiki.config.XWikiServiceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
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
 * This service handles XWiki rest services :
 * @author Thierry.Ledan
 */                 
public class XWikiService {

	private static Logger logger = LoggerFactory.getLogger(XWikiService.class);
	
	private XWikiServiceProperties xWikiProperties;
	private XWikiServiceHelper helper;
	private XWikiResourcesPath resourcesPathManager;
	
	@Autowired
	private RestTemplateBuilder localRestTemplateBuilder;
	
	public XWikiService (RestTemplate restTemplate, XWikiServiceProperties xWikiProperties) throws Exception {
		this.xWikiProperties = xWikiProperties;
		helper = new XWikiServiceHelper(restTemplate, xWikiProperties);
		this.resourcesPathManager = new XWikiResourcesPath(xWikiProperties.getBaseUrl(), xWikiProperties.getApiEntrypoint(), xWikiProperties.apiWiki);
		
		// get all available wikis and check that the targeted one exists
		if( xWikiProperties.getApiWiki() == null || ! helper.checkWikiExists(xWikiProperties.getApiWiki()) ) {
			throw new Exception("The targeted wiki " + xWikiProperties.getApiWiki() + " does not exist");
		}
	}
	
	
	/**
	 * 
	 * @param space
	 * @param name
	 * @return
	 */
	public Page getPage(String space, String name) {
		Page page = null;
		String endpoint = resourcesPathManager.getPageEndpoint(space, name);	
		page = helper.mapPage(endpoint);
		return page;
	}

	/**
	 * 
	 * @param space
	 * @return
	 */
	public Pages getPages(String space) {
		String uri = resourcesPathManager.getPagesEndpoint(space);
		return helper.mapPages(uri);
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
		
		pages = helper.mapPages(endpoint);
		
		// Loop on PageSummary list in order to create Page list
		if( pages != null && !pages.getPageSummaries().isEmpty() ) {
			
			Page tempPage = null;
			
			for(PageSummary p: pages.getPageSummaries()) {
				
				String pageUrl = null;
				
				// get page endpoint
				pageUrl =  helper.getHref(XWikiRelations.REL_PAGE, p.getLinks());
				
				// add request param to return fields that are disabled by default
				// TODO: mapping issue: field 'properrties' does not exists in the object 'ObjectSummary'
//				pageUrl = helper.addQueryParam(pageUrl, "class", "true");
//				pageUrl = helper.addQueryParam(pageUrl, "objects", "true");
//				pageUrl = helper.addQueryParam(pageUrl, "attachments", "true");
				tempPage = helper.mapPage(pageUrl);
				if( tempPage != null ) {
					pagesList.add(tempPage);

					// fetch attachments
					Attachments attachments = helper.getAttachments(tempPage);
					if( attachments != null && attachments.getAttachments() != null && attachments.getAttachments().size()  > 0 ) {
						// update url (scheme, query params..) according to starter properties
						for(Attachment attachment: attachments.getAttachments()) {
							attachment.setXwikiAbsoluteUrl(helper.updateUrlScheme(attachment.getXwikiAbsoluteUrl()));
							attachment.setXwikiRelativeUrl(helper.updateUrlScheme(attachment.getXwikiRelativeUrl()));
						}
						tempPage.setAttachments(attachments);
					}	
					
					// fetch objects (properties, ...)
					Objects objects = helper.getPageObjects(tempPage);
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
		Page page = helper.mapPage(endpoint);
		if(page != null) {
			props = helper.getProperties(page);
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
			props = helper.getProperties(page);
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
	 * 
	 * @return
	 */
	public List<String> getGroupsName(){
		List<String> groups = new ArrayList<String>();
		SearchResults results = helper.mapSearchResults(resourcesPathManager.getGroupsEndpoint());
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
	 * 
	 * @param groupPageName
	 * @return
	 */
	public List<String> getGroupUsers(String groupPageName) {
		// https://wiki.nudger.fr/rest/wikis/xwiki/spaces/XWiki/pages/SiteEditor/objects?media=json
		List<String> users = new ArrayList<String>();
		Objects objects = helper.getObjects(resourcesPathManager.getGroupUsers(groupPageName));
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
		page = helper.mapPage(endpoint);
		return page;
	}
	
	/**
	 * login with username/password
	 * 
	 * @param builder inject the default RestTemplateBuilder bean provided by Spring Boot to create a local RestTemplate
	 * @param userName
	 * @param password
	 * @return user Page if login succeeds
	 * @throws HttpClientErrorException if response http status code equals to 4xx
	 */
	public Page login( String userName, String password) throws HttpClientErrorException {
		Page page = null;
		String endpoint = resourcesPathManager.getPageEndpoint(XWikiResourcesPath.ADMIN_SPACE, userName);	
		page = helper.login(localRestTemplateBuilder, endpoint, userName, password);
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
			String propertiesUri = helper.getHref(XWikiRelations.REL_OBJECT, user.getLinks());
			Objects objects = helper.getObjects(propertiesUri);
			// then get properties from objects (look for an object from class "XWikiUsers")
			properties =  helper.getProperties(objects, XWikiResourcesPath.USERS_CLASNAME);
		}
		return properties;
	}
}
