package com.kiran.spring_security_by_toptal.beans;

import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;

import com.kiran.spring_security_by_toptal.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SignUpResponse {

    private String username;

    private String email;

    private String roles;

    public SignUpResponse(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
}