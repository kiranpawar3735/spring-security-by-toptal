package com.kiran.spring_security_by_toptal.beans;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LoginResponse {

    private String accessToken;

    private String refreshToken;
}
