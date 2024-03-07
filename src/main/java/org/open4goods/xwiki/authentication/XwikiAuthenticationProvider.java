package org.open4goods.xwiki.authentication;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import org.open4goods.xwiki.services.XWikiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


@Profile("!dev")
/**
 * A spring authentication provider that relies on Xwiki
 * @author Goulven.Furet
 *
 */
public class XwikiAuthenticationProvider implements AuthenticationProvider {


	@Autowired XWikiService xwikiService;
	
	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String password = authentication.getCredentials().toString();
		String user = authentication.getName();

		List<String> groups  = new ArrayList<String>();
		
		// TODO : Have to implement back the group logic
		
//		try {
//			groups = xwikiService.loginAndGetGroups(user, password);
//		} catch (TechnicalException e) {
//			throw new InternalAuthenticationServiceException(e.getMessage());
//		} catch (InvalidParameterException e) {
//			throw new BadCredentialsException("Bad user/password");
//		}

		List<GrantedAuthority> grantedAuths = new ArrayList<>();

		groups.stream().forEach(e ->  {
			grantedAuths.add(new SimpleGrantedAuthority(e));
			grantedAuths.add(new SimpleGrantedAuthority("ROLE_" +e));
		});

		return new UsernamePasswordAuthenticationToken(authentication.getName(), authentication.getCredentials(),
				grantedAuths);

	}

}