package com.demo.opentalk.service.Impl;

import com.demo.opentalk.config.ThymeleafTemplateConfig;
import com.demo.opentalk.constants.MessageConstant;
import com.demo.opentalk.dto.MailDTO;
import com.demo.opentalk.service.MailService;
import lombok.RequiredArgsConstructor;

import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final Environment env;
    @Override
        public String sendMail(MailDTO mailDTO) throws MessagingException {
           try {
               SimpleMailMessage message = new SimpleMailMessage();

               message.setFrom(env.getProperty("spring.mail.username"));
               message.setTo(mailDTO.getTo());
               message.setSubject(mailDTO.getSubject());
               message.setText(mailDTO.getContent());
               mailSender.send(message);
               return MessageConstant.SEND_MAIL_DONE;
           } catch (Exception e) {
               return MessageConstant.SEND_MAIL_FAIL;
           }
        }

    @Override
    public String sendMailWithHTML(MailDTO mailDTO, String templateName) throws MessagingException {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            Context context = new Context();
            context.setVariables(mailDTO.getProps());
            String html = templateEngine.process(templateName, context);
            helper.setFrom(env.getProperty("spring.mail.username"));
            helper.setTo(mailDTO.getTo());
            helper.setSubject(mailDTO.getSubject());
            helper.setText(html, true);
            try {
                mailSender.send(message);
                return MessageConstant.SEND_MAIL_DONE;
            } catch (Exception e) {
                return MessageConstant.SEND_MAIL_FAIL;
            }
    }
}
