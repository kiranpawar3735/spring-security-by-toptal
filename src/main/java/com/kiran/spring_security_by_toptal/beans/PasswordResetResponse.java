package com.kiran.spring_security_by_toptal.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PasswordResetResponse {

    private String resetToken;

    private String newPassword;
}
