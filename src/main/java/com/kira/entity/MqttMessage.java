package com.kira.entity;

import lombok.Data;

@Data
public class MqttMessage {
    private String type;
    private String content;
    private Long timestamp;
} 