package org.open4goods.xwiki.config;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriComponentsBuilder;
import org.xwiki.rest.model.jaxb.Link;

public class UrlManagementHelper {

	private XWikiServiceProperties properties;
	
	private static Logger logger = LoggerFactory.getLogger(UrlManagementHelper.class);
	
	public UrlManagementHelper(XWikiServiceProperties properties) {
		this.properties = properties;
	}
	/**
	 * Update http scheme in https if needed
	 * @param url
	 * @return
	 */
	public String updateUrlScheme(String url) {

		String updated = url;
		if(url != null && properties.isHttpsOnly()) {
			updated = url.replaceFirst("http:", "https:");
		}
		return updated;
	}
	
	/**
	 * Clean url: 
	 * 				URLDecoding  
	 * 				Force https if httpsOnly set
	 * 				Add 'media' query param from properties
	 * @param url
	 * @param key
	 * @param value
	 * @return this url with query param key=value if process succedded, null otherwise
	 */
	public String cleanUrl(String url) {

		String clean = "";
		try {
			if( url != null ) {
				String secureUrl = updateUrlScheme(url);
				String decoded = URLDecoder.decode(secureUrl, Charset.defaultCharset());
				// add request param if needed
				clean = addQueryParam(decoded, "media", this.properties.getMedia());
			}
		} catch (Exception e) {
			logger.warn("Exception while updating url {}. Error Message: {}",url,  e.getMessage());
		}
		return clean;
	}
	
	/**
	 * Add query params to url
	 * @param url
	 * @param key
	 * @param value
	 * @return this url with query param key=value if process succedded, null otherwise
	 */
	public String addQueryParam(String url, String key, String value) {

		String uriWithParams = null;
		try{
			URI uri = UriComponentsBuilder.fromUriString(url)
					.queryParam(key, value)
					.build()
					.toUri();

			if(uri != null) {
				// decode again after adding query params
				uriWithParams = URLDecoder.decode(uri.toString(), Charset.defaultCharset());
			}

		} catch(Exception e) {
			logger.warn("Unable to add query params {}={} to uri {}. Error Message:{}",key, value, url, e.getMessage());
		}       
		return uriWithParams;

	}
	
	/**
	 * Get href link from rel link in 'links'  
	 * @param rel
	 * @return 'href' link from 'rel' link
	 */
	public String getHref(String rel, List<Link> links) {

		String href = null;
		try {

			for(Link link: links) {
				if(link.getRel().equals(rel)) {
					href = link.getHref();
				} 
			}
		} catch(Exception e) {
			logger.warn("Exception while retrieving 'href' from link {}. Error Message: {}",rel,  e.getMessage());
		}
		return href;
	}
	
}
