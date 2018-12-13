package com.kafka.vehicle.metadata.generator;

import com.kafka.vehicle.domain.PositionUpdate;
import com.kafka.vehicle.domain.Vehicle;
import com.kafka.vehicle.domain.VehicleSnapshot;
import org.apache.kafka.streams.kstream.ValueJoiner;

public class VehicleStatusMerger implements ValueJoiner<Vehicle, PositionUpdate, VehicleSnapshot> {

    @Override
    public VehicleSnapshot apply(final Vehicle vehicle, final PositionUpdate positionUpdate) {
        return VehicleSnapshot.of(positionUpdate, vehicle);
    }

}
