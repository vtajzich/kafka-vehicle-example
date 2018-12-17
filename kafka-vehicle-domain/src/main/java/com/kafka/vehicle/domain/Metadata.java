package com.kafka.vehicle.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface Metadata {

    @JsonCreator
    static Metadata of(@JsonProperty("make") String make, @JsonProperty("yearOfMake") int yearOfMake, @JsonProperty("passengerCount") int count) {
        return new DefaultMetadata(make, yearOfMake, count);
    }

    String getMake();

    int getYearOfMake();
    
    int getPassengerCount();
}
