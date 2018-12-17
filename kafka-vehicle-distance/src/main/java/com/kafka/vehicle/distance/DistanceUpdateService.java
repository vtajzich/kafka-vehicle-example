package com.kafka.vehicle.distance;

import com.kafka.vehicle.domain.Position;

public interface DistanceUpdateService {

    void updateDistance(String key, String vehicleBrand, Position position);
}
