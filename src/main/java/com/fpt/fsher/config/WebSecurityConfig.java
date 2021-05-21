package com.fpt.fsher.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fpt.fsher.dao.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	};

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService()).passwordEncoder(bCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {

		httpSecurity.authorizeRequests().antMatchers("/create-user/**", "/update-user/**", "/delete-user/**")
				.access("hasAuthority('ROLE_admin')");

		httpSecurity.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");

		httpSecurity.authorizeRequests()
		.antMatchers("/singup", "/login", "/logout")
				.permitAll()
				.and()
				.authorizeRequests()
				.antMatchers("/create-user", "/update-user/{id}", "/delete-user/{id}")
				.authenticated() // trang nao can dang
									// nhap roi
				.and().formLogin().loginPage("/login").permitAll().and().logout().logoutUrl("/logout").permitAll();

	}

	public void configure(WebSecurity webSecurity) throws Exception {
		webSecurity.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/img/** ",
				"/fonts/**", "/bootstrap/***");

	}

	public AuthenticationManager customAuthenticationManager() throws Exception {
		return authenticationManager();
	}

}
