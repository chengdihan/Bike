package com.example.bike.utils;

import com.example.bike.config.DuplexAuthenticationToken.AuthenticationSource;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenUtil implements Serializable {

  private static final long serialVersionUID = 2015649597855109512L;

  //In seconds (day * hour * minutes * seconds)
  public static final long JWT_TOKEN_VALIDITY = 7 * 24 * 60 * 60;

  public static final String AUTHORITIES_KEY = "AUTH_KEY";
  public static final String AUTH_REPO = "AUTH_REPO";
  public static final String jwtTokenCookieName = "JWT-TOKEN";

  @Value("${jwt.secret}")
  private String secret;

  /**
   * Generate an unique jwt token user name with account id
   */
  public String createJwtTokenUsername(String username) {
    return username;
  }

  //retrieve username from jwt token
  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  //retrieve expiration date from jwt token
  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  /**
   * Get the authorities stored on JWT Token
   */
  public Collection<GrantedAuthority> getAuthorities(String token) {
    final Claims claims = getAllClaimsFromToken(token);
    final Collection<GrantedAuthority> authorities =
        Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

    return authorities;
  }

  public AuthenticationSource getAuthRepository(String token) {
    final Claims claims = getAllClaimsFromToken(token);
    String authRepo = (String) claims.get(AUTH_REPO);
    if (StringUtils.isEmpty(authRepo)) {
      return null;
    } else {
      try {
        return AuthenticationSource.valueOf(authRepo);
      } catch (Exception e) {
        log.error("Error evaluating Authentication Source using value {}", authRepo, e);
        return null;
      }
    }
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  //for retrieveing any information from token we will need the secret key
  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  //check if the token has expired
  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  //generate token for user
  public String generateToken(String username, List<GrantedAuthority> grantedAuthorities) {
    final String authorities = grantedAuthorities.stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    Map<String, Object> claims = new HashMap<>();
    claims.put(AUTHORITIES_KEY, authorities);
    return doGenerateToken(claims, username);
  }

  //generate token for user
  public String generateToken(String username, AuthenticationSource authSource,
      List<GrantedAuthority> grantedAuthorities) {
    final String authorities = grantedAuthorities.stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    Map<String, Object> claims = new HashMap<>();
    claims.put(AUTHORITIES_KEY, authorities);
    claims.put(AUTH_REPO, authSource);
    return doGenerateToken(claims, username);
  }

  //while creating the token -
  //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
  //2. Sign the JWT using the HS512 algorithm and secret key.
  //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
  //   compaction of the JWT to a URL-safe string
  private String doGenerateToken(Map<String, Object> claims, String subject) {

    return Jwts.builder().setClaims(claims).setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
        .signWith(SignatureAlgorithm.HS512, secret).compact();
  }

  //validate token
  public Boolean validateToken(String token, UserDetails userDetails) {
    final String userName = getUsernameFromToken(token);

    return (userName.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired(token));
  }
}