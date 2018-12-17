package com.kafka.vehicle.domain;

public enum Topic {
    
    VEHICLE_NEW("vehicle-new"),
    VEHICLE_POSITION_UPDATE("vehicle-position-update"),
    VEHICLE_SNAPSHOT("vehicle-snapshot"),
    TRANSPORTED_PASSENGER("trasported-passenger");

    private final String value;

    Topic(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
