package org.vasaviyuvajanasangha.kvcl.handlers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws ServletException, IOException {
		
		boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"));
		logger.info("auth -> {}",authentication);
		if(isAdmin) {
			setDefaultTargetUrl("/admin/admin-home");
		}else {
			setDefaultTargetUrl("/user-home");
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
	}

	
}
