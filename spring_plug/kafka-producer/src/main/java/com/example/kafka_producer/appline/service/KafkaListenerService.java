package com.example.kafka_producer.appline.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;

@Service
public class KafkaListenerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();


    private final Counter messageCounter;
    private final Counter errorCounter;
    private final Timer processingTime;

    public KafkaListenerService(KafkaTemplate<String, String> kafkaTemplate, MeterRegistry meterRegistry) {
        this.kafkaTemplate = kafkaTemplate;

        this.messageCounter = meterRegistry.counter("kafka_listener_messages_total", "service", "KafkaListenerService");
        this.errorCounter = meterRegistry.counter("kafka_listener_errors_total", "service", "KafkaListenerService");
        this.processingTime = meterRegistry.timer("kafka_listener_processing_duration", "service", "KafkaListenerService");
    }

    @KafkaListener(topics = "input-topic", groupId = "group_id")
    public void consume(String message) {

        Timer.Sample sample = Timer.start();

        try {
            JsonNode jsonNode = objectMapper.readTree(message);

            if (jsonNode.has("userId")) {
                String updatedUserId = jsonNode.get("userId").asText() + "123";
                ((ObjectNode) jsonNode).put("userId", updatedUserId);

                String updatedMessage = objectMapper.writeValueAsString(jsonNode);
                kafkaTemplate.send("output-topic", updatedMessage);

                messageCounter.increment();
            } else {
                System.out.println("Поле userId не найдено в сообщении");
            }
        } catch (IOException e) {
            System.err.println("Ошибка при обработке JSON-сообщения: " + e.getMessage());

            errorCounter.increment();
        } finally {

            sample.stop(processingTime);
        }
    }
}


