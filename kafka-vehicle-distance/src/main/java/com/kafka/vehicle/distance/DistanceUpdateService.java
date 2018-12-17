package com.kafka.vehicle.distance;

public interface DistanceUpdateService {

    void updateDistance(String key, String vehicleBrand, double distance);
}
