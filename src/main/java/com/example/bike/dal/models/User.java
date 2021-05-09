package com.example.bike.dal.models;

import static javax.persistence.GenerationType.IDENTITY;

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
@Table(name = "user")
public class User implements java.io.Serializable {

  private static final long serialVersionUID = 4585180959452712813L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "userID", unique = true, nullable = false)
  private Long userID;

  @Column(name = "sponsorID", length = 40)
  private String sponsorID;

  @Column(name = "sourceID", length = 40)
  private String sourceID;

  @Column(name = "helpcenterID", length = 40)
  private String helpcenterID;

  @Column(name = "user_name")
  private String userName;

  @Column(name = "user_surname")
  private String userSurname;

  @Column(name = "usersexID", length = 40)
  private String usersexID;

}
