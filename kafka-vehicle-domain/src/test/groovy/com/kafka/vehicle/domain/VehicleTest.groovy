package com.kafka.vehicle.domain

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

class VehicleTest extends Specification {
    
    ObjectMapper mapper = new ObjectMapper()
    
    def "should serialize vehicle"() {
        
        given:
        
        Vehicle vehicle = Vehicle.of("my vehicle", Metadata.of(1))
        
        when:
        
        def json = mapper.writeValueAsString(vehicle)
        
        then:
        
        json == """{"id":"$vehicle.id","name":"my vehicle","metadata":{"passengerCount":1}}"""
    }

    def "should de-serialize vehicle"() {

        given:
        
        String json = '{"id":"1c82473f-b1f5-41a5-8e24-732ae664b305","name":"my vehicle","metadata":{"passengerCount":1}}'

        

        when:

        Vehicle vehicle = mapper.readValue(json, Vehicle)

        then:

        vehicle.id == '1c82473f-b1f5-41a5-8e24-732ae664b305'
        vehicle.name == 'my vehicle'
        vehicle.metadata.passengerCount == 1
    }
}
