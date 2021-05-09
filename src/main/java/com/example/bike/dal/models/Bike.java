package com.example.bike.dal.models;

import com.example.bike.dal.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "bike")
public class Bike extends Auditable<String> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Basic
  @Column(name = "status", nullable = false, length = 50)
  private String status;

  @Basic
  @Column(name = "station_id")
  private Integer stationId;

  @Basic
  @Column(name = "docker_id")
  private Integer dockerId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "station_id", insertable = false, updatable = false)
  @JsonIgnoreProperties(value = {"bikes"}, allowSetters = true)
  private Station station;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "docker_id", insertable = false, updatable = false)
  @JsonIgnoreProperties(value = {"bike"}, allowSetters = true)
  private Docker docker;

  @OneToMany(
      mappedBy = "bike",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  @OrderBy("id")
  @JsonIgnoreProperties(value = {"bikes"}, allowSetters = true)
  private List<Transaction> transactions = Lists.newArrayList();

}
