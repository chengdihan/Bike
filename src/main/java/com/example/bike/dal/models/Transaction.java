package com.example.bike.dal.models;

import com.example.bike.dal.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "transaction")
public class Transaction extends Auditable<String> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Basic
  @Column(name = "unlock_code", length = 50)
  private String unlockCode;

  @Basic
  @Column(name = "bike_id", nullable = false)
  private Integer bikeId;

  @Basic
  @Column(name = "member_id", nullable = false)
  private Integer memberId;

  @Basic
  @Column(name = "time_check_out")
  private LocalDateTime timeCheckOut;

  @Basic
  @Column(name = "time_return")
  private LocalDateTime timeReturn;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bike_id", insertable = false, updatable = false)
  @JsonIgnoreProperties(value = {"transactions"}, allowSetters = true)
  private Bike bike;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", insertable = false, updatable = false)
  @JsonIgnoreProperties(value = {"transactions"}, allowSetters = true)
  private Member member;

}
