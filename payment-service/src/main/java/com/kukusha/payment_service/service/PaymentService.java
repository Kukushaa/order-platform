package com.kukusha.payment_service.service;

import com.kukusha.payment_service.database.model.PaymentInfo;
import com.kukusha.payment_service.database.service.PaymentInfoService;
import com.kukusha.payment_service.request.ConfirmPaymentDTO;
import com.kukusha.payment_service.response.CreatePaymentResponse;
import com.kukusha.payment_service.dto.PaymentDTO;
import com.kukusha.payment_service.event.PaymentCompletedEvent;
import com.kukusha.payment_service.event.PaymentCreatedEvent;
import com.kukusha.token_service.model.TokenCreateDTO;
import com.kukusha.token_service.model.TokenResponse;
import com.kukusha.token_service.model.TokenType;
import com.kukusha.token_service.service.TokenProcessorDriver;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.temporal.ChronoUnit;
import java.util.Map;

@Service
public class PaymentService {
    @Value("${token.issuer}")
    private String issuer;

    @Value("${token.minutes}")
    private int tokenMinutes;

    private final StripeService stripeService;
    private final PaymentInfoService paymentInfoService;
    private final TokenProcessorDriver tokenProcessorDriver;
    private final ApplicationEventPublisher eventPublisher;

    public PaymentService(StripeService stripeService,
                          PaymentInfoService paymentInfoService,
                          TokenProcessorDriver tokenProcessorDriver,
                          ApplicationEventPublisher eventPublisher) {
        this.stripeService = stripeService;
        this.paymentInfoService = paymentInfoService;
        this.tokenProcessorDriver = tokenProcessorDriver;
        this.eventPublisher = eventPublisher;
    }

    public CreatePaymentResponse createPayment(PaymentDTO paymentDTO) throws StripeException {
        PaymentIntent paymentIntent = stripeService.createPaymentIntent(paymentDTO);

        eventPublisher.publishEvent(new PaymentCreatedEvent(
                this,
                paymentIntent,
                paymentDTO.getUsername(),
                paymentDTO.getProductId(),
                paymentDTO.getAmount(),
                paymentDTO.getEmail()
        ));

        String paymentToken = createPaymentToken(paymentIntent.getId(), paymentDTO.getUsername(), paymentDTO.getProductId());

        return new CreatePaymentResponse(paymentToken, paymentIntent.getClientSecret());
    }

    @Transactional
    public void confirmPayment(ConfirmPaymentDTO request) throws StripeException {
        String paymentIntentId = validateAndExtractPaymentIntentId(request.getPaymentToken());

        PaymentIntent stripeIntent = stripeService.retrievePaymentIntent(paymentIntentId);
        if ("succeeded".equals(stripeIntent.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Payment already processed");
        }

        PaymentInfo paymentInfo = paymentInfoService.findByPaymentIntentIdWithLock(paymentIntentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));

        if (paymentInfo.getStatus() == PaymentInfo.Status.COMPLETED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Payment already completed");
        }

        eventPublisher.publishEvent(new PaymentCompletedEvent(this, paymentInfo));
    }

    private String createPaymentToken(String paymentIntentId, String username, Long productId) {
        TokenCreateDTO tokenCreateDTO = new TokenCreateDTO.TokenCreateDTOBuilder()
                .issuer(issuer)
                .subject(paymentIntentId)
                .expAt(tokenMinutes)
                .chronoUnit(ChronoUnit.MINUTES)
                .claims(Map.of(
                        "paymentIntentId", paymentIntentId,
                        "username", username,
                        "productId", productId
                ))
                .build();

        TokenResponse tokenResponse = tokenProcessorDriver.getTokenProcessor(TokenType.PAYMENT)
                .createToken(tokenCreateDTO);

        return tokenResponse.token();
    }

    private String validateAndExtractPaymentIntentId(String paymentToken) {
        try {
            return tokenProcessorDriver.getTokenProcessor(TokenType.PAYMENT)
                    .getClaimsAsString(paymentToken, "paymentIntentId");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid payment token");
        }
    }
}
