package com.kafka.vehicle.domain;

public interface PositionUpdate {

    static PositionUpdate of(String id, Position position) {
        return new DefaultPositionUpdate(id, position);
    }
    
    String getVehicleId();

    Position getPosition();
}
