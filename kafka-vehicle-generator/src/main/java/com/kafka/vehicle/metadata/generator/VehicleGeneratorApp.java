package com.kafka.vehicle.metadata.generator;

import com.kafka.vehicle.domain.Metadata;
import com.kafka.vehicle.domain.Topic;
import com.kafka.vehicle.domain.Vehicle;
import com.kafka.vehicle.kafka.Builder;
import com.kafka.vehicle.kafka.ShutdownHook;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Objects;
import java.util.stream.Stream;

public class VehicleGeneratorApp {

    public static void main(String[] args) {

        String ip = args[0];

        Producer<String, Vehicle> producer = Builder.producerWithJsonSerializer("vehicle", ip + ":9092");

        Thread generator = new Thread(() -> {

            Stream.generate(() -> Vehicle.of("nice name", Metadata.of(1)))
                  .filter(Objects::nonNull)
                  .peek(vehicle -> System.out.println("Vehicle: " + vehicle))
                  .forEach(vehicle -> {

                      try {
                          producer.send(new ProducerRecord<>(Topic.VEHICLE_NEW.getValue(), vehicle.getId(), vehicle));
                          Thread.sleep(5000);
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }

                  });
        });

        ShutdownHook.of(() -> generator.start()).await();
    }

    
}
