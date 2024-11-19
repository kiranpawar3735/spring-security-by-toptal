package com.kiran.spring_security_by_toptal.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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

		String authorization = request.getHeader("Authorization");

		if (authorization != null && authorization.startsWith("Bearer ")) {
			token = authorization.substring("Bearer ".length());
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
		filterChain.doFilter(request, response);

//        String authorization = request.getHeader("Authorization");
////
//        if (authorization == null) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("Authorization header is missing");
//            return;
//        }
//
//        if (authorization.startsWith("Bearer ")) {
////            // Handle JWT Authentication
//            String token = authorizationHeader.substring(7);
//            if (jwtTokenProvider.validate(token)) {
//                String username = jwtTokenProvider.getUsername(token);
//                SecurityContextHolder.getContext().setAuthentication(
//                        new UsernamePasswordAuthenticationToken(username, null, null)
//                );
//            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().write("Invalid JWT token");
//                return;
//            }
//        } else if (authorizationHeader.startsWith("Basic ")) {
//            // Handle Basic Authentication for login
//            String credentials = authorizationHeader.substring(6);
//            String decodedCredentials = new String(Base64.getDecoder().decode(credentials));
//            String[] parts = decodedCredentials.split(":", 2);
//
//            if (parts.length != 2) {
//                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                response.getWriter().write("Invalid Basic Authentication header");
//                return;
//            }
//
//            String username = parts[0];
//            String password = parts[1];
//
//            try {
//                Authentication authentication = authenticationManager.authenticate(
//                        new UsernamePasswordAuthenticationToken(username, password)
//                );
//
//                // Generate JWT after successful authentication
//                String token = jwtTokenProvider.generateToken(authentication);
//                response.setStatus(HttpServletResponse.SC_OK);
//                response.getWriter().write("Bearer " + token);
//                return;
//            } catch (Exception e) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().write("Invalid username or password");
//                return;
//            }
//        } else {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            response.getWriter().write("Unsupported Authorization type");
//            return;
//        }
//
//        filterChain.doFilter(request, response);
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