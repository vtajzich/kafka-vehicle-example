package com.kafka.vehicle.kafka.serdes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class JsonDeserializer<T> implements Deserializer<T> {

    private final Class<T> type;
    ObjectMapper objectMapper = new ObjectMapper();

    public JsonDeserializer(Class<T> type) {
        this.type = type;

        objectMapper.registerModule(new ParameterNamesModule())
                    .registerModule(new Jdk8Module())
                    .registerModule(new JavaTimeModule());
    }

    @Override
    public void configure(final Map<String, ?> configs, final boolean isKey) {

    }

    @Override
    public T deserialize(final String topic, final byte[] data) {
        try {
            return objectMapper.readValue(data, type);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void close() {

    }
}
