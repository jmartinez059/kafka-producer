package com.MNTDemo.Producer.service;

import com.MNTDemo.Producer.model.LogObject;
import com.MNTDemo.Producer.model.LogType;
import com.MNTDemo.Producer.repository.LogRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LogServiceTest {

    @Mock
    private LogRepository logRepository;

    @InjectMocks
    private LogService logService;

    private List<LogObject> logObjects = new ArrayList<>();
    private LogObject logObject1;
    private LogObject logObject2;

    @Before
    public void setup() {
        String timestamp = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Timestamp(System.currentTimeMillis()));
        logObject1 = LogObject.builder()
                .service("Test Service 1")
                .clazz("Test Class 1")
                .message("Test Message 1")
                .type(LogType.INFO)
                .timestamp(timestamp)
                .build();

        logObject2 = LogObject.builder()
                .service("Test Service 2")
                .clazz("Test Class 2")
                .message("Test Message 2")
                .type(LogType.ERROR)
                .timestamp(timestamp)
                .build();

        logObjects.add(logObject1);
        logObjects.add(logObject2);
    }

    @Test
    public void getLogs_shouldReturnListOfLogs() {
        when(logRepository.findAll()).thenReturn(logObjects);
        List<String> response = logService.getLogs();
        Assert.assertEquals(logService.convertLogObjectListToStringList(logObjects), response);
    }

    @Test
    public void getLogsByService_shouldReturnLogsForService_1_whenServiceEqualsService_1() {
        List<LogObject> logObjectsFromService_1 = new ArrayList<>();
        logObjectsFromService_1.add(logObject1);
        when(logRepository.findByService("Test Service 1")).thenReturn(logObjectsFromService_1);
        List<String> response = logService.getLogsByService("Test Service 1");
        Assert.assertEquals(logService.convertLogObjectListToStringList(logObjectsFromService_1), response);
    }

    @Test
    public void getLogsByService_shouldReturnLogsForType_ERROR_whenTypeEquals_ERROR() {
        List<LogObject> logObjectWithType_ERROR = new ArrayList<>();
        logObjectWithType_ERROR.add(logObject2);
        when(logRepository.findByType(LogType.ERROR)).thenReturn(logObjectWithType_ERROR);
        List<String> response = logService.getLogsByType(LogType.ERROR);
        Assert.assertEquals(logService.convertLogObjectListToStringList(logObjectWithType_ERROR), response);
    }
}
