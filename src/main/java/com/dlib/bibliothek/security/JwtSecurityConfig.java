
package com.dlib.bibliothek.security;

import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dlib.bibliothek.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JwtSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	private String ldapDomain = "thinkpalm.com";

	private String ldapUrl = "ldap://192.168.1.40"; // Correct

	private String ldapDn = "ou=Thinkpalm_Users,DC=dc,DC=thinkpalm,DC=info"; // Correct

	@Override
	protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
		authManagerBuilder.authenticationProvider(activeDirectoryLdapAuthenticationProvider())
				.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public JwtAuthTokenFilter authenticationJwtTokenFilter() {
		return new JwtAuthTokenFilter();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(Arrays.asList(activeDirectoryLdapAuthenticationProvider()));
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests()
				// allow anonymous resource requests
				.antMatchers(HttpMethod.GET, "/", "/v2/api-docs", // swagger
						"/webjars", // swagger-ui webjars
						"/swagger-resources", // swagger-ui resources
						"/configuration", // swagger configuration
						".html", "/favicon.ico", ".html", ".css", ".js")
				.permitAll().antMatchers("/actuator").permitAll().antMatchers("/api/user").permitAll()
				.antMatchers("/api/preference").permitAll().and().authorizeRequests().antMatchers("/h2-console")
				.permitAll().antMatchers(HttpMethod.GET, "/book/images").permitAll()
				.antMatchers(HttpMethod.GET, "/book/pdf").permitAll().anyRequest().authenticated().and()
				.exceptionHandling().authenticationEntryPoint((request, response, e) -> {
					response.setContentType("application/json;charset=UTF-8");
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter()
							.write(new JSONObject().put("error", true)
									.put("error_code", HttpStatus.UNAUTHORIZED.value()).put("message", "Unauthorized")
									.toString());
				}).and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.headers().frameOptions().disable();
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	/**
	 * 
	 * method to ignore http requests.
	 * 
	 * @param web web
	 */
	@Override
	public void configure(final WebSecurity web) {
		web.ignoring().antMatchers(HttpMethod.OPTIONS);
	}

	@Bean
	public AuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
		ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider(ldapDomain,
				ldapUrl, ldapDn);
		provider.setConvertSubErrorCodesToExceptions(true);
		provider.setUseAuthenticationRequestCredentials(true);

		return provider;
	}
}
