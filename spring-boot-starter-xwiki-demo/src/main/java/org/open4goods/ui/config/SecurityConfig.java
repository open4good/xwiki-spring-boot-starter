package org.open4goods.ui.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
/**
 * HTTP Security configuration
 * @author Goulven.Furet
 *
 */
public class SecurityConfig {

	@Autowired
	@Qualifier("xwikiAuthenticationProvider")
	private AuthenticationProvider  authProvider;
	
//	public WebConfig(AuthenticationProvider authProvider) {
//		this.authProvider = authProvider;
//	}


	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

					http.authorizeRequests()
					.requestMatchers(new AntPathRequestMatcher("/", "/content")).permitAll()
					.requestMatchers(new AntPathRequestMatcher("/protected.html")).authenticated()
		            .and().formLogin().permitAll()
		            .and().logout().permitAll();
			return http.build();


	}



	@Bean
	// TODO : Remove ?
	AuthenticationManager authManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder =
				http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.authenticationProvider(authProvider);
		return authenticationManagerBuilder.build();
	}
//
//	@Bean
//	// TODO : remove ?
//	PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
}