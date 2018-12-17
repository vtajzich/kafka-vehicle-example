package com.kafka.vehicle.distance.repository;

import com.kafka.vehicle.distance.domain.TraveledDistance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraveledDistanceRepository extends JpaRepository<TraveledDistance, String> {
}
