package com.tutor.tutorlab.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
@RequiredArgsConstructor
public class HtmlEmailService implements EmailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void send(EmailMessage message) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        // MimeMessageHelper mimeMessageHelper = new MimeMessageHelper();
    }
}
