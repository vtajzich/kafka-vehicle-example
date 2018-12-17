package com.kafka.vehicle.distance.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TraveledDistance {
    
    @Id
    private String id;

    private String vehicle;

    private double distance;

    public TraveledDistance() {
    }

    public TraveledDistance(final String id, final String vehicle, final double distance) {
        this.id = id;
        this.vehicle = vehicle;
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(final String vehicle) {
        this.vehicle = vehicle;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(final double distance) {
        this.distance = distance;
    }
}