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
				.requestMatchers("/images/**", "/css/**", "/js/**", "/webjars/**", "/favicon.ico", "/login","static/**","/error")
				.permitAll()
				.requestMatchers("/register-user", "/", "/welcome","/about-us","/test").permitAll()
				.requestMatchers("/god-admin-home").hasRole("GODADMIN")
				.requestMatchers("/admin-home").hasRole("ADMIN").requestMatchers("/user-home")
				.hasAnyRole("GODADMIN","ADMIN", "USER").anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").successHandler(new SuccessHandler()).permitAll())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessHandler(new LogoutSuccessHandler())
						.clearAuthentication(true));

		return http.build();
	}



}