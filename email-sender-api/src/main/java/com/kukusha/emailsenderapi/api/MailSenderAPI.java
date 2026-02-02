package com.kukusha.emailsenderapi.api;

import com.kukusha.emailsenderapi.config.KafkaTopicKeysConfig;
import com.kukusha.emailsenderapi.model.KafkaMessageObject;
import com.kukusha.emailsenderapi.model.RegisterSuccessfullEmailData;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MailSenderAPI {
    private final KafkaTemplate<String, KafkaMessageObject> kafkaTemplate;

    public MailSenderAPI(KafkaTemplate<String, KafkaMessageObject> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEmail(RegisterSuccessfullEmailData registerSuccessfullEmailData) {
        kafkaTemplate.send(KafkaTopicKeysConfig.USER_REGISTER_EMAIL, registerSuccessfullEmailData.getUsername(), registerSuccessfullEmailData);
    }
}
