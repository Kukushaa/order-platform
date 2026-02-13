package com.kukusha.kafka_messages_sender.api;

import com.kukusha.kafka_messages_sender.model.KafkaMessageObject;
import com.kukusha.kafka_messages_sender.model.KafkaMessagesTopics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaMessagesSenderAPI {
    private final KafkaTemplate<String, KafkaMessageObject> kafkaTemplate;

    public KafkaMessagesSenderAPI(KafkaTemplate<String, KafkaMessageObject> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(KafkaMessagesTopics topics,
                            KafkaMessageObject object) {
        String key = topics.getKey();
        kafkaTemplate.send(key, object.getUsername(), object)
                .whenComplete((result, error) -> {
                    if (error != null) {
                        log.error("Error while sending kafka messages to topics {}", key, error);
                    } else {
                        log.info("Kafka messages sent to topics {}", key);
                    }
                });
    }
}
