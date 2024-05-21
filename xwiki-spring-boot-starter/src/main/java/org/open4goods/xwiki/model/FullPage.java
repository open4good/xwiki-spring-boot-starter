package org.open4goods.xwiki.model;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.xwiki.rest.model.jaxb.Objects;
import org.xwiki.rest.model.jaxb.Page;

public class FullPage {

	private String htmlContent;
	private Page wikiPage;
	private Objects objects;
	private Map<String, String> properties;
	public String getHtmlContent() {
		return htmlContent;
	}
	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}
	public Page getWikiPage() {
		return wikiPage;
	}
	public void setWikiPage(Page wikiPage) {
		this.wikiPage = wikiPage;
	}
	public Objects getObjects() {
		return objects;
	}
	public void setObjects(Objects objects) {
		this.objects = objects;
	}
	public Map<String, String> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
	
	
	
}
