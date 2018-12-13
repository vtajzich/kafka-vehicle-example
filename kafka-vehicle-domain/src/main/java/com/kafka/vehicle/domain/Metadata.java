package com.kafka.vehicle.domain;

public interface Metadata {

    static Metadata of(int count) {
        return new DefaultMetadata(count);
    }
    
    int getPassengerCount();
}
