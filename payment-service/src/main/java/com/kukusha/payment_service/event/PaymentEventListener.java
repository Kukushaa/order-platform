package com.kukusha.payment_service.event;

import com.kukusha.kafka_messages_sender.api.KafkaMessagesSenderAPI;
import com.kukusha.kafka_messages_sender.model.KafkaMessagesTopics;
import com.kukusha.kafka_messages_sender.model.PaymentCompletedData;
import com.kukusha.payment_service.database.model.PaymentInfo;
import com.kukusha.payment_service.database.service.PaymentInfoService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {
    private final PaymentInfoService paymentInfoService;
    private final KafkaMessagesSenderAPI kafkaMessagesSenderAPI;

    public PaymentEventListener(PaymentInfoService paymentInfoService, KafkaMessagesSenderAPI kafkaMessagesSenderAPI) {
        this.paymentInfoService = paymentInfoService;
        this.kafkaMessagesSenderAPI = kafkaMessagesSenderAPI;
    }

    @EventListener
    public void handlePaymentCreated(PaymentCreatedEvent event) {
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setPaymentIntentId(event.getPaymentIntent().getId());
        paymentInfo.setClientSecret(event.getPaymentIntent().getClientSecret());
        paymentInfo.setUsername(event.getUsername());
        paymentInfo.setProductId(event.getProductId());
        paymentInfo.setAmount(event.getAmount());
        paymentInfo.setEmail(event.getEmail());

        paymentInfoService.save(paymentInfo);
    }

    @EventListener
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        PaymentInfo paymentInfo = event.getPaymentInfo();
        paymentInfo.setStatus(PaymentInfo.Status.COMPLETED);
        paymentInfoService.save(paymentInfo);

        kafkaMessagesSenderAPI.sendMessage(
                KafkaMessagesTopics.PAYMENT_COMPLETED,
                new PaymentCompletedData(
                        paymentInfo.getUsername(),
                        paymentInfo.getEmail(),
                        paymentInfo.getPaymentIntentId(),
                        paymentInfo.getProductId(),
                        paymentInfo.getAmount()
                )
        );
    }
}
