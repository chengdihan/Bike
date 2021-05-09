package com.example.bike.dal.enums;

public enum BikeStatus {
  FREE("FREE"), PENDING("PENDING"), INUSE("IN-USE"), DISABLED("DISABLED");

  private final String name;

  BikeStatus(String channel) {
    this.name = channel;
  }

  public String getName() {
    return name;
  }
}
