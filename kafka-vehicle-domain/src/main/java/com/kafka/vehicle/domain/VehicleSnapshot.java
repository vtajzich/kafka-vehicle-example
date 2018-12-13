package com.kafka.vehicle.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface VehicleSnapshot {

    @JsonCreator
    static VehicleSnapshot of(@JsonProperty("position") PositionUpdate position, @JsonProperty("vehicle") Vehicle vehicle) {
        return new DefaultVehicleSnapshot(position, vehicle);
    }

    PositionUpdate getPosition();
    
    Vehicle getVehicle();

    VehicleSnapshot merge(VehicleSnapshot previousPosition);
}
