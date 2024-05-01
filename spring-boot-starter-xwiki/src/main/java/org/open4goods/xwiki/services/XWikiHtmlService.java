package org.open4goods.xwiki.services;

import java.net.URLDecoder;
import java.nio.charset.Charset;

import org.open4goods.xwiki.config.XWikiConstantsResourcesPath;
import org.open4goods.xwiki.config.XWikiServiceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

public class XWikiHtmlService {

	private XWikiServiceProperties xWikiProperties;
	private XWikiConstantsResourcesPath resourcesPathManager;
	private MappingService mappingService;
	private RestTemplateService restTemplateService;
	
	private static Logger LOGGER = LoggerFactory.getLogger(XWikiHtmlService.class);

	public XWikiHtmlService( MappingService mappingService, RestTemplateService restTemplateService, XWikiServiceProperties xWikiProperties) {
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
	
	
	
	
	
	
	
	
	
	
	/**
	 * Get the absolute url to web page
	 * @param xwikiPath
	 * @return
	 */
	public String getWebPageUrl( String xwikiPath ) {	
		return resourcesPathManager.getViewpath() + URLDecoder.decode(xwikiPath, Charset.defaultCharset());
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
