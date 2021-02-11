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

    private List<String> logObjects = new ArrayList<>();
    private String logObject1;
    private String logObject2;

    @Before
    public void setup() {
        String timestamp = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Timestamp(System.currentTimeMillis()));
        logObject1 = "[02-11-2021 14:11:33] Service 1:Class 1:INFO - Message 1";
        logObject2 = "[02-11-2021 14:11:33] Service 2:Class 1:ERROR - Message 2";

        logObjects.add(logObject1);
        logObjects.add(logObject2);
    }

    @Test
    public void getLogs_shouldReturnListOfLogs() {
        when(logServiceMock.getLogs()).thenReturn(logObjects);
        ResponseEntity<List<String>> response = logController.getLogs();
        Assert.assertEquals(logObjects, response.getBody());
    }

    @Test
    public void getLogsByService_shouldReturnLogsForService_1_whenServiceEqualsService_1() {
        List<String> logObjectsFromService_1 = new ArrayList<>();
        logObjectsFromService_1.add(logObject1);
        when(logServiceMock.getLogsByService("Test Service 1")).thenReturn(logObjectsFromService_1);
        ResponseEntity<List<String>> response = logController.getLogsByService("Test Service 1");
        Assert.assertEquals(logObjectsFromService_1, response.getBody());
    }

    @Test
    public void getLogsByService_shouldReturnLogsForType_ERROR_whenTypeEquals_ERROR() {
        List<String> logObjectWithType_ERROR = new ArrayList<>();
        logObjectWithType_ERROR.add(logObject2);
        when(logServiceMock.getLogsByType(LogType.ERROR)).thenReturn(logObjectWithType_ERROR);
        ResponseEntity<List<String>> response = logController.getLogsByType(LogType.ERROR);
        Assert.assertEquals(logObjectWithType_ERROR, response.getBody());
    }
}
