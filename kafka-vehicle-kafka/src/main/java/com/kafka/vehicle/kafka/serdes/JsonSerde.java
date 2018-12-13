package com.kafka.vehicle.kafka.serdes;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public interface JsonSerde extends Serde {

    static <T> Serde<T> of(Class<T> type) {
        return Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(type));
    }
}
