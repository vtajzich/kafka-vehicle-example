package com.kafka.vehicle.domain;

public class DefaultVehicleSnapshot implements VehicleSnapshot {

    public DefaultVehicleSnapshot(final PositionUpdate position, final Vehicle vehicle) {
        this.positionUpdate = position;
        this.vehicle = vehicle;
    }

    private PositionUpdate positionUpdate;
    private Vehicle vehicle;

    @Override
    public PositionUpdate getPosition() {
        return positionUpdate;
    }

    @Override
    public Vehicle getVehicle() {
        return vehicle;
    }

    @Override
    public VehicleSnapshot merge(final VehicleSnapshot previousPosition) {
        return new DefaultVehicleSnapshot(previousPosition.getPosition(), this.vehicle);
    }
}
