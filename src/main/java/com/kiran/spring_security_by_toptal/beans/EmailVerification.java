package com.kiran.spring_security_by_toptal.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailVerification {

    private String email;

    private String verificationCode;
}
