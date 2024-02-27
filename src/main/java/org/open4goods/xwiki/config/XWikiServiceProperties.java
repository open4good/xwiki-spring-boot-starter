package org.open4goods.xwiki.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties from configuration file
 * 
 * @author Thierry.Ledan
 */
@ConfigurationProperties()
public class XWikiServiceProperties{
	
	@Value( "${xwiki-o4g.baseUrl}" )
	public String baseUrl;
	@Value( "${xwiki-o4g.auth.username}" )
	public String username;
	@Value( "${xwiki-o4g.auth.password}" )
	public String password;
	@Value( "${xwiki-o4g.httpsOnly}" )
	public boolean httpsOnly;
	@Value( "${xwiki-o4g.media}" )
	public String media;	
	@Value( "${xwiki-o4g.api.entryPoint}" )
	public String apiEntrypoint;	
	@Value( "${xwiki-o4g.api.wiki}" )
	public String apiWiki;

	public String getApiWiki() {
		return apiWiki;
	}

	public void setApiWiki(String apiWiki) {
		this.apiWiki = apiWiki;
	}

	public String getApiEntrypoint() {
		return apiEntrypoint;
	}

	public void setApiEntrypoint(String apiEntrypoint) {
		this.apiEntrypoint = apiEntrypoint;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}

	public boolean isHttpsOnly() {
		return httpsOnly;
	}

	public void setHttpsOnly(boolean httpsOnly) {
		this.httpsOnly = httpsOnly;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	
}

