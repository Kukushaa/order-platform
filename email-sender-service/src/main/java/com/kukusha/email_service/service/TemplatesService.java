package com.kukusha.email_service.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class TemplatesService {
    private final TemplateEngine templateEngine;

    public TemplatesService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String getTemplate(Templates templates, Map<String, Object> params) {
        Context context = new Context();
        context.setVariables(params);

        return templateEngine.process(templates.getTemplate(), context);
    }

    @Getter
    public enum Templates {
        REGISTRATION("email/users/register", "Welcome!"),
        PRODUCT_CREATE("email/products/create", "Product was created!"),
        PAYMENT_COMPLETED("email/payments/completed", "Your payment completed successfully!"),;

        private final String template;
        private final String subject;

        Templates(String template, String subject) {
            this.template = template;
            this.subject = subject;
        }
    }
}
