package com.kafka.vehicle.domain;

class DefaultMetadata implements Metadata {

    private final int passengerCount;

    public DefaultMetadata(int passengerCount) {
        this.passengerCount = passengerCount;
    }

    @Override
    public int getPassengerCount() {
        return passengerCount;
    }
}
