package com.kafka.vehicle.domain;

class DefaultMetadata implements Metadata {

    private final String make;
    private final int yearOfMake;
    private final int passengerCount;

    public DefaultMetadata(String make, int yearOfMake, int passengerCount) {
        this.make = make;
        this.yearOfMake = yearOfMake;
        this.passengerCount = passengerCount;
    }

    @Override
    public String getMake() {
        return make;
    }

    @Override
    public int getYearOfMake() {
        return yearOfMake;
    }

    @Override
    public int getPassengerCount() {
        return passengerCount;
    }
}
