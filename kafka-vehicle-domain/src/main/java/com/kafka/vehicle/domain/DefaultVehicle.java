package com.kafka.vehicle.domain;

import java.util.StringJoiner;
import java.util.UUID;

class DefaultVehicle implements Vehicle {

    private final String id; 
    private final String name;
    private final Metadata metadata;

    DefaultVehicle(String name, Metadata metadata) {
        this.id =UUID.randomUUID().toString();
        this.name = name;
        this.metadata = metadata;
    }

    DefaultVehicle(final String id, final String name, final Metadata metadata) {
        this.id = id;
        this.name = name;
        this.metadata = metadata;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DefaultVehicle.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("name='" + name + "'")
                .toString();
    }
}
