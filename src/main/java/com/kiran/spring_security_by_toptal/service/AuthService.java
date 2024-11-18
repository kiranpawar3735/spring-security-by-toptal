package com.kiran.spring_security_by_toptal.service;

import com.kiran.spring_security_by_toptal.beans.*;
import com.kiran.spring_security_by_toptal.entity.User;

import java.util.Optional;

public interface AuthService {

    Optional<SignUpResponse> signUp(SignUpRequest signUpRequest);

    Optional<LoginResponse> login(LoginRequest loginRequest);

    Optional<String> invalidate(String refreshToken);

    Optional<String> invalidateAll(String refreshToken);

    Optional<Tokens> getAccessToken(String refreshToken);

    Optional<PasswordResetResponse> verifyPasswordResetRequest(Tokens tokens);

    Optional<String> resetPassword(PasswordResetRequest passwordResetRequest);

    Optional<String> verifyEmail(EmailVerification emailVerification);
}