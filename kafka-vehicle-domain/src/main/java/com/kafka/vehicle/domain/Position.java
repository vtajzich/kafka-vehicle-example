package com.kafka.vehicle.domain;

public interface Position {

    static Position of(int x, int y) {
        return new DefaultPosition(x, y);
    }
    
    int getX();

    int getY();
}
