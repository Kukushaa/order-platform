package com.kukusha.email_service.kafka;

import com.kukusha.email_service.service.MailService;
import com.kukusha.email_service.service.TemplatesService;
import com.kukusha.kafka_messages_sender.model.RegisterSuccessfullEmailData;
import jakarta.mail.MessagingException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KafkaMailSenderComponent {
    private final MailService mailService;
    private final TemplatesService templatesService;

    public KafkaMailSenderComponent(MailService mailService, TemplatesService templatesService) {
        this.mailService = mailService;
        this.templatesService = templatesService;
    }

    @KafkaListener(topics = "emails.register", groupId = "email-sender-service")
    public void sendEmail(RegisterSuccessfullEmailData data) {
        String template = templatesService.getTemplate(TemplatesService.Templates.REGISTRATION, Map.of("username", data.getUsername()));

        try {
            mailService.sendHtml(data.getEmail(), "Welcome!", template);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "emails.product.create", groupId = "email-sender-service")
    public void sendProductCreateEmail() {

    }
}
