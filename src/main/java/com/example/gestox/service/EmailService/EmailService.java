package com.example.gestox.service.EmailService;

import com.example.gestox.entity.FileStorage;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService implements IEmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    @Autowired
    private SendGrid sendGrid;

    public void sendEmail(String from, String to, String subject, String body, FileStorage file) throws MessagingException, IOException {

        Email dest = new Email(to);
        Email emailFrom = new Email(from);
        Content content = new Content("text/plain", body);

        Mail mail = new Mail(emailFrom, subject, dest, content);

        Attachments attachment = new Attachments();
        if (file != null) {
            attachment.setContent(new String(Base64.encodeBase64(file.getData()), StandardCharsets.UTF_8));
            attachment.setType(file.getFileType());
            attachment.setFilename(file.getFileName());
            attachment.setDisposition("attachment");
            attachment.setContentId("File Attachment");
            mail.addAttachments(attachment);
        }
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            throw ex;
        }
    }
}
