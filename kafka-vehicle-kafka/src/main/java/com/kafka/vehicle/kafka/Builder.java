package com.kafka.vehicle.kafka;

import com.kafka.vehicle.kafka.serdes.JsonSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;

public interface Builder {

    static Properties producerOf(String id, String bootstrapServers) {
        return producerOf(id, bootstrapServers, Serdes.String().getClass());
    }
    
    static Properties producerOf(String id, String bootstrapServers, Class keySerderClass) {

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, id);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 5);
        props.put(StreamsConfig.POLL_MS_CONFIG, 5);

        return props;
    }

    static <Key, Value> Producer<Key, Value> producerWithJsonSerializer(String id, String bootstrapServers) {

        Properties producerConfig = producerOf(id, bootstrapServers);
        return new KafkaProducer<>(producerConfig,  new StringSerializer(), new JsonSerializer());
    }
    
    static <Key, Value> Producer<Key, Value> producerOf(String id, String bootstrapServers, Serializer<Key> keySerializer, Serializer<Value> valueSerializer) {

        Properties producerConfig = producerOf(id, bootstrapServers);
        return new KafkaProducer<>(producerConfig, keySerializer, valueSerializer);
    }
    
    static <Key, Value> Producer<Key, Value> producerOf(Properties producerConfig, Serializer<Key> keySerializer, Serializer<Value> valueSerializer) {
        return new KafkaProducer<>(producerConfig, keySerializer, valueSerializer);
    }
}
