package com.MNTDemo.Producer.service;

import com.MNTDemo.Producer.model.LogObject;
import com.MNTDemo.Producer.model.LogType;
import com.MNTDemo.Producer.repository.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LogService {

    private static final Logger logger = LoggerFactory.getLogger(LogService.class);

    @Autowired
    private LogRepository logRepository;

    public List<String> getLogs() {
        logger.info("LogService called, making call to LogRepository");
        List<LogObject> logObjects = new ArrayList<>();
        try {
            logObjects = logRepository.findAll();
        } catch (Exception e) {
            logger.error("LogService caught exception calling LogRepository.getLogs()");
            throw e;
        }
        logger.info("LogService received response from LogRepository, returning logs to LogController");
        return convertLogObjectListToStringList(logObjects);
    }

    public List<String> getLogsByType(LogType logType) {
        logger.info("LogService called, making call to LogRepository");
        List<LogObject> logObjects = new ArrayList<>();
        try {
            logObjects = logRepository.findByType(logType);
        } catch (Exception e) {
            logger.error("LogService caught exception calling LogRepository.getLogs():\n", e.getStackTrace());
            throw e;
        }
        logger.info("Received response from LogRepository, returning logs to LogController");
        return convertLogObjectListToStringList(logObjects);
    }

    public List<String> getLogsByService(String service) {
        logger.info("LogService called, making call to LogRepository");
        List<LogObject> logObjects = new ArrayList<>();
        try {
            logObjects = logRepository.findByService(service);
        } catch (Exception e) {
            logger.error("LogService caught exception calling LogRepository.getLogs():\n", e.getStackTrace());
            throw e;
        }
        logger.info("Received response from LogRepository, returning logs to LogController");
        return convertLogObjectListToStringList(logObjects);
    }

    public String convertLogObjectToLogString(LogObject logObject) {
        String log = String.format("[%s] %s:%s:%s - %s",
                logObject.getTimestamp(), logObject.getService(), logObject.getClazz(), logObject.getType(), logObject.getMessage());
        return log;
    }

    public List<String> convertLogObjectListToStringList(List<LogObject> logObjects) {
        List<String> logs = new ArrayList<>();
        logObjects.forEach(logObject -> {
            logs.add(convertLogObjectToLogString(logObject));
        });
        return logs;
    }
}
