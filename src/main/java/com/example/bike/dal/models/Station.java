package com.example.bike.dal.models;

import com.example.bike.dal.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import java.io.Serializable;
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
@Table(name = "station")
public class Station extends Auditable<String> implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Basic
  @Column(name = "name", length = 50)
  private String name;

  @Basic
  @Column(name = "location")
  private String location;

  @OneToMany(
      mappedBy = "station",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  @OrderBy("id")
  @JsonIgnoreProperties(value = {"station"}, allowSetters = true)
  private List<Bike> bikes = Lists.newArrayList();

  @OneToMany(
      mappedBy = "station",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  @OrderBy("id")
  @JsonIgnoreProperties(value = {"station"}, allowSetters = true)
  private List<Docker> dockers = Lists.newArrayList();

}
