package com.example.bike.config;

import com.example.bike.service.JwtUserDetailsService;
import com.example.bike.utils.CookieUtil;
import com.example.bike.utils.JwtTokenUtil;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  private final JwtUserDetailsService jwtUserDetailsService;

  private final JwtTokenUtil jwtTokenUtil;

  public JwtRequestFilter(
      JwtUserDetailsService jwtUserDetailsService, JwtTokenUtil jwtTokenUtil) {
    this.jwtUserDetailsService = jwtUserDetailsService;
    this.jwtTokenUtil = jwtTokenUtil;
  }


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    String path = request.getRequestURI();

    log.info("JWT Filter for Path: {}", path);
    if(path.startsWith("/public") || path.startsWith("/authenticate")){
      log.info("Skipping JWT filter if path is public {}", path);
      chain.doFilter(request, response);
      return;
    }

    String requestTokenHeader = request.getHeader("Authorization");

    //Search for JWT token in cookie if not found in header
    if(requestTokenHeader == null || requestTokenHeader.equals("")){
      Cookie[] cookies = request.getCookies();
      if(cookies != null){
        for(Cookie cookie: cookies){
          if(cookie.getName().equals(JwtTokenUtil.jwtTokenCookieName)){
            requestTokenHeader = "Bearer "+cookie.getValue();
          }
        }
      }
    }

    log.info("requestTokenHeader is " + requestTokenHeader);

    String userName = null;
    String jwtToken = null;
    // JWT Token is in the form "Bearer token". Remove Bearer word and get
    // only the Token
    if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
      jwtToken = requestTokenHeader.substring(7);
      try {
        userName = jwtTokenUtil.getUsernameFromToken(jwtToken);
      } catch (Exception e) {
        log.error("Unable to get JWT Token", e);

        if(!StringUtils.isEmpty(jwtToken)){
          log.info("Clearing unknown JWT Token from client's browser");
          clearCookie(request, response);
        }
      }
    } else {
      logger.warn("JWT Token does not begin with Bearer String");
    }

    // Once we get the token validate it.
    if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(userName);

      // if token is valid configure Spring Security to manually set
      // authentication
      if (userDetails != null && jwtTokenUtil.validateToken(jwtToken, userDetails)) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());

        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // After setting the Authentication in the context, we specify
        // that the current user is authenticated. So it passes the
        // Spring Security Configurations successfully.
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      }
      else{
        //Clear existing cookie from the response
        log.info("JWT Token has expired or invalid, clearing it from client's browser");
        clearCookie(request, response);
      }
    }

    chain.doFilter(request, response);
  }


  private void clearCookie(HttpServletRequest request, HttpServletResponse response){
    String rootDomain = request.getServerName().replaceAll(".*\\.(?=.*\\.)", "");
    CookieUtil.clear(response, JwtTokenUtil.jwtTokenCookieName, rootDomain);
  }
}