package com.kafka.vehicle.distance;

import com.kafka.vehicle.domain.Position;
import org.springframework.stereotype.Service;

@Service
public class DefaultDistanceUpdateService implements DistanceUpdateService {
    
    @Override
    public void updateDistance(final String key, final String vehicleBrand, final Position position) {
        System.out.println("======Updating distance======");
    }
}
