//package com.example.kafka_producer.controller;
//
//import com.example.kafka_producer.appline.service.MessageProducer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/kafka")
//public class KafkaController {
//
//    private final MessageProducer messageProducer;
//
//    @Autowired
//    public KafkaController(MessageProducer messageProducer) {
//        this.messageProducer = messageProducer;
//    }
//
//    @PostMapping("/send")
//    public ResponseEntity<String> sendMessage(@RequestBody String message) {
//        messageProducer.sendMessage("input-topic", message);
//        return ResponseEntity.ok("Message sent to Kafka");
//    }
//}
