package com.kafka.vehicle.domain;

public interface VehicleModel {

    static VehicleModel of(int year, String make, String name) {
        return new DefaultVehicleModel(year, make, name);
    }
    
    int getYear();

    String getMake();

    String getName();
}
