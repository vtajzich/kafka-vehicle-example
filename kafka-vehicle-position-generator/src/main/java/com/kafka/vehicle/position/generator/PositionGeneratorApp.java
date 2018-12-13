package com.kafka.vehicle.position.generator;

import com.kafka.vehicle.domain.Position;
import com.kafka.vehicle.domain.PositionUpdate;
import com.kafka.vehicle.domain.Vehicle;
import com.kafka.vehicle.kafka.Builder;
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
import java.util.Properties;
import java.util.stream.Stream;

import static com.kafka.vehicle.kafka.RandomUtil.getRandomNumberInRange;

public class PositionGeneratorApp {

    private static final String CONSUMER_ID = "vehicle-position-generator";

    public static void main(String[] args) {

        Map<String, Vehicle> vehicles = new Hashtable<>();

        Properties props = Builder.props(CONSUMER_ID, "localhost:9092");

        Producer<String, PositionUpdate> producer = Builder.producerWithJsonSerializer(CONSUMER_ID, "localhost:9092");

        final StreamsBuilder builder = new StreamsBuilder();

        KTable<String, Vehicle> vehicleTable = builder.table("vehicle-new", Consumed.with(Serdes.String(), JsonSerde.of(Vehicle.class)));

        vehicleTable.toStream()
                     .peek((key, value) -> System.out.println("Received vehicle: " + value))
                     .foreach((key, value) -> vehicles.put(key, value));

        Thread generator = new Thread(() -> {

            Stream.generate(() -> {

                if (vehicles.isEmpty()) {
                    return null;
                }

                int vehicleFromIndex = getRandomNumberInRange(0, vehicles.size() - 1);

                String vehicleId = vehicles.keySet().toArray(new String[0])[vehicleFromIndex];

                Vehicle vehicle = vehicles.get(vehicleId);

                return PositionUpdate.of(vehicle.getId(), Position.of(0, 0));

            }).filter(Objects::nonNull)
                  .forEach(update -> {

                      System.out.println("Sending update: " + update);
                      producer.send(new ProducerRecord<>("vehicle-position-update", update.getVehicleId(), update));

                      try {
                          Thread.sleep(2000);
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }
                  });
        });

        generator.start();

        final Topology topology = builder.build();
        final KafkaStreams streams = new KafkaStreams(topology, props);

        ShutdownHook.of(() -> streams.start());
    }
}
