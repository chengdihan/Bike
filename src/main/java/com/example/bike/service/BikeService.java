package com.example.bike.service;

import com.example.bike.api.request.AddBikeRequest;
import com.example.bike.api.request.AddDockerRequest;
import com.example.bike.api.request.AddMemberRequest;
import com.example.bike.api.request.AddStationRequest;
import com.example.bike.dal.models.Bike;
import com.example.bike.dal.models.Docker;
import com.example.bike.dal.models.Member;
import com.example.bike.dal.models.Station;
import com.example.bike.dal.repositories.BikeRepository;
import com.example.bike.dal.repositories.DockerRepository;
import com.example.bike.dal.repositories.MemberRepository;
import com.example.bike.dal.repositories.PaymentRepository;
import com.example.bike.dal.repositories.StationRepository;
import com.example.bike.dal.repositories.TransactionRepository;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BikeService {

  private final BikeRepository bikeRepository;
  private final MemberRepository memberRepository;
  private final TransactionRepository transactionRepository;
  private final StationRepository stationRepository;
  private final DockerRepository dockerRepository;
  private final PaymentRepository paymentRepository;

  public BikeService(
      BikeRepository bikeRepository,
      MemberRepository memberRepository,
      TransactionRepository transactionRepository,
      StationRepository stationRepository,
      DockerRepository dockerRepository,
      PaymentRepository paymentRepository) {
    this.bikeRepository = bikeRepository;
    this.memberRepository = memberRepository;
    this.transactionRepository = transactionRepository;
    this.stationRepository = stationRepository;
    this.dockerRepository = dockerRepository;
    this.paymentRepository = paymentRepository;
  }


  public Collection<Member> getMembers() {
    return memberRepository.findAll();

  }

  public Collection<Station> getStation() {
    return stationRepository.findAll();
  }

  public List<Bike> getBike() {
    return bikeRepository.findAll();

  }

  public Station addStation(AddStationRequest req) {
    return stationRepository.saveAndFlush(
        Station.builder()
            .name(req.getName())
            .location(req.getLocation())
            .build()
    );
  }

  public Docker addDocker(AddDockerRequest req) {
    return dockerRepository.saveAndFlush(
        Docker.builder()
            .stationId(req.getStation_id())
            .build()
    );
  }

  public Bike addBike(AddBikeRequest req) {
    return bikeRepository.saveAndFlush(
        Bike.builder()
            .stationId(req.getStation_id())
            .dockerId(req.getDocker_id())
            .status("FREE")
            .build()
    );

  }

  public Member addMember(AddMemberRequest req) {
    return memberRepository.saveAndFlush(
        Member.builder()
            .name(req.getName())
            .build()
    );
  }
}
