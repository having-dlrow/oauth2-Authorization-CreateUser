package com.example.authorizationserver.securityHandler;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.io.Serial;
import java.util.List;

public class MFAAuthentication extends AnonymousAuthenticationToken {

    private static final long serialVersionUID = 1L;
    private final Authentication authentication;

    public MFAAuthentication(Authentication authentication, String authority) {
        super("anonymous", "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS", authority));
        this.authentication = authentication;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    @Override
	public Object getPrincipal() {
		return this.authentication.getPrincipal();
	}
}
