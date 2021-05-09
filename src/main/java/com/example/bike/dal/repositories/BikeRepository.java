package com.example.bike.dal.repositories;

import com.example.bike.dal.models.Bike;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BikeRepository extends JpaRepository<Bike, Integer> {
  List<Bike> findByStationIdAndStatusOrderById(Integer stationId, String status);
}
