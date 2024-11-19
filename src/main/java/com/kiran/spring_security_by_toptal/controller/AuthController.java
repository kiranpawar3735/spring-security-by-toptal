package com.kiran.spring_security_by_toptal.controller;

import com.kiran.spring_security_by_toptal.beans.*;
import com.kiran.spring_security_by_toptal.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        return authService.signUp(signUpRequest)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody Tokens tokens) {
        String refreshToken = tokens.getRefreshToken();
        return authService.invalidate(refreshToken)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @PostMapping("/logout-all")
    public ResponseEntity<String> logoutAll(@RequestBody Tokens tokens) {
        String refreshToken = tokens.getRefreshToken();
        return authService.invalidateAll(refreshToken)
                .map(ResponseEntity::ok)//"All sessions logged out successfully!"
                .orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Tokens> getAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.getAccessToken(refreshTokenRequest.getRefreshToken())
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @PostMapping("/password-reset-request")
    public ResponseEntity<PasswordResetResponse> verifyPasswordResetRequest(@RequestBody Tokens tokens) {
        return authService.verifyPasswordResetRequest(tokens)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @PostMapping("/password-reset")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) {
        return authService.resetPassword(passwordResetRequest)
                .map(ResponseEntity::ok)//"Password reset successful. Try login."
                .orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestBody EmailVerification emailVerification) {
        return authService.verifyEmail(emailVerification)
                .map(ResponseEntity::ok)//"Email verified successful. Try login."
                .orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
    }
}