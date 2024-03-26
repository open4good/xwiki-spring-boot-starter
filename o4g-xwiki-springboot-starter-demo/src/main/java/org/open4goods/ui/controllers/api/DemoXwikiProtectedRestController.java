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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xwiki.rest.model.jaxb.Page;
import org.xwiki.rest.model.jaxb.Pages;

import io.swagger.v3.oas.annotations.Operation;

/**
 * This controller allows informations and communications from fetchers
 * @author goulven
 *
 */
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
	@GetMapping("/page")
	public Page getPage( @RequestParam String space, @RequestParam String page ) {
		Page wikiPage = xwikiReadService.getPage(space, page);
		return wikiPage;
	}
	
	
	// http://localhost:8080/pages?space=Blog
	@GetMapping("/pages")
	public Pages getPages( @RequestParam String space ) {
		Pages page = xwikiReadService.getPages(space);
		return page;
	}
	
	// http://localhost:8080/props?space=Blog&page=BlogIntroduction
	@GetMapping("/props")
	public Map<String, String> getProperties ( @RequestParam String space, @RequestParam String page ) {
		Map<String, String> props = xwikiReadService.getProperties(space, page);
		return props;
	}
	
	// http://localhost:8080/pagelist?space=Blog
	@GetMapping("/pagelist")
	public List<Page> getPageList( @RequestParam String space ){
		List<Page> pages = xwikiReadService.getPagesList(space);
		return pages;
	}
}
