package org.open4goods.xwiki;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties()
public class XWikiServiceProperties{
	
	@Value( "${xwiki-o4g.baseUrl}" )
	public String baseUrl;
	@Value( "${xwiki-o4g.auth.username}" )
	public String username;
	@Value( "${xwiki-o4g.auth.password}" )
	public String password;
	
	
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

