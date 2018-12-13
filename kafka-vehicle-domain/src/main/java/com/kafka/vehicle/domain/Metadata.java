package com.kafka.vehicle.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface Metadata {

    @JsonCreator
    static Metadata of(@JsonProperty("passengerCount") int count) {
        return new DefaultMetadata(count);
    }
    
    int getPassengerCount();
}
