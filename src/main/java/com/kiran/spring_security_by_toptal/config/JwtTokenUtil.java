package com.kiran.spring_security_by_toptal.config;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.kiran.spring_security_by_toptal.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtil {

	private static final String ISSUER = "user-data-service";

	@Autowired
	private UserDetailsService userDetailsService;

	private long accessTokenExpirationMs;
	private long accessTokenExpirationMinutes = 5l;
	private long refreshTokenExpirationMs;
	private long refreshTokenExpirationDays = 1l;
	private static String accessTokenSecret = "trFDluht6T7ujDfNyaAumoiKFiFDCIwvm36Un1QnZW++NL1J828XYcUNUQKTPotv0YzuSX815qqpK8GfNDD3Ow==";
	private static String refreshTokenSecret = "mMHZUefIS59guYPZ8KHqd3JBZN7YF2rm1Di/2DV7mFCG4eUcTT1NNPByCoWlQIkRDbWGfg3w4bG5AkHeFuYBQg==";

	public JwtTokenUtil() {
		this.accessTokenExpirationMs = accessTokenExpirationMinutes * 60 * 1000;
		this.refreshTokenExpirationMs = refreshTokenExpirationDays * 24 * 60 * 60 * 1000;
	}

	public String getUsername(String token) {
		return extractUsername(token);
	}

	public boolean validate(String token) {
		try {
			final String username = extractUsername(token);
			User user = (User) userDetailsService.loadUserByUsername(username);
			return !isTokenExpired(token)
					&& user.getUserId().equalsIgnoreCase(extractAllClaims(token).get("tokenId").toString());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public String generateRefreshToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("tokenId", user.getUserId());
		return createToken(user.getUsername(), claims, refreshTokenExpirationMs, refreshTokenSecret);
	}

	public String generateToken(Authentication authentication) {
		return generateToken((User) authentication.getPrincipal());
	}

	public String generateToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("tokenId", user.getUserId());
		return createToken(user.getUsername(), claims, accessTokenExpirationMs, accessTokenSecret);

	}

	private String createToken(String username, Map<String, Object> claims, long expirationMillis, String secret) {
		Key key = new SecretKeySpec(secret.getBytes(), "HmacSHA512");
		return Jwts.builder().claims(claims).subject(username).issuer(ISSUER).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + expirationMillis))
//                .signWith(key, io.jsonwebtoken.SignatureAlgorithm.HS256)
				.signWith(key).compact();
	}

	private Key generateHS256Key(String secret) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			byte[] digested = messageDigest.digest(secret.getBytes(StandardCharsets.UTF_8));
			digested = Arrays.copyOf(digested, 64);
			return new SecretKeySpec(digested, Jwts.SIG.HS512.getId());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getRefreshTokenId(String refreshToken) {
		return null;
	}

	public String getRefreshTokenUserId(String refreshToken) {
		return null;
	}

	public boolean validateRefreshToken(String token) {
		try {
			final String username = extractUsername(token);
			User user = (User) userDetailsService.loadUserByUsername(username);
			return !isTokenExpired(token)
					&& user.getUserId().equalsIgnoreCase(extractAllClaims(token).get("tokenId").toString());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		try {
			final Claims claims = extractAllClaims(token);
			return claimsResolver.apply(claims);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().requireIssuer(ISSUER).verifyWith(Keys.hmacShaKeyFor(accessTokenSecret.getBytes())).build()
				.parseSignedClaims(token).getPayload();
	}

	private Boolean isTokenExpired(String token) {
		Date issuedAt = extractIssuedAt(token);
		String issuer = extractIssuer(token);
		if (issuer.equalsIgnoreCase(ISSUER)) {
			return issuedAt.before(new Date(accessTokenExpirationMs));
		} else {
			return extractExpiration(token).before(new Date());
		}
	}

	private String extractIssuer(String token) {
		return extractClaim(token, Claims::getIssuer);
	}

	private Date extractIssuedAt(String token) {
		return extractClaim(token, Claims::getIssuedAt);
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
}