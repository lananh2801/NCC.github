package com.demo.opentalk.service;

import com.demo.opentalk.dto.MailDTO;

import javax.mail.MessagingException;

public interface MailService {
    String sendMail(MailDTO mailDTO) throws MessagingException;
    String sendMailWithHTML(MailDTO mailDTO) throws MessagingException;
}
