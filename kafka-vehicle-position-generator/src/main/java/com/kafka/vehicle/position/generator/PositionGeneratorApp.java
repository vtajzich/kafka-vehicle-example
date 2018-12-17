package com.kafka.vehicle.position.generator;

import com.kafka.vehicle.domain.*;
import com.kafka.vehicle.kafka.Builder;
import com.kafka.vehicle.kafka.RandomUtil;
import com.kafka.vehicle.kafka.ShutdownHook;
import com.kafka.vehicle.kafka.serdes.JsonSerde;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KTable;

import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static com.kafka.vehicle.kafka.RandomUtil.getRandomNumberInRange;

public class PositionGeneratorApp {

    private static final String PRODUCER_ID = "vehicle-position-generator";
    private static final String CONSUMER_ID = "vehicle-position-generator-consumer";

    final String bootstrapServers = "localhost:9092";

    Map<String, Vehicle> vehicles = new Hashtable<>();

    Producer<String, PositionUpdate> producer = Builder.producerWithJsonSerializer(PRODUCER_ID, bootstrapServers);

    final StreamsBuilder builder = new StreamsBuilder();

    public static void main(String[] args) {

        var positionGenerator = new PositionGeneratorApp();
        positionGenerator.run();
    }

    public void run() {
        
        KTable<String, Vehicle> vehicleTable = builder.table(Topic.VEHICLE_NEW.getValue(), Consumed.with(Serdes.String(), JsonSerde.of(Vehicle.class)));

        vehicleTable.toStream()
                .peek((key, value) -> System.out.println("Received vehicle: " + value))
                .foreach(vehicles::put);

        Thread generator = new Thread(() ->

                Stream.generate(this::generatePositionUpdate)
                        .filter(Objects::nonNull)
                        .forEach(this::producePositionUpdate)
        );

        generator.start();

        final Topology topology = builder.build();
        System.out.println(topology.describe());

        final KafkaStreams streams = new KafkaStreams(topology, Builder.consumerProps(CONSUMER_ID, bootstrapServers));
        streams.cleanUp();

        ShutdownHook.of(() -> streams.start(), () -> streams.close()).await();
    }

    private PositionUpdate generatePositionUpdate() {
        
        if (vehicles.isEmpty()) {
            return null;
        }

        int vehicleFromIndex = getRandomNumberInRange(0, vehicles.size() - 1);

        String vehicleId = vehicles.keySet().toArray(new String[0])[vehicleFromIndex];

        Vehicle vehicle = vehicles.get(vehicleId);

        int x = RandomUtil.getRandomNumberInRange(0, Area.WIDTH);
        int y = RandomUtil.getRandomNumberInRange(0, Area.HEIGHT);

        return PositionUpdate.of(vehicle.getId(), Position.of(x, y));
    }

    private void producePositionUpdate(PositionUpdate update) {
        System.out.println("Sending update: " + update);
        producer.send(new ProducerRecord<>(Topic.VEHICLE_POSITION_UPDATE.getValue(), update.getVehicleId(), update));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
