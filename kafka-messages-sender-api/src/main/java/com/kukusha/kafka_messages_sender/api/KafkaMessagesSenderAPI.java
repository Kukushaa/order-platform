package com.kukusha.kafka_messages_sender.api;

import com.kukusha.kafka_messages_sender.config.KafkaTopicKeysConfig;
import com.kukusha.kafka_messages_sender.model.KafkaMessageObject;
import com.kukusha.kafka_messages_sender.model.RegisterSuccessfullEmailData;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessagesSenderAPI {
    private final KafkaTemplate<String, KafkaMessageObject> kafkaTemplate;

    public KafkaMessagesSenderAPI(KafkaTemplate<String, KafkaMessageObject> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEmail(RegisterSuccessfullEmailData registerSuccessfullEmailData) {
        kafkaTemplate.send(KafkaTopicKeysConfig.USER_REGISTER_EMAIL, registerSuccessfullEmailData.getUsername(), registerSuccessfullEmailData);
    }
}
