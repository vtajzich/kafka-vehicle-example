package com.kafka.vehicle.metadata.generator;

import com.kafka.vehicle.domain.Metadata;
import com.kafka.vehicle.domain.Topic;
import com.kafka.vehicle.domain.Vehicle;
import com.kafka.vehicle.domain.VehicleModel;
import com.kafka.vehicle.kafka.Builder;
import com.kafka.vehicle.kafka.RandomUtil;
import com.kafka.vehicle.kafka.ShutdownHook;
import org.apache.commons.io.IOUtils;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VehicleGeneratorApp {

    private List<VehicleModel> vehicleModels;
    private Producer<String, Vehicle> producer = Builder.producerWithJsonSerializer("vehicle", "localhost:9092");

    public static void main(String[] args) throws IOException {

        var vehicleGenerator = new VehicleGeneratorApp();
        vehicleGenerator.run();
    }

    public void run() throws IOException {

        InputStream resource = VehicleGeneratorApp.class.getResourceAsStream("/data.csv");

        vehicleModels = IOUtils.readLines(resource, Charset.defaultCharset())
                .stream()
                .skip(1)
                .map(line -> line.split(","))
                .map(columns -> VehicleModel.of(Integer.valueOf(columns[0]), columns[1].replaceAll("\"", ""), columns[2].replaceAll("\"", "")))
                .collect(Collectors.toList());

        Thread generator = new Thread(() ->

                Stream.generate(this::generateVehicle)
                        .filter(Objects::nonNull)
                        .peek(vehicle -> System.out.println("Vehicle: " + vehicle))
                        .forEach(this::produceVehicle)
        );

        ShutdownHook.of(() -> generator.start(), () -> System.out.println("stop")).await();

    }

    private Vehicle generateVehicle() {
        
        int carModelIndex = RandomUtil.getRandomNumberInRange(0, vehicleModels.size());
        var vehicleModel = vehicleModels.get(carModelIndex);
        int numberOfPassengers = RandomUtil.getRandomNumberInRange(1, 5);

        return Vehicle.of(vehicleModel.getMake() + " - " + vehicleModel.getName(), Metadata.of(vehicleModel.getMake(), vehicleModel.getYear(), numberOfPassengers));
    }

    private void produceVehicle(Vehicle vehicle) {
        try {
            producer.send(new ProducerRecord<>(Topic.VEHICLE_NEW.getValue(), vehicle.getId(), vehicle));
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
