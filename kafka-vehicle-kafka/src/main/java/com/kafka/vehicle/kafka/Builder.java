package com.kafka.vehicle.kafka;

import com.kafka.vehicle.kafka.serdes.JsonSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

public interface Builder {

    static Properties consumerProps(String id, String bootstrapServers) {
        return consumerProps(id, bootstrapServers, Serdes.String().getClass());
    }

    static Properties consumerProps(String id, String bootstrapServers, Class keySerderClass) {

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, id);
        props.put(StreamsConfig.CLIENT_ID_CONFIG, id + "-client");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, keySerderClass);
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 5);
        props.put(StreamsConfig.POLL_MS_CONFIG, 5);

        return props;
    }
    
    static Properties producerProps(String bootstrapServers) {

        Properties props = new Properties();
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ACKS_CONFIG, "all");
        props.put(RETRIES_CONFIG, 0);
        props.put(LINGER_MS_CONFIG, 5);

        return props;
    }

    static <Key, Value> Producer<Key, Value> producerWithJsonSerializer(String id, String bootstrapServers) {

        Properties producerConfig = producerProps(bootstrapServers);
        return new KafkaProducer<>(producerConfig,  new StringSerializer(), new JsonSerializer());
    }
    
    static <Key, Value> Producer<Key, Value> producerOf(String id, String bootstrapServers, Serializer<Key> keySerializer, Serializer<Value> valueSerializer) {

        Properties producerConfig = producerProps(bootstrapServers);
        return new KafkaProducer<>(producerConfig, keySerializer, valueSerializer);
    }
    
    static <Key, Value> Producer<Key, Value> producerOf(Properties producerConfig, Serializer<Key> keySerializer, Serializer<Value> valueSerializer) {
        return new KafkaProducer<>(producerConfig, keySerializer, valueSerializer);
    }
}
