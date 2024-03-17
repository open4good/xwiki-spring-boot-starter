package org.open4goods.xwiki.authentication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.open4goods.xwiki.services.XWikiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;



/**
 * A spring authentication provider that relies on Xwiki
 * @author Goulven.Furet
 *
 */

//TODO:  gérer le profile par les props ?? 

//@Profile("!dev")
//@ConditionalOnProperty(name = "xwiki.spring.profile") 
public class XwikiAuthenticationProvider implements AuthenticationProvider {

	XWikiService xwikiService;
	
	public XwikiAuthenticationProvider(XWikiService xwikiService) {
		this.xwikiService = xwikiService;
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String password = authentication.getCredentials().toString();
		String user = authentication.getName();

		List<String> groups  = new ArrayList<String>();
		try {
			groups = xwikiService.login(user, password);
		} catch (Exception e) {
			// error messages have been managed in login method
			// TODO : Specific exception 
			throw new XwikiAuthenticationException(e.getMessage());
		} 
		
		List<GrantedAuthority> grantedAuths = new ArrayList<>();
		if( groups != null ) {
			groups.stream().forEach(e ->  { 
				grantedAuths.add(new SimpleGrantedAuthority( e.replace("xwiki:XWiki.", "").trim().toUpperCase() ));
				grantedAuths.add(new SimpleGrantedAuthority( "ROLE_" + e.replace("xwiki:XWiki.", "").trim().toUpperCase() ));
			});
		}
		
		return new UsernamePasswordAuthenticationToken(authentication.getName(), authentication.getCredentials(), grantedAuths);
	}
}