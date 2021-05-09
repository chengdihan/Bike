package com.example.bike.api.response;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse implements Serializable {

  private static final long serialVersionUID = -8091879091924046844L;
  String jwttoken;
  String email;
  String firstName;
  String lastName;
  String userName;
  Long userID;
}