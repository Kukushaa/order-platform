package com.kukusha.email_service.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kukusha.email_service.service.MailService;
import com.kukusha.email_service.service.TemplatesService;
import com.kukusha.kafka_messages_sender.model.KafkaEmailMessageObject;
import com.kukusha.kafka_messages_sender.model.PaymentCompletedData;
import com.kukusha.kafka_messages_sender.model.ProductCreatedData;
import com.kukusha.kafka_messages_sender.model.RegisterSuccessfulEmailData;
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
    public void sendEmail(RegisterSuccessfulEmailData data) {
        sendEmail(TemplatesService.Templates.REGISTRATION, data);
    }

    @KafkaListener(topics = "emails.product.create", groupId = "email-sender-service")
    public void sendProductCreateEmail(ProductCreatedData data) {
        sendEmail(TemplatesService.Templates.PRODUCT_CREATE, data);
    }

    @KafkaListener(topics = "emails.payment.completed", groupId = "email-sender-service")
    public void sendEmailPaymentCompleted(PaymentCompletedData data) {
        sendEmail(TemplatesService.Templates.PAYMENT_COMPLETED, data);
    }

    private void sendEmail(TemplatesService.Templates templateName, KafkaEmailMessageObject data) {
        String template = templatesService.getTemplate(templateName, toMap(data));

        try {
            mailService.sendHtml(data.getEmailTo(), "Welcome!", template);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> toMap(KafkaEmailMessageObject messageObject) {
        return new ObjectMapper().convertValue(messageObject, new TypeReference<>() {
        });
    }
}
