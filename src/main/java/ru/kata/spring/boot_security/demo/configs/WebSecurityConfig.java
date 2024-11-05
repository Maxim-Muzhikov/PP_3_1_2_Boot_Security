package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@Order(100)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private final SuccessUserHandler successUserHandler;
	private final UserDetailsService userDetailsService;
	
	@Autowired
	public WebSecurityConfig(SuccessUserHandler successUserHandler,
	                         UserDetailsService userDetailsService) {
		this.successUserHandler = successUserHandler;
		this.userDetailsService = userDetailsService;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.antMatchers("/", "/index", "/login").permitAll()
				.antMatchers("/admin/**").hasRole("ADMIN")
				.antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginPage("/login")
				.loginProcessingUrl("/login")
				.usernameParameter("email")
				.successHandler(successUserHandler)
				.permitAll()
				.and()
				.logout()
				.logoutUrl("/logout")
				.permitAll();
	}
	
}