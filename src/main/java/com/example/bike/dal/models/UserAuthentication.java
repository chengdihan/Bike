package com.example.bike.dal.models;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "userauthentication")
public class UserAuthentication implements java.io.Serializable {

  private static final long serialVersionUID = 4585180959452712813L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "user_nameID", unique = true, nullable = false)
  private Long userNameID;

  @Column(name = "userID", length = 40)
  private Long userID;

  @Column(name = "user_name")
  private String userName;

  @Column(name = "user_email")
  private String userEmail;

  @Column(name = "user_password")
  private String userPassword;

  @Column(name = "user_emailverified")
  private Boolean useEMailVerified;

  @Column(name = "user_registrationdate")
  private LocalDate userRegistrationdate;

  @Column(name = "user_verificationcode", length = 40)
  private String userVerificationcode;

}
