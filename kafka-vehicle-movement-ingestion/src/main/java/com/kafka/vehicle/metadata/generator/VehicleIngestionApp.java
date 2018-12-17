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
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Serialized;

import java.util.concurrent.Future;

public class VehicleIngestionApp {

    private static final String PRODUCER_ID = "vehicle-movement-ingestion";
    private static final String CONSUMER_ID = "vehicle-movement-ingestion-consumer";

    final String bootstrapServers = "localhost:9092";

    private Producer<String, VehicleSnapshot> producer = Builder.producerWithJsonSerializer(PRODUCER_ID, bootstrapServers);

    Serde<Vehicle> vehicleSerde = JsonSerde.of(Vehicle.class);
    Serde<PositionUpdate> positionUpdateSerde = JsonSerde.of(PositionUpdate.class);
    Serde<VehicleSnapshot> vehicleSnapshotSerde = JsonSerde.of(VehicleSnapshot.class);
    VehicleStatusMerger vehicleStatusMerger = new VehicleStatusMerger();
    
    public static void main(String[] args) {

        var vehicleIngestionApp = new VehicleIngestionApp();
        vehicleIngestionApp.run();
    }
    
    public void run() {

        final StreamsBuilder builder = new StreamsBuilder();

        KTable<String, Vehicle> newVehiclesKTable = builder.table(Topic.VEHICLE_NEW.getValue(), Consumed.with(Serdes.String(), vehicleSerde));

        builder.stream(Topic.VEHICLE_POSITION_UPDATE.getValue(), Consumed.with(Serdes.String(), positionUpdateSerde))
                .leftJoin(newVehiclesKTable, this::joinVehicleAndUpdate, Joined.with(Serdes.String(), positionUpdateSerde, vehicleSerde))
                .groupByKey(Serialized.with(Serdes.String(), vehicleSnapshotSerde))
                .reduce(this::mergePosition)
                .toStream()
                .peek((key, value) -> System.out.println("key: " + key + ", value: " + value))
                .foreach(this::produceVehicleSnapshot);


        final Topology topology = builder.build();

        final KafkaStreams streams = new KafkaStreams(topology, Builder.consumerProps(CONSUMER_ID, bootstrapServers));
        streams.cleanUp();

        ShutdownHook.of(() -> streams.start(), () -> streams.close()).await();
    }

    private VehicleSnapshot joinVehicleAndUpdate(PositionUpdate update, Vehicle vehicle) {
        return vehicleStatusMerger.apply(vehicle, update);
    }

    private VehicleSnapshot mergePosition(VehicleSnapshot previousPosition, VehicleSnapshot currentPosition) {
        return currentPosition.merge(previousPosition);
    }

    private Future<RecordMetadata> produceVehicleSnapshot(String key, VehicleSnapshot value) {
        return producer.send(new ProducerRecord<>(Topic.VEHICLE_SNAPSHOT.getValue(), key, value));
    }

}
