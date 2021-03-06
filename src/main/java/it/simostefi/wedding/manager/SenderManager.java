package it.simostefi.wedding.manager;

import au.com.bytecode.opencsv.CSVReader;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import it.simostefi.wedding.config.EnvConstants;
import it.simostefi.wedding.model.Email;
import it.simostefi.wedding.service.gmail.GmailService;
import it.simostefi.wedding.service.mapper.MappingService;
import it.simostefi.wedding.service.taskqueue.TaskDef;
import it.simostefi.wedding.service.taskqueue.TaskQueueService;
import it.simostefi.wedding.servlet.task.TaskLauncher;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class SenderManager extends Manager {

    private Joiner joiner = Joiner.on("#");

    public SenderManager() {
    }

    public void storeEmails(String requestId) throws IOException, MessagingException {
        CSVReader reader = new CSVReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("inviti/inviti.csv")), ';');
        reader.readNext();

        List<Email> alreadyStored = datastoreService.ofy().load().type(Email.class).list();
        Set<String> emailSent = new HashSet<>();
        if(!alreadyStored.isEmpty()) {
            for (Email email : alreadyStored) {
                emailSent.add(joiner.join(email.getEmails()));
            }
        }

        String[] tokens;
        List<Email> emails = new ArrayList<>();
        while ((tokens = reader.readNext()) != null) {
            if (tokens[0].isEmpty() || tokens[1].isEmpty()) {
                continue;
            }
            Email email = Email.getEmail(tokens);
            if(!emailSent.contains(joiner.join(email.getEmails()))) {
                emails.add(email);
            }
        }
        datastoreService.ofy().save().entities(emails);

        TaskDef task = new TaskDef(
                TaskOptions.Method.GET, TaskLauncher.Tasks.SEND.getServlet(),
                ImmutableMap.of(
                        "action", "SEND",
                        "ID", requestId), TaskLauncher.Tasks.SEND.name()+requestId);
        TaskQueueService.runTask(TaskLauncher.Tasks.SEND.getQueue(), task);

        log.info("Email stored");
    }

    public void sendEmails() throws IOException, MessagingException {
        GmailService gmailService = new GmailService(getTechUserCredential());
        List<Email> emails = datastoreService.ofy().load().type(Email.class).list();
        if (emails.isEmpty()) {
            return;
        }
        InputStream reader = getClass().getClassLoader().getResourceAsStream("email_template/email-flickr.html");
        StringWriter writer = new StringWriter();
        IOUtils.copy(reader, writer, "UTF-8");
        String body = writer.toString();

        for (Email email : emails) {
            if (email.getSent()) {
                continue;
            }
            String customisedBody = MappingService.getResolvedString(body, ImmutableMap.of(
                    "SALUTATION", email.getSalutation(),
                    "BASEURL", EnvConstants.getBaseURL(),
                    "ID", email.getId()));
            Email sentEmail = gmailService.sendEmail(email, "Ci siamo quasi...", customisedBody, new HashMap<String, String>());
            datastoreService.ofy().save().entity(sentEmail);
        }

        log.info("Email sent");
    }

    public void registerView(String id) {
        Email email = getEmail(id);
        if(email != null) {
            email.setOpen(true);
            datastoreService.ofy().save().entity(email);
        }
    }

    public void registerWeb(String id) {
        Email email = getEmail(id);
        if(email != null) {
            email.setClicked(true);
            datastoreService.ofy().save().entity(email);
        }
    }

    private Email getEmail(String id){
        Email email = datastoreService.ofy().load().type(Email.class).id(id).now();
        if (email == null) {
            log.error("Email not found for id {}", id);
            return null;
        }
        return email;
    }
}
