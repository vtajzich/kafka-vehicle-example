package com.kafka.vehicle.passengers;

import com.kafka.vehicle.passengers.domain.TransportedPassenger;
import com.kafka.vehicle.passengers.repository.TransportedPassengerRepository;
import org.springframework.stereotype.Service;

@Service
public class DefaultPassengersUpdateService implements PassengersUpdateService {

    private final TransportedPassengerRepository repository;

    public DefaultPassengersUpdateService(TransportedPassengerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void updatePassengers(String key, TransportedPassenger transportedPassenger) {
        repository.save(transportedPassenger);
        System.out.println("Updated record: " + transportedPassenger.getMake() + ", " + transportedPassenger.getTotalPassengersTransported());
    }
}
