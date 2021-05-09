package com.example.bike.dal.pojo;

import java.util.Collection;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Data
public class AuthUser extends User {

  private static final long serialVersionUID = 1210384777917049867L;

  public enum UserType {
    USER
  }

  private final Long userId;
  private final UserType type;
  private final String firstName;
  private final String lastName;

  public AuthUser(String username, String password,
      Collection<? extends GrantedAuthority> authorities, Long userId, UserType type,
      String firstName, String lastName) {
    super(username, password, authorities);
    this.type = type;
    this.userId = userId;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public AuthUser(String username, String password, boolean enabled, boolean accountNonExpired,
      boolean credentialsNonExpired, boolean accountNonLocked,
      Collection<? extends GrantedAuthority> authorities,
      Long userId, UserType type, String firstName, String lastName) {
    super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked,
        authorities);

    this.userId = userId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.type = type;
  }
}
