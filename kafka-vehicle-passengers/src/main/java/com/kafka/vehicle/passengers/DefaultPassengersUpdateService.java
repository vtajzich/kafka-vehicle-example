package com.kafka.vehicle.passengers;

import com.kafka.vehicle.domain.Position;
import com.kafka.vehicle.passengers.domain.TransportedPassenger;
import com.kafka.vehicle.passengers.repository.TransportedPassengerRepository;
import org.springframework.stereotype.Service;

@Service
public class DefaultPassengersUpdateService implements PassengersUpdateService {

    private final TransportedPassengerRepository repository;

    int counter = 0;

    public DefaultPassengersUpdateService(TransportedPassengerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void updatePassengers(final String key, final String vehicleBrand, final Position position) {
        System.out.println("======Updating passengers======");

        repository.save(new TransportedPassenger("ford", 2018, counter++));
    }
}
