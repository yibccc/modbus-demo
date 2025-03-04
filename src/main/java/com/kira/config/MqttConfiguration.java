package com.kira.config;

import com.kira.mqtt.MyMqttCallback;
import com.kira.mqtt.MyMqttClient;
import com.kira.service.MqttMessageService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfiguration {
    private static final Logger log = LoggerFactory.getLogger(MqttConfiguration.class);

    @Value("${mqtt.host}")
    private String host;
    
    @Value("${mqtt.username}")
    private String username;
    
    @Value("${mqtt.password}") 
    private String password;
    
    @Value("${mqtt.clientId}")
    private String clientId;
    
    @Value("${mqtt.timeout}")
    private int timeout;
    
    @Value("${mqtt.keepalive}")
    private int keepalive;
    
    @Value("${mqtt.topic}")
    private String topic;

    @Autowired
    private MqttMessageService mqttMessageService;

    @Bean
    public MyMqttClient mqttClient() {
        MyMqttClient mqttClient = new MyMqttClient(host, username, password, clientId, timeout, keepalive);
        // 尝试连接
        for (int i = 0; i < 10; i++) {
            try {
                mqttClient.connect();
                mqttClient.setCallback(new MyMqttCallback(mqttClient, mqttMessageService));
                // 订阅主题
                mqttClient.subscribe("sky/test/message", 1);
                mqttClient.subscribe("sky/demo", 1); // 订阅 sky/demo 主题
                log.info("MQTT客户端连接成功并订阅主题: sky/test/message, sky/demo");
                return mqttClient;
            } catch (MqttException e) {
                log.error("MQTT连接失败,重试次数={}", i, e);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return mqttClient;
    }
} 