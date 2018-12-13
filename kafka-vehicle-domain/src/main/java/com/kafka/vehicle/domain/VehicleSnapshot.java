package com.kafka.vehicle.domain;

public interface VehicleSnapshot {

    static VehicleSnapshot of(PositionUpdate position, Vehicle vehicle) {
        return new DefaultVehicleSnapshot(position, vehicle);
    }

    PositionUpdate getPosition();
    
    Vehicle getVehicle();

    VehicleSnapshot merge(VehicleSnapshot previousPosition);
}
