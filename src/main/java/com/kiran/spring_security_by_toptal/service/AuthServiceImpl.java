package com.kiran.spring_security_by_toptal.service;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kiran.spring_security_by_toptal.beans.EmailVerification;
import com.kiran.spring_security_by_toptal.beans.LoginRequest;
import com.kiran.spring_security_by_toptal.beans.LoginResponse;
import com.kiran.spring_security_by_toptal.beans.PasswordResetRequest;
import com.kiran.spring_security_by_toptal.beans.PasswordResetResponse;
import com.kiran.spring_security_by_toptal.beans.SignUpRequest;
import com.kiran.spring_security_by_toptal.beans.SignUpResponse;
import com.kiran.spring_security_by_toptal.beans.Tokens;
import com.kiran.spring_security_by_toptal.config.JwtTokenUtil;
import com.kiran.spring_security_by_toptal.entity.User;
import com.kiran.spring_security_by_toptal.repository.RefreshTokenRepository;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Override
	public Optional<SignUpResponse> signUp(SignUpRequest signUpRequest) {
		User user = new User(signUpRequest.getUsername(), passwordEncoder.encode(signUpRequest.getPassword()),
				signUpRequest.getEmail(), Arrays.stream(signUpRequest.getRoles().trim().split(","))
						.map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
		return Optional.of(userService.createUser(user).map(SignUpResponse::new)
				.orElseThrow(() -> new IllegalArgumentException("User creation failed")));
	}

	@Override
	public Optional<LoginResponse> login(LoginRequest loginRequest) {
		Authentication authentication = this.authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		if (authentication.isAuthenticated())
			SecurityContextHolder.getContext().setAuthentication(authentication);
		else
			SecurityContextHolder.clearContext();

		return Optional.of(LoginResponse.builder().accessToken(jwtTokenUtil.generateToken(authentication))
				.refreshToken(jwtTokenUtil.generateRefreshToken((User) authentication.getPrincipal())).build());
	}

	@Override
	public Optional<String> invalidate(String refreshToken) {
		if (jwtTokenUtil.validateRefreshToken(refreshToken)) {
			refreshTokenRepository.deleteById(jwtTokenUtil.getRefreshTokenId(refreshToken));
			return Optional.of("Logged out successfully!");
		}
		throw new BadCredentialsException("invalid refresh token");
	}

	@Override
	public Optional<String> invalidateAll(String refreshToken) {
		if (jwtTokenUtil.validateRefreshToken(refreshToken)) {
			refreshTokenRepository.deleteByUser_UserId(jwtTokenUtil.getRefreshTokenUserId(refreshToken));
			return Optional.of("All sessions logged out successfully!");
		}
		throw new BadCredentialsException("invalid refresh token");
	}

	@Override
	public Optional<Tokens> getAccessToken(String refreshToken) {
		if (jwtTokenUtil.validateRefreshToken(refreshToken)) {
			return userService.getUser(jwtTokenUtil.getRefreshTokenUserId(refreshToken))
					.map(jwtTokenUtil::generateToken)
					.map(token -> Optional.of(Tokens.builder().accessToken(token).refreshToken(refreshToken).build()))
					.orElseThrow(() -> new BadCredentialsException("invalid refresh token"));
		}
		throw new BadCredentialsException("invalid refresh token");
	}

	@Override
	public Optional<PasswordResetResponse> verifyPasswordResetRequest(Tokens tokens) {
		return Optional.empty();
	}

	@Override
	public Optional<String> resetPassword(PasswordResetRequest passwordResetRequest) {
		return Optional.empty();
	}

	@Override
	public Optional<String> verifyEmail(EmailVerification emailVerification) {
		return Optional.empty();
	}
}
