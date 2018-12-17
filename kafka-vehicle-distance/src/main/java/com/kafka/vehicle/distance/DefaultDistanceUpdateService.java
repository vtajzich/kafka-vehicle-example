package com.kafka.vehicle.distance;

import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class DefaultDistanceUpdateService implements DistanceUpdateService {
    
    @Override
    public void updateDistance(final String key, final String vehicleBrand, final double distance) {
        System.out.println("======Updating distance======");
        System.out.println(MessageFormat.format("id: {0}, vehicle: {1}, updated distance: {2}", key, vehicleBrand, distance));
    }
}
