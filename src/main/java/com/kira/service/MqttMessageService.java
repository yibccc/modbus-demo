package com.kira.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class MqttMessageService {
    private static final Logger log = LoggerFactory.getLogger(MqttMessageService.class);



    public void processMessage(String topic, String payload) {
        try {

            // 处理 sky/demo 主题的消息
            if (topic.equals("sky/demo")) {
                log.info("接收到来自 sky/demo 主题的消息: {}", payload);
                // 这里可以根据需要处理 sky/demo 主题的消息
                // 例如，解析 JSON 并打印内容
                // 如果需要将其转换为某个 DTO 类，请确保 DTO 类的字段与 JSON 结构匹配
            }

        } catch (Exception e) {
            log.error("处理MQTT消息失败: topic={}, payload={}", topic, payload, e);
        }
    }
} 