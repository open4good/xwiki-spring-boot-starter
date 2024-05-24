package org.open4goods.ui.controllers.api;

import java.util.List;
import java.util.Map;

import org.open4goods.ui.controllers.ui.DemoThymleafXwikiContent;
import org.open4goods.xwiki.services.XWikiReadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xwiki.rest.model.jaxb.Page;
import org.xwiki.rest.model.jaxb.Pages;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * This controller allows informations and communications from fetchers
 * @author goulven
 *
 */

@Tag( name = "XWIKI SPRING BOOT STARTER API", description = "Rest services available for Spring Boot application")
@RestController
public class DemoXwikiProtectedRestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DemoXwikiProtectedRestController.class);

	@Autowired
	@Qualifier( "xwikiReadService" )
	XWikiReadService xwikiReadService;
	
//	
//	@PatchMapping(path = "/feeds")
//	@Operation(summary="Manualy run the indexation of all feeds")
//	@PreAuthorize("hasAuthority('"+RolesConstants.ROLE_ADMIN+"')")
//	public IndexationResponse indexFeeds() {
//		feedService.fetchFeeds();
//		return new IndexationResponse();
//	}
	
	
	@GetMapping(path = "/allgroup")
	@PreAuthorize("hasAuthority('XWIKIALLGROUP')")
	public void testAuthorization() {
		LOGGER.info("AUTHORIZED");
	}

	
	// http://localhost:8080/page/spaces/BROUILLONS.testbrouillon/pageName/WebHome
	/**
	 * Operation to get a xwiki Page
	 * 
	 * @param spaces space path to page
	 * @param pageName page's name
	 * @return a Page
	 */
	@Operation( summary = "Operation to get a xwiki Page", 
			description = "Fetch a xwiki Page belonging to a Space"
					+ "<br>Separate main space and nested spaces with '.' to get the space path")
					
	@GetMapping( "page/spaces/{spaces:.+}/pageName/{pageName}" )
	public Page getPage( @PathVariable("spaces") String spaces, @PathVariable("pageName")  String pageName ) {
		Page wikiPage = xwikiReadService.getPage(spaces+":"+ pageName);
		return wikiPage;
	}

	// http://localhost:8080/pages/spaces/BROUILLONS.testbrouillon
	/**
	 * Operation to fetch a set of PageSummary
	 * 
	 * @param spaces targeted space path 
	 * @return a Pages object
	 */
	@Operation( summary = "Operation to get a set of PageSummary", 
			description = "Fetch all Page Summary belonging to a space"
					+ "<br>Separate main space and nested spaces with '.' to get the space path")
	
	@GetMapping( "/pages/spaces/{spaces:.+}" )
	public Pages getPages( @PathVariable("spaces") String spaces ) {
		Pages page = xwikiReadService.getPages(spaces);
		return page;
	}
	
	// http://localhost:8080/page/props/spaces/BROUILLONS.testbrouillon/pageName/WebHome
	/**
	 * Operation to fetch Page's properties
	 * 
	 * @param spaces space path to Page
	 * @param pageName page's name
	 * @return a map of key/value pairs of properties
	 */
	@Operation( summary = "Operation to fetch Page's properties",
			description = "Fetch a map of properties (as String) belonging to a page."
					+ "<br>Separate main space and nested spaces with '.' to get the space path")
	
	@GetMapping( "/page/props/spaces/{spaces:.+}/pageName/{pageName}" )
	public Map<String, String> getProperties ( @PathVariable("spaces") String spaces, @PathVariable("pageName") String pageName ) {
		Map<String, String> props = xwikiReadService.getProperties(spaces, pageName);
		return props;
	}
	
	
	// http://localhost:8080/pagelist?space=Blog
	/**
	 * Operation to fetch a list of Page
	 * 
	 * @param spaces targeted space path
	 * @return a list of page
	 */
	@Operation( 
			summary = "Operation to fetch a list of Page", 
			description = "Fetch all Pages belonging to a space."
					+ "<br>Heavy process because it needs to scan all PageSummary belonging to a space and then creates a Page object from each PageSummary."
					+ "<br>Furthermore, for each Page this process requests the server in order to fetch attachments and objects belonging to this Page."
					+ "<br>Up to 3 requests for each generated Page."
					+ "<br>Note: A XWiki Page does not provide a field for properties !!!!"
					+ "<br>TODO: fetch classes to Page")
	
	@GetMapping( "/pageList/spaces/{spaces:.+}" )
	public List<Page> getPageList( @PathVariable("spaces") String spaces ){
		List<Page> pages = xwikiReadService.getPagesList(spaces);
		return pages;
	}
}
