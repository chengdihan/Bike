package com.example.bike.dal.models;

import com.example.bike.dal.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
@Table(name = "docker")
public class Docker extends Auditable<String> implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Basic
  @Column(name = "station_id", length = 50)
  private Integer stationId;

  @Basic
  @Column(name = "status")
  private String status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "station_id", insertable = false, updatable = false)
  @JsonIgnoreProperties(value = {"dockers"}, allowSetters = true)
  private Station station;

  @OneToOne(mappedBy = "docker")
  private Bike bike;
}
