package com.kafka.vehicle.domain;

public class DefaultTraveledDistance implements TraveledDistance {

    private final String id;
    private final String name;
    private final double distance;
    private final Position lastPosition;

    public DefaultTraveledDistance(final String id, final String name, final double distance, final Position lastPosition) {
        this.id = id;
        this.name = name;
        this.distance = distance;
        this.lastPosition = lastPosition;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getDistance() {
        return distance;
    }

    @Override
    public Position getLastPosition() {
        return lastPosition;
    }
}
