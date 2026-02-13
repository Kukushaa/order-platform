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
        REGISTRATION("email/users/register"),
        PRODUCT_CREATE("email/products/create"),
        PAYMENT_COMPLETED("email/payments/completed");

        private final String template;

        Templates(String template) {
            this.template = template;
        }
    }
}
