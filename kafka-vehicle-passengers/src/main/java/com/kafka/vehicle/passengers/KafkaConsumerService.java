package com.kafka.vehicle.passengers;

import com.kafka.vehicle.domain.Metadata;
import com.kafka.vehicle.domain.Topic;
import com.kafka.vehicle.domain.VehicleSnapshot;
import com.kafka.vehicle.kafka.Builder;
import com.kafka.vehicle.kafka.ShutdownHook;
import com.kafka.vehicle.kafka.serdes.JsonSerde;
import com.kafka.vehicle.passengers.domain.TransportedPassenger;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService implements ApplicationListener<ContextRefreshedEvent> {

    private static final String CONSUMER_ID = "vehicle-distance-consumer";

    @Autowired
    private PassengersUpdateService passengersUpdateService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {
        final String bootstrapServers = "localhost:9092";


        final StreamsBuilder builder = new StreamsBuilder();

        Serde<VehicleSnapshot> vehicleSnapshotSerde = JsonSerde.of(VehicleSnapshot.class);

        builder.stream(Topic.VEHICLE_SNAPSHOT.getValue(), Consumed.with(Serdes.String(), vehicleSnapshotSerde))
                .peek((key, value) -> System.out.println("Vehicle snapshot update: " + key + ", value: " + value))
                .map(this::createTransportedPassenger)
                .foreach((key, value) -> passengersUpdateService.updatePassengers(value));

        final Topology topology = builder.build();

        final KafkaStreams streams = new KafkaStreams(topology, Builder.consumerProps(CONSUMER_ID, bootstrapServers));
        streams.cleanUp();

        ShutdownHook.of(() -> streams.start(), () -> streams.close()).await();
    }

    private KeyValue<String, TransportedPassenger> createTransportedPassenger(String key, VehicleSnapshot snapshot) {

        final Metadata metadata = snapshot.getVehicle().getMetadata();
        return KeyValue.pair(metadata.getMake(), new TransportedPassenger(metadata.getMake(), metadata.getYearOfMake(), metadata.getPassengerCount()));
    }
}