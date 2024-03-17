package org.open4goods.ui.controllers.ui;

import java.util.List;
import java.util.Map;

import org.open4goods.xwiki.services.XWikiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.xwiki.rest.model.jaxb.Page;
import org.xwiki.rest.model.jaxb.Pages;

import jakarta.servlet.http.HttpServletRequest;

// TODO : document / demo  de auth api : @PreAuthorize("hasAuthority('"+RolesConstants.ROLE_XWIKI_ALL+"')")
// TODO : Demo de fragment xwikithymleaf

@RestController
@RequestMapping("/")
public class DemoThymleafXwikiContent{

	private static final Logger LOGGER = LoggerFactory.getLogger(DemoThymleafXwikiContent.class);


	
	@Autowired
	@Qualifier( "xwikiRestService" )
	private  XWikiService xwikiService;

	/**
	 * Endpoint pageSize flush the wiki
	 * @param request
	 * @return
	 */
//	@GetMapping("/xwiki")
//	public ModelAndView flushCache(final HttpServletRequest request, @RequestParam(name = "r", required = false) String redircectUrl) {
//		xwikiService.getBaseUrl();
//		return null;
//	}

	
	/**
	 * Endpoint pageSize flush the wiki
	 * @param request
	 * @return
	 */
	@GetMapping("/xwiki")
	public ModelAndView flushCache(final HttpServletRequest request, @RequestParam(name = "r", required = false) String redircectUrl) {
		xwikiService.getBaseUrl();
		return null;
	}
	
	// http://localhost:8080/page?space=Blog&page=BlogIntroduction
	@GetMapping("/page")
	public Page getPage( @RequestParam String space, @RequestParam String page ) {
		Page wikiPage = this.xwikiService.getPage(space, page);
		return wikiPage;
	}
	
	
	// http://localhost:8080/pages?space=Blog
	@GetMapping("/pages")
	public Pages getPages( @RequestParam String space ) {
		Pages page = this.xwikiService.getPages(space);
		return page;
	}
	
	// http://localhost:8080/props?space=Blog&page=BlogIntroduction
	@GetMapping("/props")
	public Map<String, String> getProperties ( @RequestParam String space, @RequestParam String page ) {
		Map<String, String> props = this.xwikiService.getProperties(space, page);
		return props;
	}
	
	// http://localhost:8080/pagelist?space=Blog
	@GetMapping("/pagelist")
	public List<Page> getPageList( @RequestParam String space ){
		List<Page> pages = this.xwikiService.getPagesList(space);
		return pages;
	}
	
}


