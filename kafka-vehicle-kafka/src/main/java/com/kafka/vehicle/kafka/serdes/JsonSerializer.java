package com.kafka.vehicle.kafka.serdes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class JsonSerializer<T> implements Serializer<T> {

    ObjectMapper objectMapper = new ObjectMapper();

    public JsonSerializer() {

        objectMapper.registerModule(new ParameterNamesModule())
                    .registerModule(new Jdk8Module())
                    .registerModule(new JavaTimeModule());
    }

    @Override
    public void configure(final Map<String, ?> configs, final boolean isKey) {

    }

    @Override
    public byte[] serialize(final String topic, final T data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            return new byte[0];
        }
    }

    @Override
    public void close() {

    }
}
