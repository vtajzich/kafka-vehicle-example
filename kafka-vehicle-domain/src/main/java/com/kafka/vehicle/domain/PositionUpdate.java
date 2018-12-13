package com.kafka.vehicle.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface PositionUpdate {

    @JsonCreator
    static PositionUpdate of(@JsonProperty("id") String id, @JsonProperty("position") Position position) {
        return new DefaultPositionUpdate(id, position);
    }

    String getVehicleId();

    Position getPosition();
}
