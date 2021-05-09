package com.example.bike.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.WebUtils;

@Slf4j
public class CookieUtil {

  public static void create(HttpServletResponse httpServletResponse, String name, String value,
      Boolean secure, Integer maxAgeInSeconds, String domain) {
    Cookie cookie = new Cookie(name, value);
    cookie.setSecure(secure);
    cookie.setHttpOnly(true);
    cookie.setMaxAge(maxAgeInSeconds);
    cookie.setDomain(domain);
    cookie.setPath("/");
    httpServletResponse.addCookie(cookie);
  }

  public static void clear(HttpServletResponse httpServletResponse, String name, String domain) {
    Cookie cookie = new Cookie(name, null);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge(0);
    cookie.setDomain(domain);
    httpServletResponse.addCookie(cookie);
  }

  public static String getValue(HttpServletRequest httpServletRequest, String name) {
    Cookie cookie = WebUtils.getCookie(httpServletRequest, name);
    return cookie != null ? cookie.getValue() : null;
  }
}