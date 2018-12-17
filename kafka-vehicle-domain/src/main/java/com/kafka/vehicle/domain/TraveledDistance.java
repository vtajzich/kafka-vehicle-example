package com.kafka.vehicle.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface TraveledDistance {

    @JsonCreator
    static TraveledDistance of(@JsonProperty("id") String id, 
                               @JsonProperty("name") String name, 
                               @JsonProperty("distance") double distance, 
                               @JsonProperty("lastPosition") Position lastPosition) {
        return new DefaultTraveledDistance(id, name, distance, lastPosition);
    }

    String getId();

    String getName();

    double getDistance();

    Position getLastPosition();
    
    
}
