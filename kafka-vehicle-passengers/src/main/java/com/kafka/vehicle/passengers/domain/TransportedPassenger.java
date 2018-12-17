package com.kafka.vehicle.passengers.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TransportedPassenger {

    @Id
    private String make;
    private int yearOfMake;
    private long totalPassengersTransported;

    public TransportedPassenger() {
    }

    public TransportedPassenger(String make, int yearOfMake, long totalPassengersTransported) {
        this.make = make;
        this.yearOfMake = yearOfMake;
        this.totalPassengersTransported = totalPassengersTransported;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public int getYearOfMake() {
        return yearOfMake;
    }

    public void setYearOfMake(int yearOfMake) {
        this.yearOfMake = yearOfMake;
    }

    public long getTotalPassengersTransported() {
        return totalPassengersTransported;
    }

    public void setTotalPassengersTransported(long totalPassengersTransported) {
        this.totalPassengersTransported = totalPassengersTransported;
    }
}
