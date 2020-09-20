package rest.services.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import rest.services.constants.SecurityConstants;

@Component
public class RestAPIAuthenticationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authToken = request.getHeader("AUTH_TOKEN");
		Claims claims = decodeToken(authToken);
		if (null != claims) {
			Authentication auth = new UsernamePasswordAuthenticationToken(
					AuthorityUtils.createAuthorityList("ROLE_USER"), claims.getSubject());
			SecurityContextHolder.getContext().setAuthentication(auth);
			
			filterChain.doFilter(request, response);
		} else {
			throw new SecurityException("Bad Credentials");
		}

	}

	private Claims decodeToken(String xAuthToken) throws JwtException, SignatureException {
		Claims claims = null;

		if (xAuthToken != null) {
			claims = Jwts.parser().setSigningKey(SecurityConstants.JWT_SECRET_KEY.getBytes()).parseClaimsJws(xAuthToken)
					.getBody();
		}
		return claims;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String path = request.getServletPath();
		return path.contains("/openapi/");
	}

}
