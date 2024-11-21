package com.kiran.spring_security_by_toptal.service;

import java.util.Optional;

import com.kiran.spring_security_by_toptal.beans.EmailVerification;
import com.kiran.spring_security_by_toptal.beans.LoginRequest;
import com.kiran.spring_security_by_toptal.beans.LoginResponse;
import com.kiran.spring_security_by_toptal.beans.PasswordResetRequest;
import com.kiran.spring_security_by_toptal.beans.PasswordResetResponse;
import com.kiran.spring_security_by_toptal.beans.SignUpRequest;
import com.kiran.spring_security_by_toptal.beans.SignUpResponse;
import com.kiran.spring_security_by_toptal.beans.Tokens;

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