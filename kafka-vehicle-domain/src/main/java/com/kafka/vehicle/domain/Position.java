package com.kafka.vehicle.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface Position {

    @JsonCreator
    static Position of(@JsonProperty("x") int x, @JsonProperty("y") int y) {
        return new DefaultPosition(x, y);
    }
    
    int getX();

    int getY();
}
