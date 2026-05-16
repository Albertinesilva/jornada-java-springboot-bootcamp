package com.albertsilva.dev.dscatalog.security.userdetails;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class AuthenticatedUser {

  private final String username;

  private final Collection<? extends GrantedAuthority> authorities;

  public AuthenticatedUser(String username, Collection<? extends GrantedAuthority> authorities) {
    this.username = username;
    this.authorities = authorities;
  }

  public String getUsername() {
    return username;
  }

  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }
}