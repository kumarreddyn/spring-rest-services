package rest.services.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	RestAPIAuthenticationFilter customAuthenticationFilter;
	
	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.authenticationProvider(new RestAPIAuthenticationProvider());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and()
			.csrf().disable()
			.authorizeRequests()
			.antMatchers("/**/openapi/**").permitAll()
			.antMatchers("/**").authenticated().and()
			.addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);	        
	}
	
	
	
}
