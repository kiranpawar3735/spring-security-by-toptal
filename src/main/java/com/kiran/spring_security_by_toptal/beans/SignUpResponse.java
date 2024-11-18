package com.kiran.spring_security_by_toptal.beans;

import com.kiran.spring_security_by_toptal.entity.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.stream.Collectors;

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