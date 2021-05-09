package com.example.bike.web;

import com.example.bike.api.BikeApi;
import com.example.bike.api.request.AddBikeRequest;
import com.example.bike.api.request.AddDockerRequest;
import com.example.bike.api.request.AddMemberRequest;
import com.example.bike.api.request.AddStationRequest;
import com.example.bike.api.response.BikeListItem;
import com.example.bike.api.response.GetBikeResponse;
import com.example.bike.api.response.GetMemberResponse;
import com.example.bike.api.response.GetStationResponse;
import com.example.bike.api.response.MemberListItem;
import com.example.bike.api.response.StationListItem;
import com.example.bike.dal.models.Bike;
import com.example.bike.dal.models.Docker;
import com.example.bike.dal.models.Member;
import com.example.bike.dal.models.Station;
import com.example.bike.service.BikeService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BikeController implements BikeApi {

  private BikeService bikeService;

  @Autowired
  public BikeController(
      BikeService bikeService) {
    this.bikeService = bikeService;
  }


  @Override
  public ResponseEntity<GetMemberResponse> getMembers() {
    List<MemberListItem> list = bikeService.getMembers().stream()
        .map(p -> MemberListItem.builder()
            .id(p.getId())
            .name(p.getName())
            .build()
        )
        .collect(Collectors.toList());

    return ResponseEntity.ok(
        GetMemberResponse.builder()
            .members(list)
            .build()
    );
  }

  @Override
  public ResponseEntity<GetBikeResponse> getBike() {

    List<BikeListItem> list = bikeService.getBike().stream()
        .map(p -> BikeListItem.builder()
            .id(p.getId())
            .status(p.getStatus())
            .station_id(p.getStationId())
            .docker_id(p.getDockerId())
            .build()
        )
        .collect(Collectors.toList());

    return ResponseEntity.ok(
        GetBikeResponse.builder()
            .bikes(list)
            .build()
    );
  }

  @Override
  public ResponseEntity<GetStationResponse> getStations() {
    List<StationListItem> list = bikeService.getStation().stream()
        .map(p -> StationListItem.builder()
            .id(p.getId())
            .name(p.getName())
            .location(p.getLocation())
            .build()
        )
        .collect(Collectors.toList());

    return ResponseEntity.ok(
        GetStationResponse.builder()
            .stations(list)
            .build()
    );
  }

  @Override
  public ResponseEntity<Station> addStation(AddStationRequest req) {
    var station = bikeService.addStation(req);
    return ResponseEntity.ok(station);
  }

  @Override
  public ResponseEntity<Docker> addDocker(AddDockerRequest req) {
    var docker = bikeService.addDocker(req);
    return ResponseEntity.ok(docker);
  }

  @Override
  public ResponseEntity<Bike> addBike(AddBikeRequest req) {
    var bike = bikeService.addBike(req);
    return ResponseEntity.ok(bike);
  }

  @Override
  public ResponseEntity<Member> addMember(AddMemberRequest req) {
    var member = bikeService.addMember(req);
    return ResponseEntity.ok(member);
  }
}
