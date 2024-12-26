package io.conduktor.demos.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemo {
    private static final Logger log = LoggerFactory.getLogger(ConsumerDemo.class.getSimpleName());

    public static void main(String[] args) {
        log.info("I am Kafka Consumer!");

        String groupId = "my-java-application-2";
        String topic = "demo_java";

        // create Producer properties
        Properties properties = new Properties();

        // connect to local host (note that this is hosted under docker on my machine)
        properties.setProperty("bootstrap.servers", "127.0.0.1:19092");

        // connect to cdk-gateway - another day...

        // set producer properties
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());
        properties.setProperty("group.id", groupId);
        properties.setProperty("auto.offset.reset", "earliest");

        // create the Consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // subscribe to our topic(s)
        consumer.subscribe(Arrays.asList(topic));

        // poll for data
        while (true) {
            log.info("Polling for data...");

            ConsumerRecords<String, String> records =
                    consumer.poll(Duration.ofMillis(100)); // new in Kafka 2.0.0

            for (ConsumerRecord<String, String> record: records) {
                log.info("Key: " + record.key() + " | Value: " + record.value());
                log.info("Partition: " + record.partition() + " | Offset: " + record.offset());
            }
        }


    }
}
