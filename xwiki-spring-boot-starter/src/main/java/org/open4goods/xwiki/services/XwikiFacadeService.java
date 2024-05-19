package org.open4goods.xwiki.services;

import java.util.Map;

import org.open4goods.xwiki.model.FullPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xwiki.rest.model.jaxb.Objects;
import org.xwiki.rest.model.jaxb.Page;

public class XwikiFacadeService {


	private final XwikiMappingService mappingService;

	private final XWikiReadService xWikiReadService;
	private final XWikiHtmlService xWikiHtmlService;
	private final XWikiObjectService xWikiObjectService;
	
	private static Logger LOGGER = LoggerFactory.getLogger(XwikiFacadeService.class);

	public XwikiFacadeService( XwikiMappingService mappingService, XWikiObjectService xWikiObjectService, XWikiHtmlService xWikiHtmlService,XWikiReadService xWikiReadService, XWikiObjectService xWikiObjectService2, XWikiHtmlService xWikiHtmlService2) {
		this.mappingService = mappingService;
		
		this.xWikiReadService = xWikiReadService;
		this.xWikiHtmlService = xWikiHtmlService2;
		this.xWikiObjectService = xWikiObjectService2;

	}
	
	// TODO : I18n
	public FullPage getFullPage (String... path) {
		
		
		Page wikiPage  = xWikiReadService.getPage(path);
		//String htmlContent = xWikiHtmlService.html(space+":"+page);
		
		wikiPage.getObjects().getObjectSummaries();
		Map<String, String> properties = xWikiObjectService.getProperties(wikiPage);
		
		Objects objects = mappingService.getPageObjects(wikiPage);
		
		objects.getObjectSummaries().forEach(e -> {
			Map<String, String> props = mappingService.getUserProperties(objects, e.getClassName());
			System.out.println(props);
		});
		
		
		return null;
		
		
		
		
		
		
		
	}

	
	
	
}
