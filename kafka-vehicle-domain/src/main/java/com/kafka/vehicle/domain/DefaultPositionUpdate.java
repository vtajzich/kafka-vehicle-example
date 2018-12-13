package com.kafka.vehicle.domain;

class DefaultPositionUpdate implements PositionUpdate {

    private final String id;
    private final Position position;

    public DefaultPositionUpdate(final String id, final Position position) {
        this.id = id;
        this.position = position;
    }

    @Override
    public String getVehicleId() {
        return id;
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
