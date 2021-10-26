package com.contentlab.teams.java.channeltabsso;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(value = 0)
public class AppConfiguration extends WebSecurityConfigurerAdapter
{
	// @Value("${spring.security.oauth2.resourceserver.jwt.key-value}")
	// RSAPublicKey key;

 	@Override
 	protected void configure(HttpSecurity http) throws Exception {
 		http.headers().contentSecurityPolicy("frame-ancestors 'self' https://*.microsoftonline.com  https://*.microsoft.com;");
	}
}