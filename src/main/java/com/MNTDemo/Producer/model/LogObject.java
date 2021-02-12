package com.MNTDemo.Producer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LogObject")
public class LogObject {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "service")
    private String service;
    @JsonProperty("class")
    @Column(name = "class")
    private String clazz;
    @Column(name = "message")
    private String message;
    @Column(name = "type")
    private LogType type;
    @Column(name = "timestamp")
    private String timestamp;

    public void setService(String service) {
        this.service = service;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setType(LogType type) {
        this.type = type;
    }

    public String getService() {
        return service;
    }

    public String getClazz() {
        return clazz;
    }

    public String getMessage() {
        return message;
    }

    public LogType getType() {
        return type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "LogObject{" +
                ", service='" + service + '\'' +
                ", clazz='" + clazz + '\'' +
                ", message='" + message + '\'' +
                ", type=" + type +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
