package com.kiran.spring_security_by_toptal.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		String token = null;
		String username = null;

		String authorizationHeader = request.getHeader("Authorization");

		try {
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				token = authorizationHeader.substring("Bearer ".length());
				username = jwtTokenUtil.getUsername(token);
			}
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				if (jwtTokenUtil.validate(token)) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());

					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
//			else if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
//				// Extract username and password from the Basic Authentication header
//		        String authHeader = authorizationHeader.replace("Basic ", "");
//		        String decoded = new String(Base64.getDecoder().decode(authHeader), StandardCharsets.UTF_8);
//		        String[] credentials = decoded.split(":", 2);
//		        
//		        username = credentials[0];
//		        String password = credentials[1];
//		        
//		        // Perform the authentication
//		        Authentication authentication = authenticationManager.authenticate(
//		            new UsernamePasswordAuthenticationToken(username, password)
//		        );
//		        
//		        // Set authentication in the security context
//				SecurityContextHolder.getContext().setAuthentication(authentication);
//			}
			filterChain.doFilter(request, response);
		} catch (

		ExpiredJwtException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		} catch (AuthenticationException e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		}
//
//        String requestUri = request.getRequestURI();
//        String token = getTokenFromRequest(request);
//
//        if (token == null) {
//            // No token provided, redirect to login page if not already on it
//            if (!requestUri.equals("/auth/login")) {
//                response.sendRedirect("/auth/login");
//                return;
//            }
//
//            // Handle incomplete login request
//            if (isIncompleteLoginRequest(request)) {
//                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                response.getWriter().write("Username and password are required");
//                return;
//            }
//        } else if (jwtTokenProvider.validate(token)) {
//            // Validate token and set security context
//            String username = jwtTokenProvider.getUsername(token);
//            SecurityContextHolder.getContext().setAuthentication(
//                    new UsernamePasswordAuthenticationToken(username, null, null)
//            );
//        }
//
//        // Proceed with the filter chain
//        filterChain.doFilter(request, response);
	}

	private String getTokenFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	private boolean isIncompleteLoginRequest(HttpServletRequest request) {
		// Check if the login request lacks username or password
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		return (username == null || username.isEmpty() || password == null || password.isEmpty());
	}
}