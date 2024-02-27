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
import org.springframework.web.client.RestTemplate;
import org.xwiki.rest.model.jaxb.Attachment;
import org.xwiki.rest.model.jaxb.Page;
import org.xwiki.rest.model.jaxb.PageSummary;
import org.xwiki.rest.model.jaxb.Pages;
import org.xwiki.rest.model.jaxb.Properties;
import org.xwiki.rest.model.jaxb.Property;


/**
 * This service handles XWiki rest services :
 * @author Thierry.Ledan
 */                 
public class XWikiService {

	private static Logger logger = LoggerFactory.getLogger(XWikiService.class);
	
	private XWikiServiceProperties xWikiProperties;
	private XWikiServiceHelper helper;
	private XWikiResourcesPath resourcesPathManager;
	
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
		//String uri = "rest/wikis/xwiki/spaces/" + space + "/pages/" + name + "?media=json";
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
		//String uri = "rest/wikis/xwiki/spaces/" + space + "/pages?media=json";
		String uri = resourcesPathManager.getPagesEndpoint(space);
		return helper.mapPages(uri);
	}
		
	/**
	 * Retrieve all 'Page' associated to a space
	 * properties and attachments are added
	 * @param space
	 * @return A List of 'Page' object, could be empty, never null
	 */
	public List<Page> getPagesList(String space) {
		
		Pages pages = null;
		List<Page> pagesList = new ArrayList<Page>();
		//String uri = "rest/wikis/xwiki/spaces/" + space + "/pages?media=json";
		String endpoint = resourcesPathManager.getPagesEndpoint(space);
		
		pages = helper.mapPages(endpoint);
		
		// Loop on PageSummary list in order to create Page list
		// TODO: add attachments here ?
		// TODO: add objects here ?
		
		if( pages != null && !pages.getPageSummaries().isEmpty() ) {
			
			Page tempPage = null;
			
			for(PageSummary p: pages.getPageSummaries()) {
				
				String pageUrl = null;
//				String attachmentsUrl = null;
//				String properties = null;
				
				// get page endpoint
				//pageUrl =  helper.getHref("http://www.xwiki.org/rel/page", p.getLinks());
				pageUrl =  helper.getHref(XWikiRelations.REL_PAGE, p.getLinks());
				tempPage = helper.mapPage(pageUrl);
				if( tempPage != null ) {
					pagesList.add(tempPage);
				}
			}
		}
		return pagesList;
	}
	
	
	/**
	 * Get attachments associated to the Page 'page'
	 * @return
	 */
	public List<Attachment> getAttachmentList(Page page){
		return helper.getAttachments(page);
	}
	
	/**
	 * Return the url for this attachment
	 * Scheme is updated if https is mandatory
	 * 
	 * @param attachment
	 * @return
	 */
	public String getAttachmentUrl(Attachment attachment) {
		return helper.getAttachmentUrl(attachment);
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
	
	/**
	 * 
	 * @param space
	 * @return
	 */
//	public Map<String,String> getProperties(String pageName) {
//		Map<String,String> props = new HashMap<String, String>();
//		String endpoint = resourcesPathManager.getPageEndpoint(spaceName, pageName);
//		Page page = helper.mapPage(endpoint);
//		if(page != null) {
//			props = helper.getProperties(page);
//		}
//		return props;
//	}
	
}
