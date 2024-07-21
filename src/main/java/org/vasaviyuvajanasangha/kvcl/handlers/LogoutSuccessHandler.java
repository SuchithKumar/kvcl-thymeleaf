package org.vasaviyuvajanasangha.kvcl.handlers;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UrlPathHelper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		UrlPathHelper pathHelper = new UrlPathHelper();
		String context = pathHelper.getContextPath(request);
		
		response.sendRedirect(context+"/welcome");
	}
	
	

}
