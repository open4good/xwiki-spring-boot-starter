package org.open4goods.xwiki.authentication;

import org.springframework.security.core.AuthenticationException;

public class XwikiAuthenticationException extends AuthenticationException {

	public XwikiAuthenticationException(String msg) {
		super(msg);
	}

}
