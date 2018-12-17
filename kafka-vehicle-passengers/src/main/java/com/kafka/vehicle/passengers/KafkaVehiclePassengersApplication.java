package com.kafka.vehicle.passengers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaVehiclePassengersApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(KafkaVehiclePassengersApplication.class, args);
	}
	
	/*
	
	
	| vehicle_id | name | distance|
	
	
	| make | year of make | total passengers transported | 
	
	 */

}

