package com.kiran.spring_security_by_toptal.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// After username password authentication filter
		var accessToken = jwtTokenUtil.generateToken(authentication);
		var refreshToken = jwtTokenUtil.generateRefreshToken(authentication);
		response.addHeader("Authorization", "Bearer " + accessToken);
		response.addHeader("Refresh-Token", refreshToken);
		response.setStatus(HttpServletResponse.SC_OK);
	}
}