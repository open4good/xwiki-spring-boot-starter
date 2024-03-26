package org.open4goods.ui.controllers.ui;

import org.open4goods.xwiki.services.XWikiReadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

// TODO : document / demo  de auth api : @PreAuthorize("hasAuthority('"+RolesConstants.ROLE_XWIKI_ALL+"')")
// TODO : Demo de fragment xwikithymleaf

@Controller
@RequestMapping("/old")
public class DemoThymleafXwikiContent{

	private static final Logger LOGGER = LoggerFactory.getLogger(DemoThymleafXwikiContent.class);


	
	@Autowired
	@Qualifier( "xwikiReadService" )
	private  XWikiReadService xwikiReadService;

	/**
	 * Endpoint pageSize flush the wiki
	 * @param request
	 * @return
	 */
	@GetMapping("/content")
	public ModelAndView content(final HttpServletRequest request, @RequestParam(name = "r", required = false) String redirectUrl) {
		ModelAndView modelAndView = new ModelAndView("content");
		modelAndView.addObject("xwiki", xwikiReadService);
		return modelAndView;
	}

	
	/**
	 * Endpoint pageSize flush the wiki
	 * @param request
	 * @return
	 */
	@GetMapping("/xwiki")
	public ModelAndView flushCache(final HttpServletRequest request, @RequestParam(name = "r", required = false) String redirectUrl) {
		//xwikiReadService.getBaseUrl();
		return null;
	}
	
}


