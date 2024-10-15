package com.example.gestox.service.EmailService;

import com.example.gestox.entity.FileStorage;

import javax.mail.MessagingException;
import java.io.IOException;

public interface IEmailService {

    public void sendEmail(String from, String to, String subject, String body, FileStorage file) throws MessagingException, IOException ;
}
