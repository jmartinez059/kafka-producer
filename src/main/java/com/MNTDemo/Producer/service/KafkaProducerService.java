package com.MNTDemo.Producer.service;

import com.MNTDemo.Producer.model.LogObject;
import com.MNTDemo.Producer.repository.LogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    @Value("${env.logging-topic}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private LogService logService;

    @Autowired
    private LogRepository logRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    public void sendMessage(LogObject logObject) {
        logger.info(String.format("KafkaProducerService sendMessage() called:\n %s", logObject));
        logRepository.save(logObject);
        String logObjectJsonString = logService.convertLogObjectToLogString(logObject);
        kafkaTemplate.send(topic, logObjectJsonString);
        logger.info("Successfully sent message to topic " + topic);
    }
}