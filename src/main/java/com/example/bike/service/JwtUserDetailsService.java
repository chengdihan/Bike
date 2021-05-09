package com.example.bike.service;

import com.example.bike.dal.models.UserAuthentication;
import com.example.bike.dal.pojo.AuthUser;
import com.example.bike.dal.repositories.UserAuthenticationRepository;
import com.example.bike.dal.repositories.UserRepository;
import com.example.bike.utils.AuthorityBuilder;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("jwtUserDetailsService")
public class JwtUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  private final UserAuthenticationRepository userAuthenticationRepository;

  @Value("${spring.profiles.active}")
  private String profile;

  public JwtUserDetailsService(UserRepository userRepository,
      UserAuthenticationRepository userAuthenticationRepository) {
    this.userRepository = userRepository;
    this.userAuthenticationRepository = userAuthenticationRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String userName)
      throws UsernameNotFoundException {
    //Development
    if ("local".equals(profile) && ("admin@admin.com".equals(userName) || "admin"
        .equals(userName))) {
      return buildDevUser(userName);
    }

    //Load User using username from DB
    return autoLoadUserFromDataSource(userName);
  }

  private User autoLoadUserFromDataSource(String userName) {
    return buildUser(userName);
  }

  private User buildDevUser(String userName) {
    Long userID = 1l;
    String firstName = "admin";
    String lastName = "admin";
    String password = "password";

    List<GrantedAuthority> authorities = AuthorityBuilder.buildTestingAuthority();

    return new AuthUser(userName, password, authorities, userID,
        AuthUser.UserType.USER, firstName, lastName);
  }

  private User buildUser(String userNameOrEmail) {
    List<UserAuthentication> userAuthentications = userAuthenticationRepository
        .findByUserNameOrUserEmail(userNameOrEmail, userNameOrEmail);
    UserAuthentication userAuthentication = userAuthentications.stream().findFirst().orElse(null);

    if (userAuthentication != null) {
      com.example.bike.dal.models.User user = userRepository.findByUserID(userAuthentication.getUserID());

      if(user != null) {
        List<GrantedAuthority> authorities = AuthorityBuilder.buildAdminAuthority();
        return new AuthUser(userNameOrEmail, userAuthentication.getUserPassword(), authorities, user.getUserID(),
            AuthUser.UserType.USER, user.getUserName(), user.getUserSurname());
      } else {
        return null;
      }
    } else {
      return null;
    }
  }
}