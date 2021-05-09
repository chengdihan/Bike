package com.example.bike.web;

import com.example.bike.api.request.JwtRequest;
import com.example.bike.api.response.JwtResponse;
import com.example.bike.config.DuplexAuthenticationToken;
import com.example.bike.config.DuplexAuthenticationToken.AuthenticationSource;
import com.example.bike.dal.models.User;
import com.example.bike.dal.models.UserAuthentication;
import com.example.bike.dal.pojo.AuthUser;
import com.example.bike.dal.pojo.AuthUser.UserType;
import com.example.bike.dal.repositories.UserAuthenticationRepository;
import com.example.bike.utils.JwtTokenUtil;
import com.example.bike.dal.repositories.UserRepository;
import com.example.bike.utils.CookieUtil;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin
public class JwtAuthenticationController {

  private final AuthenticationManager authenticationManager;

  private final JwtTokenUtil jwtTokenUtil;

  private final UserRepository userRepository;

  private final UserAuthenticationRepository userAuthenticationRepository;

  @Value("${spring.profiles.active}")
  private String profile;

  @Value("${jwt.token.expiration}")
  private Integer tokenExpirationInSecond;

  public JwtAuthenticationController(
      AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
      UserRepository userRepository, UserAuthenticationRepository userAuthenticationRepository) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenUtil = jwtTokenUtil;
    this.userRepository = userRepository;
    this.userAuthenticationRepository = userAuthenticationRepository;
  }

  /**
   * Authenticate using email as the user name
   */
  @ApiOperation(value = "Authenticate user using email and password pair.  This endpoint could handle authentication for users.")
  @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
  public ResponseEntity<?> createAuthenticationToken(HttpServletResponse httpServletResponse,
      HttpServletRequest httpServletRequest,
      @RequestBody JwtRequest authenticationRequest) throws Exception {

    DuplexAuthenticationToken duplexToken = (DuplexAuthenticationToken) authenticate(
        authenticationRequest.getUserName(), authenticationRequest.getPassword());

    if (duplexToken == null) {
      throw new Exception("INVALID_CREDENTIALS");
    }

    String userName = authenticationRequest.getUserName();
    List<GrantedAuthority> authorities = duplexToken.getAuthorities().stream()
        .collect(Collectors.toList());
    AuthenticationSource source = duplexToken.getSource();

    ResponseEntity<?> response = null;
    String jwtToken = null;

    // Prepare dev user in dev mode
    if ("local".equals(profile) && ("admin".equals(userName) || "admin@admin.com"
        .equals(userName))) {
      User user = new User();
      user.setUserID(1l);
      user.setUserName("admin");
      user.setUserSurname("admin");

      UserAuthentication userAuthentication = new UserAuthentication();
      userAuthentication.setUserEmail("admin@admin.com");
      userAuthentication.setUserName("admin");

      String tokenUserName = jwtTokenUtil.createJwtTokenUsername(user.getUserName());
      jwtToken = jwtTokenUtil.generateToken(tokenUserName, AuthenticationSource.User, authorities);

      response = ResponseEntity.ok(user2JwtResponse(user, userAuthentication, jwtToken));
    }
    // Prepare user authentication token
    else if (source.equals(AuthenticationSource.User)) {
      List<UserAuthentication> userAuthentications = userAuthenticationRepository
          .findByUserName(userName);
      UserAuthentication userAuthentication = userAuthentications.stream()
          .findFirst().orElse(null);

      if (userAuthentication != null) {
        String tokenUserName = jwtTokenUtil
            .createJwtTokenUsername(userAuthentication.getUserName());

        Long userID = userAuthentication.getUserID();
        User user = userRepository.findByUserID(userID);

        jwtToken = jwtTokenUtil.generateToken(tokenUserName, AuthenticationSource.User
            , authorities);

        response = ResponseEntity.ok(user2JwtResponse(user, userAuthentication, jwtToken));
      }
    }

    // Use cookies to store JWT Tokens if request ask for it
    if (authenticationRequest.getUseCookie() != null && authenticationRequest.getUseCookie()) {

      // String cookieDomain = StringUtils.isEmpty(authenticationRequest.getDomain()) ? this.defaultDomain:authenticationRequest.getDomain();
      String rootDomain = httpServletRequest.getServerName().replaceAll(".*\\.(?=.*\\.)", "");

      // int expireInSeconds = 60*60*24*100;
      CookieUtil.create(httpServletResponse, JwtTokenUtil.jwtTokenCookieName, jwtToken, false,
          tokenExpirationInSecond, rootDomain);
    }

    return response;
  }

  /**
   * Let JWTRequestFilter process set the User to Spring security context. Convert active user to
   * JWTResponse
   */
  @ApiOperation(value = "Get user's JWTResponse using JWT Token if exist.")
  @RequestMapping(value = "/jwt-token-check", method = RequestMethod.GET)
  public ResponseEntity<?> authenticationTokenCheck(HttpServletRequest request,
      HttpServletResponse response, @AuthenticationPrincipal AuthUser activeUser) {

    String jwtToken = null;
    String requestTokenHeader = request.getHeader("Authorization");

    //Search for JWT token in cookie if not found in header
    if (requestTokenHeader == null || requestTokenHeader.equals("")) {
      Cookie[] cookies = request.getCookies();
      if (cookies != null) {
        for (Cookie cookie : cookies) {
          if (cookie.getName().equals(JwtTokenUtil.jwtTokenCookieName)) {
            jwtToken = cookie.getValue();
            break;
          }
        }
      }
    } else {
      jwtToken = requestTokenHeader.substring(7);
    }

    if (UserType.USER.equals(activeUser.getType())) {
      User user = userRepository.findByUserID(activeUser.getUserId());

      List<UserAuthentication> userAuthentications = userAuthenticationRepository
          .findByUserID(user.getUserID());

      if (user != null && !CollectionUtils.isEmpty(userAuthentications)) {
        UserAuthentication userAuthentication = userAuthentications.get(0);

        return ResponseEntity.ok(user2JwtResponse(user, userAuthentication, jwtToken));
      }
    }

    //Clear the invalid JWT token from client's cookie
    log.info("JWT Token has expired or invalid, clearing it from client's browser");
    String rootDomain = request.getServerName().replaceAll(".*\\.(?=.*\\.)", "");
    CookieUtil.clear(response, JwtTokenUtil.jwtTokenCookieName, rootDomain);

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
  }

  /**
   * Prepare JWT response for User
   */
  private JwtResponse user2JwtResponse(User user, UserAuthentication userAuthentication,
      String jwtToken) {

    return JwtResponse.builder()
        .jwttoken(jwtToken)
        .userName(userAuthentication.getUserName())
        .email(userAuthentication.getUserEmail())
        .firstName(user.getUserName())
        .lastName(user.getUserSurname())
        .userID(user.getUserID()).build();
  }

  private Authentication authenticate(String email, String password) throws Exception {
    try {
      // This calls the authenticate method in GaaAuthenticationProvider
      // return authenticationManager.authenticate(new
      // UsernamePasswordAuthenticationToken(email, password));
      DuplexAuthenticationToken authToken = new DuplexAuthenticationToken(email, password,
          AuthenticationSource.User);
      return authenticationManager.authenticate(authToken);
    } catch (DisabledException e) {
      throw new Exception("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
      throw new Exception("INVALID_CREDENTIALS", e);
    }
  }
}