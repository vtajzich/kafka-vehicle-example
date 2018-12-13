package com.kafka.vehicle.metadata.generator;

import com.kafka.vehicle.domain.PositionUpdate;
import com.kafka.vehicle.domain.Topic;
import com.kafka.vehicle.domain.Vehicle;
import com.kafka.vehicle.domain.VehicleSnapshot;
import com.kafka.vehicle.kafka.Builder;
import com.kafka.vehicle.kafka.ShutdownHook;
import com.kafka.vehicle.kafka.serdes.JsonSerde;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Serialized;

import java.util.Random;

public class VehicleIngestionApp {

    public static void main(String[] args) {

        String ip = args[0];

        Producer<String, VehicleSnapshot> producer = Builder.producerWithJsonSerializer(Topic.VEHICLE_SNAPSHOT.getValue(), ip + ":9092");

        final StreamsBuilder builder = new StreamsBuilder();

        Thread generator = new Thread(() -> {

            Serde<Vehicle> vehicleSerde = JsonSerde.of(Vehicle.class);
            Serde<PositionUpdate> positionUpdateSerde = JsonSerde.of(PositionUpdate.class);
            Serde<VehicleSnapshot> vehicleSnapshotSerde = JsonSerde.of(VehicleSnapshot.class);
            VehicleStatusMerger vehicleStatusMerger = new VehicleStatusMerger();

            KTable<String, Vehicle> newVehiclesKTable = builder.table("vehicle-new", Consumed.with(Serdes.String(), vehicleSerde));

            newVehiclesKTable.toStream()
                             .foreach((key, value) -> System.out.println("Received new vehicle: " + value));

            builder.stream("vehicle-update", Consumed.with(Serdes.String(), positionUpdateSerde))
                   .leftJoin(newVehiclesKTable, (update, vehicle) -> vehicleStatusMerger.apply(vehicle, update), Joined.with(Serdes.String(), positionUpdateSerde, vehicleSerde))
                   .groupByKey(Serialized.with(Serdes.String(), vehicleSnapshotSerde))
                   .reduce((previousPosition, currentPosition) -> currentPosition.merge(previousPosition))
                   .toStream()
                   .foreach((key, value) -> System.out.println("Vehicle received update: (" + value.getVehicle().getName() + " -> " + value.getPosition().getPosition().getX() + "," + value.getPosition().getPosition().getY() + " updates)"));

        });

        ShutdownHook.of(() -> generator.start()).await();
    }
    
}
