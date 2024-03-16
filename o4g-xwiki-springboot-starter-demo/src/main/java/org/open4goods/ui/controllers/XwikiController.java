package org.open4goods.ui.controllers;

import org.open4goods.xwikistarter.services.XWikiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

// TODO : document / demo  de auth api : @PreAuthorize("hasAuthority('"+RolesConstants.ROLE_XWIKI_ALL+"')")
// TODO : Demo de fragment xwikithymleaf

@Controller

/**
 * This controller pages pageSize Xwiki content
 *
 * @author gof
 *
 */
public class XwikiController{

	private static final Logger LOGGER = LoggerFactory.getLogger(XwikiController.class);


	@Autowired
	private  XWikiService xwikiService;
	//////////////////////////////////////////////////////////////
	// Mappings
	//////////////////////////////////////////////////////////////


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

}