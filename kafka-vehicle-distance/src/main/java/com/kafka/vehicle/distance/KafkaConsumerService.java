package com.kafka.vehicle.distance;

import com.kafka.vehicle.domain.Topic;
import com.kafka.vehicle.domain.TraveledDistance;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService implements ApplicationListener<ContextRefreshedEvent> {

    private static final String CONSUMER_ID = "vehicle-distance-consumer";
    private static final String PRODUCER_ID = "vehicle-distance-producer";

    @Autowired
    private DistanceUpdateService distanceUpdateService;

    final String bootstrapServers = "localhost:9092";

    DistanceAndVehicleJoiner distanceVehicleJoiner = new DistanceAndVehicleJoiner();
    
    Producer<String, TraveledDistance> producer = Builder.producerWithJsonSerializer(PRODUCER_ID, bootstrapServers);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {

        final StreamsBuilder builder = new StreamsBuilder();
        
        Serde<VehicleSnapshot> vehicleSnapshotSerde = JsonSerde.of(VehicleSnapshot.class);
        Serde<TraveledDistance> traveledDistanceSerde = JsonSerde.of(TraveledDistance.class);

        KTable<String, TraveledDistance> traveledDistanceKTable = builder.table(Topic.VEHICLE_DISTANCE.getValue(), Consumed.with(Serdes.String(), traveledDistanceSerde));
        
        builder.stream(Topic.VEHICLE_SNAPSHOT.getValue(), Consumed.with(Serdes.String(), vehicleSnapshotSerde))
               .peek((key, value) -> System.out.println("received vehicle snapshot: key: " + key + ", vehicle: " + value.getVehicle().getName() + ", position: " + value.getPosition().getPosition()))
               .leftJoin(traveledDistanceKTable, (update, distance) -> distanceVehicleJoiner.apply(distance,update), Joined.with(Serdes.String(), vehicleSnapshotSerde, traveledDistanceSerde))
               .groupByKey(Serialized.with(Serdes.String(),traveledDistanceSerde))
               //.reduce()
               .reduce((previous, current) -> current)
               .toStream()
               .foreach((key, value) -> process(key, value));
        //               .foreach((key, value) -> System.out.println("Vehicle received update: (" + value.getVehicle().getName() + " -> " + value.getPosition().getPosition().getX() + "," + value.getPosition().getPosition().getY() + " updates)"));

        final Topology topology = builder.build();

        final KafkaStreams streams = new KafkaStreams(topology, Builder.consumerProps(CONSUMER_ID, bootstrapServers));
        streams.cleanUp();

        ShutdownHook.of(() -> streams.start(), () -> streams.close()).await();
    }

    void process(String key, TraveledDistance value){
        producer.send(new ProducerRecord<>(Topic.VEHICLE_DISTANCE.getValue(), key, value));
        distanceUpdateService.updateDistance(value.getId(), value.getName(), value.getDistance());
    }
}