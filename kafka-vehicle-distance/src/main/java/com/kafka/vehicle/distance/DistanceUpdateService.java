package com.kafka.vehicle.distance;

public interface DistanceUpdateService {

    void updateDistance(String key, String vehicle, double distance);
}
