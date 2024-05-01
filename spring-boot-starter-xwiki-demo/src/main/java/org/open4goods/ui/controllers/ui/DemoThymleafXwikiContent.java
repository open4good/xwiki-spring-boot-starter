package org.open4goods.ui.controllers.ui;

import org.open4goods.xwiki.services.XWikiHtmlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// TODO : document / demo  de auth api : @PreAuthorize("hasAuthority('"+RolesConstants.ROLE_XWIKI_ALL+"')")
// TODO : Demo de fragment xwikithymleaf

@Controller
@RequestMapping("")
public class DemoThymleafXwikiContent{

	private static final Logger LOGGER = LoggerFactory.getLogger(DemoThymleafXwikiContent.class);


	
	@Autowired
	@Qualifier( "xwikiHtmlService" )
	private  XWikiHtmlService xwikiHtmlService;

	/**
	 * 
	 * 
	 * @param request
	 * @return
	 */
	//@GetMapping("/content")
	// http://localhost:8080/content/Blog/Ouverture+du+prototype+!
	@GetMapping("/content/{space}/{page}")
	public ModelAndView content(
			@PathVariable(name = "space") String space, 
			@PathVariable(name = "page") String page,
            final HttpServletRequest request,
            HttpServletResponse response) {
		
		ModelAndView modelAndView = new ModelAndView("content");
		String content = xwikiHtmlService.getWebPage(space + "/" + page, false);
		modelAndView.addObject("xwiki", content);
		return modelAndView;
	}

	
	/**
	 * 
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/xwiki")
	public ModelAndView flushCache(final HttpServletRequest request, @RequestParam(name = "r", required = false) String redirectUrl) {
		//xwikiReadService.getBaseUrl();
		return null;
	}
	
}


