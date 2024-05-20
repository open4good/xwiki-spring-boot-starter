package org.open4goods.xwiki.services;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.open4goods.xwiki.model.FullPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xwiki.rest.model.jaxb.Objects;
import org.xwiki.rest.model.jaxb.Page;

/**
 * An Xwiki facade service, which encapsulates xwiki unitary services to deliver
 * high level  wiki content to spring boot web translation
 */
public class XwikiFacadeService {

	private static Logger LOGGER = LoggerFactory.getLogger(XwikiFacadeService.class);

	// The unitary services
	private final XwikiMappingService mappingService;
	private final XWikiReadService xWikiReadService;
	private final XWikiHtmlService xWikiHtmlService;
	private final XWikiObjectService xWikiObjectService;
	

	public XwikiFacadeService( XwikiMappingService mappingService, XWikiObjectService xWikiObjectService, XWikiHtmlService xWikiHtmlService,XWikiReadService xWikiReadService, XWikiObjectService xWikiObjectService2, XWikiHtmlService xWikiHtmlService2) {
		this.mappingService = mappingService;		
		this.xWikiReadService = xWikiReadService;
		this.xWikiHtmlService = xWikiHtmlService2;
		this.xWikiObjectService = xWikiObjectService2;
	}
	
	// TODO : I18n
	public FullPage getFullPage (String... path) {
		FullPage ret = new FullPage();
		
		String htmlContent = xWikiHtmlService.html(StringUtils.join(path,"/"));

		Page wikiPage  = xWikiReadService.getPage(path);
		Objects objects = mappingService.getPageObjects(wikiPage);
		
		Map<String, String> properties = xWikiObjectService.getProperties(wikiPage);
	
		objects.getObjectSummaries().forEach(e -> {
			Map<String, String> props = mappingService.getUserProperties(objects, e.getClassName());
			System.out.println(props);
		});
		
		
		return ret;
		
	}
}
