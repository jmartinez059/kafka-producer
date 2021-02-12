package com.MNTDemo.Producer.service;

import com.MNTDemo.Producer.model.LogObject;
import com.MNTDemo.Producer.model.LogType;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
@EmbeddedKafka(topics = {"${env.logging-topic}"}, partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092",
        "port=9092" })
@SpringBootTest
public class KafkaProducerServiceTest {
    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private Consumer<String, String> consumer;

    @Value("${env.logging-topic}")
    private String TOPIC;

    @BeforeAll
    public void setup() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker);
        ConsumerFactory<String, String> cf = new DefaultKafkaConsumerFactory<String, String>(consumerProps,
                new StringDeserializer(), new StringDeserializer());
        consumer = cf.createConsumer();
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, TOPIC);
    }


    @AfterAll
    public void teardown() {
        if (consumer != null) {
            consumer.close();
        }
    }

    @Test
    public void sendMessage_shouldPutLogObjectInTopic() {
        LogObject logObject = LogObject.builder()
                .service("Test Service")
                .clazz("Test Class")
                .message("Test message to be placed in topic")
                .type(LogType.INFO)
                .timestamp("MM-dd-yyyy HH:mm:ss")
                .build();

        kafkaProducerService.sendMessage(logObject);

        ConsumerRecord<String, String> receivedMessage = KafkaTestUtils.getSingleRecord(consumer, TOPIC);
        Assert.assertNotNull(receivedMessage);
        String actualMessage = receivedMessage.value();
        String expected = "[MM-dd-yyyy HH:mm:ss] Test Service:Test Class:INFO - Test message to be placed in topic";
        Assert.assertEquals(expected, actualMessage);
    }
}
