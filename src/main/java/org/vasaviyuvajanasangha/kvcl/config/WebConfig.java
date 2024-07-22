package org.vasaviyuvajanasangha.kvcl.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.vasaviyuvajanasangha.kvcl.handlers.LogoutSuccessHandler;
import org.vasaviyuvajanasangha.kvcl.handlers.SuccessHandler;
import org.vasaviyuvajanasangha.kvcl.service.MyUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebConfig {

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsManager userDetailsManager() {
		return new MyUserDetailsManager();
	}

	@Bean
	public AuthenticationProvider authProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(encoder());
		provider.setUserDetailsService(userDetailsManager());
		return provider;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth
//                		.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations())
				.permitAll()
				.requestMatchers("/images/**", "/css/**", "/js/**", "/WEB-INF/jsp/**", "/favicon.ico", "/login","static/**")
				.permitAll()
				.requestMatchers("/register-user", "/", "/welcome").permitAll()
				.requestMatchers("/admin-home").hasAnyRole("ADMIN").requestMatchers("/user-home")
				.hasAnyRole("ADMIN", "USER").anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").successHandler(new SuccessHandler()).permitAll())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessHandler(new LogoutSuccessHandler())
						.clearAuthentication(true));
//				/error?continue HTTP/1.1" 500
//		2024-07-21 15:21:01 default[20240721t185207]  "GET /favicon.ico HTTP/1.1" 404
//		2024-07-21 15:21:06 default[20240721t185207]  "GET /error?continue HTTP/1.1" 500
		return http.build();
	}



}