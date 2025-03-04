package com.kira.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyMqttClient {
    private static final Logger log = LoggerFactory.getLogger(MyMqttClient.class);
    
    private MqttClient client;
    private final String host;
    private final String username;
    private final String password;
    private final String clientId;
    private final int timeout;
    private final int keepalive;

    public MyMqttClient(String host, String username, String password, String clientId, int timeout, int keepalive) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.clientId = clientId;
        this.timeout = timeout;
        this.keepalive = keepalive;
    }

    public void setCallback(MqttCallback callback) {
        if (client != null) {
            client.setCallback(callback);
            log.info("MQTT回调设置成功");
        } else {
            log.error("MQTT客户端为null，无法设置回调");
        }
    }

    public void connect() throws MqttException {
        if (client == null) {
            client = new MqttClient(host, clientId, new MemoryPersistence());
        }
        
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setConnectionTimeout(timeout);
        options.setKeepAliveInterval(keepalive);
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);
        
        if (!client.isConnected()) {
            client.connect(options);
        }
        log.info("MQTT连接成功");
    }

    public void publish(String topic, String message) {
        publish(topic, message, 0, false);
    }

    public void publish(String topic, String message, int qos, boolean retained) {
        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(qos);
            mqttMessage.setRetained(retained);
            client.publish(topic, mqttMessage);
        } catch (MqttException e) {
            log.error("发布消息失败", e);
        }
    }

    public void subscribe(String topic, int qos) throws MqttException {
        client.subscribe(topic, qos);
        log.info("订阅主题: {}", topic);
    }
} 