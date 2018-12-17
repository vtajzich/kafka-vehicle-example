package com.kafka.vehicle.distance;

import com.kafka.vehicle.domain.TraveledDistance;
import com.kafka.vehicle.domain.Vehicle;
import com.kafka.vehicle.domain.VehicleSnapshot;
import org.apache.kafka.streams.kstream.ValueJoiner;

public class DistanceAndVehicleJoiner implements ValueJoiner<TraveledDistance, VehicleSnapshot, TraveledDistance> {

    @Override
    public TraveledDistance apply(final TraveledDistance latestDistance, final VehicleSnapshot snapshotUpdate) {

        Vehicle vehicle = snapshotUpdate.getVehicle();

        if (latestDistance == null) {
            return TraveledDistance.of(vehicle.getId(), vehicle.getName(), 0d, snapshotUpdate.getPosition().getPosition());
        }
        System.out.println("   Latest distance was: " + latestDistance.getDistance());
        
        double traveled = latestDistance.getLastPosition().distance(snapshotUpdate.getPosition().getPosition());

        System.out.println("   Traveled: " + traveled);
        
        double newDistance = latestDistance.getDistance() + traveled;

        System.out.println("   Next distance is: " + newDistance);
        
        
        return TraveledDistance.of(latestDistance.getId(), latestDistance.getName(), newDistance, snapshotUpdate.getPosition().getPosition());
    }

}
