package it.simostefi.wedding.service.gmail;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import it.simostefi.wedding.config.EnvConstants;
import it.simostefi.wedding.model.Email;
import it.simostefi.wedding.utils.Utils;
import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Slf4j
public class GmailService {

    private Gmail gmail;

    public GmailService(Credential credential) {
        gmail = new Gmail.Builder(Utils.HTTP_TRANSPORT, Utils.JSON_FACTORY, credential)
                .setApplicationName(EnvConstants.APP_NAME)
                .build();
    }

    public Email sendEmail(Email email, String subject, String body) throws MessagingException, IOException {
        log.info("sending {}", email);
        Message message = gmail.users().messages().send("me",
                createMessageWithEmail(createEmail(email.getEmails(), subject, body))).execute();
        log.info("sent {}", email);
        email.setSent(true);
        return email;
    }

    private MimeMessage createEmail(List<String> to,
                                    String subject,
                                    String bodyText)
            throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        for (String t : to) {
            email.addRecipient(javax.mail.Message.RecipientType.TO,
                    new InternetAddress(t));
        }
        email.setSubject(subject);
        email.setContent(bodyText, "text/html");
        return email;
    }

    private Message createMessageWithEmail(MimeMessage emailContent)
            throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }
}
