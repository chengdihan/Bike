package com.example.bike.config;

import com.example.bike.dal.models.UserAuthentication;
import com.example.bike.config.DuplexAuthenticationToken.AuthenticationSource;
import com.example.bike.dal.repositories.UserAuthenticationRepository;
import com.example.bike.dal.repositories.UserRepository;
import com.example.bike.utils.AuthorityBuilder;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BikeAuthenticationProvider implements AuthenticationProvider {

  private PasswordEncoder passwordEncoder;

  final
  UserRepository userRepository;

  final
  UserAuthenticationRepository userAuthenticationRepository;

  @Value("${spring.profiles.active}")
  String profile;

  public BikeAuthenticationProvider(
      UserRepository userRepository, UserAuthenticationRepository userAuthenticationRepository) {
    this.userRepository = userRepository;
    this.userAuthenticationRepository = userAuthenticationRepository;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

//    DuplexAuthenticationToken duplexAuthentication = (DuplexAuthenticationToken) authentication;

//    if (duplexAuthentication.getIsOAuth() != null && duplexAuthentication.getIsOAuth()) {
//      return authenticateOAuthLogin(authentication);
//    } else
    {
      return authenticateUserNameLogin(authentication);
    }
  }

//  private DuplexAuthenticationToken authenticateOAuthLogin(Authentication authentication) {
//
//    DuplexAuthenticationToken duplexAuthentication = (DuplexAuthenticationToken) authentication;
//
//    OAuthSource oAuthSource = duplexAuthentication.getOauthSource();
//
//    if (oAuthSource == OAuthSource.Google) {
//      String token = duplexAuthentication.getGoogleAuthToken();
//
//      User user;
//      try {
//        user = verifyToken(token);
//      } catch (Exception e) {
//        log.error("Error authenticating with Google", e);
//        throw new RuntimeException("Error authenticating with Google", e);
//      }
//
//      if (user == null) {
//        throw new RuntimeException("Not a registered user");
//      }
//
//      List<User> matchingUsers = userDao.findByUsernameIgnoreCase(user.getUsername());
//      //Check if user has multiple accounts
//
//      List<GrantedAuthority> authorities = AuthorityBuilder
//          .buildAdminAuthority();
//      DuplexAuthenticationToken duplexAuthenticationToken = new DuplexAuthenticationToken(
//          user.getUsername(), null, authorities,
//          AuthenticationSource.User);
//      duplexAuthenticationToken.setUserId(user.getId());
//      return duplexAuthenticationToken;
//    } else {
//      log.info("Not a supported OAuth source");
//      throw new RuntimeException("Not a supported OAuth source");
//    }
//  }

  private Authentication authenticateUserNameLogin(Authentication authentication) {
    DuplexAuthenticationToken duplexAuthentication = (DuplexAuthenticationToken) authentication;

    DuplexAuthenticationToken authToken = null;

    String userName = duplexAuthentication.getName();
    String password = duplexAuthentication.getCredentials().toString();
    AuthenticationSource source = duplexAuthentication.getSource();

    //Check if username is an email
    if (EmailValidator.getInstance().isValid(userName)) {
      String email = userName;
      if ("local".equalsIgnoreCase(profile) && "admin@admin.com".equalsIgnoreCase(email)) {
        return new DuplexAuthenticationToken("admin@admin.com", password,
            AuthorityBuilder.buildTestingAuthority(), AuthenticationSource.User);
      }

      if (source == null || source.equals(AuthenticationSource.User)) {
        authToken = validateUserWithEmail(email, password);
      }
    } else {
      if ("local".equalsIgnoreCase(profile) && "admin".equalsIgnoreCase(userName)) {
        return new DuplexAuthenticationToken("admin", password,
            AuthorityBuilder.buildTestingAuthority(), AuthenticationSource.User);
      }

      authToken = validateUserWithUsername(userName, password);
    }

    return authToken;
  }

  private DuplexAuthenticationToken validateUserWithEmail(String email, String password) {
    List<UserAuthentication> matchingUsers = userAuthenticationRepository.findByUserEmail(email);
    return validateUser(email, password, matchingUsers);
  }

  private DuplexAuthenticationToken validateUserWithUsername(String userName, String password) {
    List<UserAuthentication> matchingUsers = userAuthenticationRepository.findByUserName(userName);
    return validateUser(userName, password, matchingUsers);
  }

  private DuplexAuthenticationToken validateUser(String email, String password,
      List<UserAuthentication> matchingUsers) {

    UserAuthentication user = matchingUsers.get(0);

    if (user != null && this.passwordEncoder.matches(password, user.getUserPassword())) {
      String emailAccount = email;
      List<GrantedAuthority> authorities = AuthorityBuilder.buildAdminAuthority();
      return new DuplexAuthenticationToken(emailAccount, password, authorities,
          AuthenticationSource.User);
    }

    return null;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(DuplexAuthenticationToken.class);
  }

  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

//  private User verifyToken(String idTokenString)
//      throws GeneralSecurityException, IOException {
//
//    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//    GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, JSON_FACTORY)
//        .setAudience(Collections.singletonList(clientId))
//        .build();
//
//    GoogleIdToken idToken = verifier.verify(idTokenString);
//
//    if (idToken != null) {
//      Payload payload = idToken.getPayload();
//
//      // Get profile information from payload
//      String userId = payload.getSubject();
//      String email = payload.getEmail();
//      //boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
//      String name = (String) payload.get("name");
//      String pictureUrl = (String) payload.get("picture");
//      //String locale = (String) payload.get("locale");
//      String familyName = (String) payload.get("family_name");
//      String givenName = (String) payload.get("given_name");
//
//      // Use or store profile information
//      User user = userDao.findByEmailIgnoreCase(email).stream().findFirst().orElse(null);
//
//      if (user != null) {
//        if (StringUtils.isEmpty(user.getGoogleUserId())) {
//          user.setGoogleUserId(userId);
//        }
//        if (StringUtils.isEmpty(user.getGooglePictureUrl())) {
//          user.setGooglePictureUrl(pictureUrl);
//        }
//        if (StringUtils.isEmpty(user.getFirstname())) {
//          user.setFirstname(givenName);
//        }
//        if (StringUtils.isEmpty(user.getLastname())) {
//          user.setLastname(familyName);
//        }
//        user = userDao.save(user);
//      }
//
//      return user;
//    } else {
//      log.error("Invalid google token: " + idTokenString);
//      return null;
//    }
//  }
}