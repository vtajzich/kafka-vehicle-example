package com.kafka.vehicle.metadata.generator;

import com.kafka.vehicle.domain.PositionUpdate;
import com.kafka.vehicle.domain.Topic;
import com.kafka.vehicle.domain.Vehicle;
import com.kafka.vehicle.domain.VehicleSnapshot;
import com.kafka.vehicle.kafka.Builder;
import com.kafka.vehicle.kafka.ShutdownHook;
import com.kafka.vehicle.kafka.serdes.JsonSerde;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Serialized;

public class VehicleIngestionApp {

    private static final String PRODUCER_ID = "vehicle-movement-ingestion";
    private static final String CONSUMER_ID = "vehicle-movement-ingestion-consumer";

    public static void main(String[] args) {

        final String bootstrapServers = "localhost:9092";

        Producer<String, VehicleSnapshot> producer = Builder.producerWithJsonSerializer(PRODUCER_ID, bootstrapServers);

        final StreamsBuilder builder = new StreamsBuilder();

        Serde<Vehicle> vehicleSerde = JsonSerde.of(Vehicle.class);
        Serde<PositionUpdate> positionUpdateSerde = JsonSerde.of(PositionUpdate.class);
        Serde<VehicleSnapshot> vehicleSnapshotSerde = JsonSerde.of(VehicleSnapshot.class);
        VehicleStatusMerger vehicleStatusMerger = new VehicleStatusMerger();

        KTable<String, Vehicle> newVehiclesKTable = builder.table(Topic.VEHICLE_NEW.getValue(), Consumed.with(Serdes.String(), vehicleSerde));

        //            newVehiclesKTable.toStream()
        //                             .foreach((key, value) -> System.out.println("Received new vehicle: " + value));

        builder.stream(Topic.VEHICLE_POSITION_UPDATE.getValue(), Consumed.with(Serdes.String(), positionUpdateSerde))
               .peek((key, value) -> System.out.println("key: " + key + ", value: " + value))
               .leftJoin(newVehiclesKTable, (update, vehicle) -> vehicleStatusMerger.apply(vehicle, update), Joined.with(Serdes.String(), positionUpdateSerde, vehicleSerde))
               .groupByKey(Serialized.with(Serdes.String(), vehicleSnapshotSerde))
               .reduce((previousPosition, currentPosition) -> currentPosition.merge(previousPosition))
               .toStream()
               .peek((key, value) -> System.out.println("key: " + key + ", value: " + value))
               .foreach(((key, value) -> producer.send(new ProducerRecord<>(Topic.VEHICLE_SNAPSHOT.getValue(), key, value))));
//               .foreach((key, value) -> System.out.println("Vehicle received update: (" + value.getVehicle().getName() + " -> " + value.getPosition().getPosition().getX() + "," + value.getPosition().getPosition().getY() + " updates)"));


        final Topology topology = builder.build();

        final KafkaStreams streams = new KafkaStreams(topology, Builder.consumerProps(CONSUMER_ID, bootstrapServers));
        streams.cleanUp();

        ShutdownHook.of(() -> streams.start(), () -> streams.close()).await();
    }

}
