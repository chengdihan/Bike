package com.example.bike.dal.enums;

public enum DockerStatus {
  EMPTY("EMPTY"), OCCUPIED("OCCUPIED"), DISABLED("DISABLED");

  private final String name;

  DockerStatus(String channel) {
    this.name = channel;
  }

  public String getName() {
    return name;
  }
}
