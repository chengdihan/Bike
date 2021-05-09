package com.example.bike.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class AuthorityBuilder {

  public static List<GrantedAuthority> buildAdminAuthority() {

    Set<GrantedAuthority> setAuths = new HashSet<>();

    List<GrantedAuthority> Result = new ArrayList<>(setAuths);

    return Result;
  }

  public static List<GrantedAuthority> buildTestingAuthority() {

    Set<GrantedAuthority> setAuths = new HashSet<>();

    // Build development testing authorities
    setAuths.add(new SimpleGrantedAuthority("User"));
    setAuths.add(new SimpleGrantedAuthority("SuperAdmin"));

    List<GrantedAuthority> Result = new ArrayList<>(setAuths);

    return Result;
  }
}