package org.open4goods.xwiki.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Properties from configuration file
 * 
 * @author Thierry.Ledan
 */

@ConfigurationProperties()
public class XWikiServiceProperties{
	
	@Value( "${xwiki.baseUrl}" )
	public String baseUrl;
	
	@Value( "${xwiki.auth.username}" )
	public String username;
	
	@Value( "${xwiki.auth.password}" )
	public String password;
	
	// needed for test
	@Value( "#{new Boolean('${xwiki.httpsOnly}')} " )
	public boolean httpsOnly;
	
	@Value( "${xwiki.media}" )
	public String media;	
	
	@Value( "${xwiki.api.entryPoint}" )
	public String apiEntrypoint;	
	
	@Value( "${xwiki.api.wiki}" )
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

