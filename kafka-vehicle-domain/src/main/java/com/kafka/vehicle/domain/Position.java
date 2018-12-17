package com.kafka.vehicle.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface Position {

    @JsonCreator
    static Position of(@JsonProperty("x") int x, @JsonProperty("y") int y) {
        return new DefaultPosition(x, y);
    }

    int getX();

    int getY();

    @JsonIgnore
    default double distance(Position otherPosition) {
        int dx = otherPosition.getX() - this.getX();
        int dy = otherPosition.getY() - this.getY();

        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }
}
