package com.kafka.vehicle.domain;

import java.util.StringJoiner;
import java.util.UUID;

class DefaultVehicle implements Vehicle {

    private final String id = UUID.randomUUID().toString();
    private final String name;
    private final Metadata metadata;

    public DefaultVehicle(String name, Metadata metadata) {
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
