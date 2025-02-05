package com.example.servicefoundation.mail.service.impl;

import com.example.servicefoundation.mail.message.EmailMessage;
import com.example.servicefoundation.mail.service.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService<EmailMessage> {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public void send(EmailMessage message) {
        Map<String, String> args = message.getArgs();

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
            helper.setFrom(message.getSender());
            helper.setSubject(message.getSubject());
            helper.setTo(message.getReceiver());

            Context context = new Context();
            context.setLocale(message.getLocale());
            for (Map.Entry<String, String> varEntry : args.entrySet()) {
                context.setVariable(varEntry.getKey(), varEntry.getValue());
            }

            String html = templateEngine.process(message.getTemplate(), context);
            helper.setText(html, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException messagingException) {
            log.error("Failed to send the email", messagingException);
        }
    }
}
