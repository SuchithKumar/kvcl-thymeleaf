package org.vasaviyuvajanasangha.kvcl.handlers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.vasaviyuvajanasangha.kvcl.service.PlayerServiceImpl;

@Service
public class SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private PlayerServiceImpl playerService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws ServletException, IOException {
		
		boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"));
		boolean isSuperAdmin = authentication.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_GODADMIN"));

		if(isSuperAdmin) {
			setDefaultTargetUrl("/user-home");
		}else if(isAdmin) {
			setDefaultTargetUrl("/user-home");
		}
		else {
			setDefaultTargetUrl("/user-home");
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
	}

	
}
