package com.example.bike.api.request;

import java.io.Serializable;
import lombok.Data;

@Data
public class JwtRequest implements Serializable {

  private static final long serialVersionUID = 5926468583005150707L;

  private String userName;
  private String password;
  private String googleAuthToken;
  private Boolean useCookie;

  public JwtRequest() {
  }

  public JwtRequest(String username, String password) {
    this.setUserName(username);
    this.setPassword(password);
  }
}