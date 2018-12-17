package com.kafka.vehicle.passengers;

import com.kafka.vehicle.passengers.domain.TransportedPassenger;

public interface PassengersUpdateService {

    void updatePassengers(String key, TransportedPassenger transportedPassenger);
}
