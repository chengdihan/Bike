package com.example.bike.config;

import java.util.Collection;
import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class DuplexAuthenticationToken extends UsernamePasswordAuthenticationToken {

  public enum AuthenticationSource {
    User
  }

  public enum OAuthSource {
    Google, Facebook, Bing
  }

  private static final long serialVersionUID = 8254840208465878150L;
  private AuthenticationSource source;
  private String googleAuthToken;
  private Long userId;

  private Boolean isOAuth;
  private OAuthSource oauthSource;

  public DuplexAuthenticationToken(Object principal, Object credentials,
      AuthenticationSource source) {
    super(principal, credentials);
    this.source = source;
  }

  public DuplexAuthenticationToken(Object principal, Object credentials,
      Collection<? extends GrantedAuthority> authorities, AuthenticationSource source) {
    super(principal, credentials, authorities);
    this.source = source;
  }

  public DuplexAuthenticationToken(UserDetails userDetails,
      Collection<? extends GrantedAuthority> authorities, AuthenticationSource source) {
    super(userDetails == null ? null : userDetails.getUsername(),
        userDetails == null ? null : userDetails.getPassword(), authorities);
    this.source = source;
  }
}