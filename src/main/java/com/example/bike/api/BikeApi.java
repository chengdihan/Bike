package com.example.bike.api;

import com.example.bike.api.request.AddBikeRequest;
import com.example.bike.api.request.AddDockerRequest;
import com.example.bike.api.request.AddMemberRequest;
import com.example.bike.api.request.AddStationRequest;
import com.example.bike.api.response.GetBikeResponse;
import com.example.bike.api.response.GetMemberResponse;
import com.example.bike.api.response.GetStationResponse;
import com.example.bike.dal.models.Bike;
import com.example.bike.dal.models.Docker;
import com.example.bike.dal.models.Member;
import com.example.bike.dal.models.Station;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "BikeApi", description = "Example API for citibike in a SQL database.")
@RequestMapping("/v1/examples/bike")
public interface BikeApi {

  @Operation(summary = "Get the list of members that have been added to the system.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "A list of members available in the system"),
  })
  @GetMapping(value = "member")
  ResponseEntity<GetMemberResponse> getMembers();

  @Operation(summary = "Get the list of stations that have been added to the system.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "A list of stations available in the system"),
  })
  @GetMapping(value = "station")
  ResponseEntity<GetStationResponse> getStations();

  @Operation(summary = "Get the list of bikes that have been added to the system.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "A list of bikes available in the system"),
  })
  @GetMapping(value = "bike")
  ResponseEntity<GetBikeResponse> getBike();

  @Operation(summary = "Add a new station to the system")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "The station with the assigned id.")
  })
  @PostMapping(value = "/station")
  ResponseEntity<Station> addStation(@Valid @RequestBody AddStationRequest req);

  @Operation(summary = "Add a new docker to the system")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "The docker with the assigned id.")
  })
  @PostMapping(value = "/docker")
  ResponseEntity<Docker> addDocker(@Valid @RequestBody AddDockerRequest req);

  @Operation(summary = "Add a new bike to the system")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "The bike with the assigned id.")
  })
  @PostMapping(value = "/bike")
  ResponseEntity<Bike> addBike(@Valid @RequestBody AddBikeRequest req);

  @Operation(summary = "Add a new member to the system")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "The member with the assigned id.")
  })
  @PostMapping(value = "/member")
  ResponseEntity<Member> addMember(@Valid @RequestBody AddMemberRequest req);

}
