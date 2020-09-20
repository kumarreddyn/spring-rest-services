package rest.services.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class RestAPIAuthenticationProvider implements AuthenticationProvider{
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication;				
        if(null != usernamePasswordAuthenticationToken && !"".equals(usernamePasswordAuthenticationToken.getPrincipal())){
            return new UsernamePasswordAuthenticationToken(usernamePasswordAuthenticationToken.getPrincipal(), usernamePasswordAuthenticationToken.getCredentials(), usernamePasswordAuthenticationToken.getAuthorities());
        }else {
        	throw new BadCredentialsException("Invalid credentials");
        }
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
