package com.kafka.vehicle.domain;

class DefaultVehicleModel implements VehicleModel {

    private final int year;
    private final String make;
    private final String name;

    public DefaultVehicleModel(final int year, final String make, final String name) {
        this.year = year;
        this.make = make;
        this.name = name;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public String getMake() {
        return make;
    }

    @Override
    public String getName() {
        return name;
    }
}
