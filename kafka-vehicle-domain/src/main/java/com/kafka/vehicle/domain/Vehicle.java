package com.kafka.vehicle.domain;

public interface Vehicle {
    
    static Vehicle of(String name, Metadata metadata) {
        return new DefaultVehicle(name, metadata);
    }

    String getId();

    String getName();

    Metadata getMetadata();
}
