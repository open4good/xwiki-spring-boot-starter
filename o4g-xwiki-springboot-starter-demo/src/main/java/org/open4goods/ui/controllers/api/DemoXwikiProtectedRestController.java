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
	
	// http://localhost:8080/page?space=Blog&page=BlogIntroduction
//	@Operation( summary = "Operation to fetch a xwiki Page", 
//			description = "Fetch a xwiki Page belonging to a space" 
//					+ "<br>TODO: Gestion des codes de retour http")
//	@GetMapping( value = "/page", produces = "application/json" )
//	public Page getPage( @RequestParam String space, @RequestParam String page ) {
//		Page wikiPage = xwikiReadService.getPage(space, page);
//		return wikiPage;
//	}
	
	// http://localhost:8080/page/spaces/BROUILLONS.testbrouillon/pageName/WebHome
	@Operation( summary = "Operation to fetch a xwiki Page", 
			description = "Fetch a xwiki Page belonging to a Space"
					+ "<br>Separate main space and nested spaces with '.'")
					
	@GetMapping( "page/spaces/{spaces:.+}/pageName/{pageName}" )
	public Page getPage( @PathVariable("spaces") String spaces, @PathVariable("pageName")  String pageName ) {
		Page wikiPage = xwikiReadService.getPage(spaces, pageName);
		return wikiPage;
	}

	// http://localhost:8080/pages?space=Blog
	@Operation( summary = "Operation to fetch a set of PageSummary", 
			description = "Fetch all Page Summary belonging to a space"
					+ "<br>Separate main space and nested spaces with '.'")
	
	@GetMapping( "/pages/spaces/{spaces:.+}" )
	public Pages getPages( @PathVariable("spaces") String spaces ) {
		Pages page = xwikiReadService.getPages(spaces);
		return page;
	}
	
	
	// http://localhost:8080/props?space=Blog&page=BlogIntroduction
	@Operation( summary = "Operation to fetch Page's properties",
			description = "Fetch a map of properties (as String) belonging to a page in a space"
					+ "<br>TODO: Gestion des codes de retour http")
	@GetMapping(  value = "/props", produces = "application/json" )
	public Map<String, String> getProperties ( @RequestParam String space, @RequestParam String page ) {
		Map<String, String> props = xwikiReadService.getProperties(space, page);
		return props;
	}
	
	// http://localhost:8080/pagelist?space=Blog
	@Operation( 
			summary = "Operation to fetch a list of Page", 
			description = "Fetch all Pages belonging to a space."
					+ "<br>Heavy process because it needs to scan all PageSummary belonging to a space and then creates a Page object from a PageSummary."
					+ "<br>Furthermore, for each Page this process requests the server in order to fetch attachments and objects belonging to this Page."
					+ "<br>Up to 3 requests for each generated Page."
					+ "<br>Note: Page does not have any properties."
					+ "<br>TODO: fetch classes to Page" 
					+ "<br>TODO: Gestion des codes de retour http")
	@GetMapping(  value = "/pagelist", produces = "application/json" )
	public List<Page> getPageList( @RequestParam String space ){
		List<Page> pages = xwikiReadService.getPagesList(space);
		return pages;
	}
}
