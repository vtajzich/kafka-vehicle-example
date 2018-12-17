package com.kafka.vehicle.passengers;

import com.kafka.vehicle.domain.Position;

public interface PassengersUpdateService {

    void updatePassengers(String key, String vehicleBrand, Position position);
}
