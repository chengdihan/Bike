package com.example.bike.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BikeListItem {

    long id;

    Integer station_id;

    Integer docker_id;

    String status;
}
