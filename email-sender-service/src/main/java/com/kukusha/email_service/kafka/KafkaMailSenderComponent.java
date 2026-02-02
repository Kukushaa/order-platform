package com.kukusha.email_service.kafka;

import com.kukusha.email_service.service.MailService;
import com.kukusha.emailsenderapi.model.RegisterSuccessfullEmailData;
import jakarta.mail.MessagingException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaMailSenderComponent {
    private final MailService mailService;

    public KafkaMailSenderComponent(MailService mailService) {
        this.mailService = mailService;
    }

    // Test send email IMPL, move to DB or .ftl file and read/impl from it
    @KafkaListener(topics = "emails.register", groupId = "email-sender-service")
    public void sendEmail(RegisterSuccessfullEmailData data) {
        String template = "<div style=\"font-family: 'Segoe UI', Helvetica, Arial, sans-serif; background-color: #f9f9f9; padding: 20px;\">\n" +
                "    <div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; border: 1px solid #e0e0e0;\">\n" +
                "        <div style=\"background-color: #4A90E2; padding: 20px; text-align: center;\">\n" +
                "            <h1 style=\"color: #ffffff; margin: 0; font-size: 24px;\">Welcome to the Family!</h1>\n" +
                "        </div>\n" +
                "        <div style=\"padding: 30px; color: #333333; line-height: 1.6;\">\n" +
                "            <p style=\"font-size: 16px;\">Hi <strong>" + data.getUsername() + "</strong>,</p>\n" +
                "            <p>Your account has been successfully created. We're excited to have you with us!</p>\n" +
                "            <div style=\"text-align: center; margin-top: 30px;\">\n" +
                "                <a href=\"[Login URL]\" style=\"background-color: #4A90E2; color: #ffffff; padding: 12px 25px; text-decoration: none; border-radius: 5px; font-weight: bold; display: inline-block;\">Get Started</a>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div style=\"background-color: #f4f4f4; padding: 15px; text-align: center; font-size: 12px; color: #777777;\">\n" +
                "            &copy; 2026 Your Brand. If you didn't create this account, please ignore this email.\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>";
        try {
            mailService.sendHtml(data.getEmail(), "Welcome!", template);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
