package com.kafka.vehicle.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface Vehicle {
    
    static Vehicle of(String name, Metadata metadata) {
        return new DefaultVehicle(name, metadata);
    }
    
    @JsonCreator
    static Vehicle of(@JsonProperty("id") String id, @JsonProperty("name") String name, @JsonProperty("metadata") Metadata metadata) {
        return new DefaultVehicle(id, name, metadata);
    }

    String getId();

    String getName();

    Metadata getMetadata();
}
