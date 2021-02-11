package com.MNTDemo.Producer.controller;

import com.MNTDemo.Producer.model.LogObject;
import com.MNTDemo.Producer.model.LogType;
import com.MNTDemo.Producer.service.LogService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LogControllerTest {

    @Mock
    private LogService logServiceMock;

    @InjectMocks
    private LogController logController;

    private List<LogObject> logObjects = new ArrayList<>();
    private LogObject logObject1;
    private LogObject logObject2;

    @Before
    public void setup() {
        String timestamp = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Timestamp(System.currentTimeMillis()));
        logObject1 = LogObject.builder()
                .service("Test Service 1")
                .clazz  ("Test Class 1")
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
        when(logServiceMock.getLogs()).thenReturn(logObjects);
        ResponseEntity<List<LogObject>> response = logController.getLogs();
        Assert.assertEquals(logObjects, response.getBody());
    }

    @Test
    public void getLogsByService_shouldReturnLogsForService_1_whenServiceEqualsService_1() {
        List<LogObject> logObjectsFromService_1 = new ArrayList<>();
        logObjectsFromService_1.add(logObject1);
        when(logServiceMock.getLogsByService("Test Service 1")).thenReturn(logObjectsFromService_1);
        ResponseEntity<List<LogObject>> response = logController.getLogsByService("Test Service 1");
        Assert.assertEquals(logObjectsFromService_1, response.getBody());
    }

    @Test
    public void getLogsByService_shouldReturnLogsForType_ERROR_whenTypeEquals_ERROR() {
        List<LogObject> logObjectWithType_ERROR = new ArrayList<>();
        logObjectWithType_ERROR.add(logObject2);
        when(logServiceMock.getLogsByType(LogType.ERROR)).thenReturn(logObjectWithType_ERROR);
        ResponseEntity<List<LogObject>> response = logController.getLogsByType(LogType.ERROR);
        Assert.assertEquals(logObjectWithType_ERROR, response.getBody());
    }
}
