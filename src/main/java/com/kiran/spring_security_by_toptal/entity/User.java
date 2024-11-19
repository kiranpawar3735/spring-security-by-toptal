package com.kiran.spring_security_by_toptal.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "users")
public class User implements UserDetails, CredentialsContainer {
	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	@Id
	private String userId;

	@Indexed(unique = true)
	private String username;

	private String password;

	@Indexed(unique = true)
	private String email;

	private boolean accountNonExpired;

	private boolean accountNonLocked;

	private boolean credentialsNonExpired;

	private boolean enabled;

	private Set<? extends GrantedAuthority> authorities;

	public User(String username, String password, String email, Collection<? extends GrantedAuthority> authorities) {
		this(username, password, email, true, true, true, true, authorities);
	}

	public User(String username, String password, String email, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		Assert.isTrue(username != null && !username.isEmpty() && password != null,
				"Cannot pass null or empty values to constructor");
		this.username = username;
		this.password = password;
		this.email = email;
		this.enabled = enabled;
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.authorities = Collections.unmodifiableSet(sortedAuthorities(authorities));
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public void eraseCredentials() {
		this.password = null;
	}

	private static TreeSet<? extends GrantedAuthority> sortedAuthorities(
			Collection<? extends GrantedAuthority> authorities) {
		return authorities.stream().filter(Objects::nonNull).collect(Collectors.toCollection(() -> new TreeSet<>(
				Comparator.comparing(GrantedAuthority::getAuthority, Comparator.nullsFirst(String::compareTo)))));
	}
}