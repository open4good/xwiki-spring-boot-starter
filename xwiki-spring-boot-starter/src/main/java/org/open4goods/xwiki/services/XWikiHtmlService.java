package org.open4goods.xwiki.services;

import java.io.StringReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;


import org.open4goods.xwiki.config.XWikiConstantsResourcesPath;
import org.open4goods.xwiki.config.XWikiServiceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionException;
import org.springframework.http.ResponseEntity;
import org.xwiki.component.embed.EmbeddableComponentManager;
import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.rendering.converter.Converter;
import org.xwiki.rendering.renderer.printer.DefaultWikiPrinter;
import org.xwiki.rendering.renderer.printer.WikiPrinter;
import org.xwiki.rendering.syntax.Syntax;

public class XWikiHtmlService {

	private XWikiServiceProperties xWikiProperties;
	private XWikiConstantsResourcesPath resourcesPathManager;
	private XwikiMappingService mappingService;
	private RestTemplateService restTemplateService;
	
	private static Logger LOGGER = LoggerFactory.getLogger(XWikiHtmlService.class);

	public XWikiHtmlService( XwikiMappingService mappingService, RestTemplateService restTemplateService, XWikiServiceProperties xWikiProperties) {
		this.xWikiProperties = xWikiProperties;
		this.mappingService = mappingService;
		this.restTemplateService = restTemplateService;
		
		this.resourcesPathManager = new XWikiConstantsResourcesPath(xWikiProperties.getBaseUrl(), xWikiProperties.getApiEntrypoint(), xWikiProperties.getApiWiki());

	}
	
	
	/**
	 * Returns xwiki web server response from wikiPage
	 * with an absolute or relative path
	 * @param xwikiPath path to web page
	 * @param withAbsolutePath true if 'xwikiPath' is absolute
	 * @return
	 */
//	public ResponseEntity<String> getWebPage( String xwikiPath, boolean withAbsolutePath ) {
//		String xwikiWebUrl = URLDecoder.decode(xwikiPath, Charset.defaultCharset());
//		if( ! withAbsolutePath ) {
//			xwikiWebUrl = resourcesPathManager.getViewpath() + xwikiWebUrl;
//		} 
//		return getWebResponse( xwikiWebUrl );
//	}
	
	
	
	public String html( String xwikiPath) {
		
		return getWebPage(xwikiPath, false);
	}
		
	/**
	 * Returns xwiki web server response from wikiPage
	 * with an absolute or relative path
	 * @param xwikiPath path to web page
	 * @param withAbsolutePath true if 'xwikiPath' is absolute
	 * @return html response
	 */
	// TODO: manage response error / exceptions
	public String getWebPage( String xwikiPath, boolean withAbsolutePath ) {
		
		String MARKER = "<div id=\"xwikicontent\" class=\"col-xs-12\">";
		
		String htmlResult = null;
		
		// web Page url
		String xwikiWebUrl = URLDecoder.decode(xwikiPath, Charset.defaultCharset());
		if( ! withAbsolutePath ) {
			xwikiWebUrl = resourcesPathManager.getViewpath() + xwikiWebUrl;
		} 
		// request server
		ResponseEntity<String> response = this.restTemplateService.getWebResponse( xwikiWebUrl );
		if( response == null ) {
			// manage error/exception
		} else {
			// code status 2xx
			try {
				String raw= response.getBody();
				int pos=raw.indexOf(MARKER);
				raw = raw.substring(pos+MARKER.length()).trim();
				String body= raw.substring(0,raw.indexOf("\n"));

				// Removing simple <p> tag if occurs
				if (body.startsWith("<p>")) {
					body=body.substring(3,body.length()-4);
				}
				htmlResult = body;
			}
			catch (Exception e) {
				LOGGER.error("Cannot render to html page at " + xwikiWebUrl,e);
			}
		}
		return htmlResult;
	}
	
	
	 public String renderXWiki20SyntaxAsXHTML(String contentXwiki21)
	    {
	        try {
				// Initialize Rendering components and allow getting instances
				EmbeddableComponentManager cm = new EmbeddableComponentManager();
				cm.initialize(this.getClass().getClassLoader());

				// Use the Converter component to convert between one syntax to another.
				Converter converter = cm.getInstance(Converter.class);

				// Convert input in XWiki Syntax 2.1 into XHTML. The result is stored in the printer.
				WikiPrinter printer = new DefaultWikiPrinter();
				converter.convert(new StringReader(contentXwiki21), Syntax.XWIKI_2_1, Syntax.XHTML_1_0, printer);

				return printer.toString();
			} catch (Exception e) {
				LOGGER.error("Error while rendering XWiki content to XHTML.",e);
				return "Error while rendering XWiki content to XHTML.";
			}
	       
	    }
	
	
	// TODO : Below should be in XWikiConstantsResourcesPath ?
	
	
	
	
	/**
	 * Get the absolute url to the wiki page in view mode
	 * @param xwikiPath
	 * @return
	 */
	public String getViewPageUrl( String xwikiPath ) {	
		return resourcesPathManager.getViewpath() + URLDecoder.decode(xwikiPath, Charset.defaultCharset());
	}
	
	
	/**
	 * Get the absolute url to web page in edit mode
	 * @param xwikiPath
	 * @return
	 */
	public String getEditPageUrl( String xwikiPath ) {	
		return resourcesPathManager.getEditpath() + URLDecoder.decode(xwikiPath, Charset.defaultCharset());
	}
	
	
	
	/**
	 * Get the URL of a Page attachment (image...) given its name and space
	 * @param space
	 * @param name
	 * @param attachmentName
	 * @return
	 */
	public String getAttachmentUrl(String space, String name, String attachmentName) {
		return resourcesPathManager.getDownloadAttachlmentUrl(space, name, attachmentName);
	}
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public byte[] getAttachment( String url ) {
		return this.mappingService.downloadAttachment(url);
	}
	
	
}
