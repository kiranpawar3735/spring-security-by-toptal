package com.kiran.spring_security_by_toptal.beans;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Tokens {

    private String accessToken;

    private String refreshToken;
}
