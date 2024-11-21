package com.kiran.spring_security_by_toptal.config;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kiran.spring_security_by_toptal.repository.UserRepository;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeaders = request.getHeader(HttpHeaders.AUTHORIZATION);
        
        if (authHeaders==null || !authHeaders.startsWith("Bearer")) {
            //        continue filter chain
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeaders.split(" ")[1].trim();
        if (!jwtTokenUtil.validate(token)) {
        // continue filter chain
            filterChain.doFilter(request, response);
            return;
        }

        UserDetails userDetails = userRepository.findByUsername(jwtTokenUtil.getUsername(token))
                .orElse(null);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails == null ? List.of() : userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

//        set security context
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

//        continue filter chain
        filterChain.doFilter(request, response);
    }
}