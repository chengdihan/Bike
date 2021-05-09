package com.example.bike.dal.repositories;

import com.example.bike.dal.models.Docker;
import com.example.bike.dal.models.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DockerRepository extends JpaRepository<Docker, Integer> {
}
