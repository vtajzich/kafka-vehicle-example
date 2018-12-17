package com.kafka.vehicle.distance;

import com.kafka.vehicle.distance.domain.TraveledDistance;
import com.kafka.vehicle.distance.repository.TraveledDistanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class DefaultDistanceUpdateService implements DistanceUpdateService {
    
    @Autowired
    TraveledDistanceRepository traveledDistanceRepository;
    
    @Override
    public void updateDistance(final String key, final String vehicle, final double distance) {
        System.out.println("======Updating distance======");
        System.out.println(MessageFormat.format("id: {0}, vehicle: {1}, updated distance: {2}", key, vehicle, distance));
        
        traveledDistanceRepository.save(new TraveledDistance(key, vehicle, distance));
    }
}
