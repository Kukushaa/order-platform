package com.kukusha.kafka_messages_sender.api;

import com.kukusha.kafka_messages_sender.model.EmailType;
import com.kukusha.kafka_messages_sender.model.KafkaMessageObject;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessagesSenderAPI {
    private final KafkaTemplate<String, KafkaMessageObject> kafkaTemplate;

    public KafkaMessagesSenderAPI(KafkaTemplate<String, KafkaMessageObject> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEmail(EmailType emailType, KafkaMessageObject kafkaMessageObject) {
        kafkaTemplate.send(emailType.getKakfaKey(), kafkaMessageObject.getUsername(), kafkaMessageObject);
    }
}
