package com.MNTDemo.Producer.controller;

import com.MNTDemo.Producer.model.LogObject;
import com.MNTDemo.Producer.model.LogType;
import com.MNTDemo.Producer.service.KafkaProducerService;
import com.MNTDemo.Producer.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class LogController {

    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private LogService logService;

    @PostMapping("/log")
    public ResponseEntity<LogObject> messageToTopic(@RequestBody LogObject logObject) {
        logObject.setTimestamp(logService.getTimeStamp());
        logger.info(String.format("Received log event from %s", logObject.getService()));
        this.kafkaProducerService.sendMessage(logObject);
        return new ResponseEntity<>(logObject, HttpStatus.OK);
    }

    @PostMapping("/logBatch")
    public ResponseEntity<LogObject> batchMessagesToTopic(@RequestBody List<LogObject> logObjects) {
        logObjects.forEach(logObject -> {
            logObject.setTimestamp(logService.getTimeStamp());
            this.kafkaProducerService.sendMessage(logObject);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/logs")
    public ResponseEntity<List<String>> getLogs() {
        logger.info("LogController received request to get all logs, calling LogService");
        List<String> logs = new ArrayList<>();
        try {
             logs = logService.getLogs();
        } catch (Exception e) {
            logger.error("LogController caught exception calling LogService.getLogs():" );
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("LogController got all logs from LogService, returning all logs in http response");
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }

    @GetMapping("/logs/type")
    public ResponseEntity<List<String>> getLogsByType(@RequestParam("value") LogType logType) {
        logger.info(String.format("LogController received request to get all logs with type: %s, calling LogService", logType));
        List<String> logs = new ArrayList<>();
        try {
            logs = logService.getLogsByType(logType);
        } catch (Exception e) {
            logger.error("LogController caught exception calling LogService.getLogsByType():");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("LogController logs for type "  + logType + " from LogService, returning logs in http response");
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }

    @GetMapping("/logs/service")
    public ResponseEntity<List<String>> getLogsByService(@RequestParam("value") String service) {
        logger.info(String.format("LogController received request to get all logs with service: %s, calling LogService", service));
        List<String> logs = new ArrayList<>();
        try {
            logs = logService.getLogsByService(service);
        } catch (Exception e) {
            logger.error("LogController caught exception calling LogService.getLogsByService():");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("LogController logs for service "  + service + " from LogService, returning logs in http response");
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }
}