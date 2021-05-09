package com.example.bike.dal.models;

import com.example.bike.dal.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member extends Auditable<String> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Basic
  @Column(name = "name", length = 50)
  private String name;

  @Basic
  @Column(name = "unlock_code", length = 50)
  private String unlockCode;

  @OneToMany(
      mappedBy = "member",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  @OrderBy("id")
  @JsonIgnoreProperties(value = {"member"}, allowSetters = true)
  private List<Transaction> transactions = Lists.newArrayList();

  @OneToMany(
      mappedBy = "member",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  @OrderBy("id")
  @JsonIgnoreProperties(value = {"member"}, allowSetters = true)
  private List<Payment> payments = Lists.newArrayList();


}
