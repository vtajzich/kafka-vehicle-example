package com.kafka.vehicle.passengers.repository;

import com.kafka.vehicle.passengers.domain.TransportedPassenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportedPassengerRepository extends JpaRepository<TransportedPassenger, String> {
}
