package it.simostefi.wedding.manager;

import au.com.bytecode.opencsv.CSVReader;
import it.simostefi.wedding.model.Email;
import it.simostefi.wedding.service.datastore.DatastoreService;
import it.simostefi.wedding.service.gmail.GmailService;
import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SenderManager extends Manager {

    private GmailService gmailService;

    public SenderManager(){
        gmailService = new GmailService(getTechUserCredential());
    }

    public void sendEmails() throws IOException, MessagingException {
        ClassLoader classLoader = getClass().getClassLoader();
        CSVReader reader = new CSVReader(new InputStreamReader(classLoader.getResourceAsStream("inviti/inviti.csv")));
        reader.readNext();

        String[] tokens = null;
        List<Email> emails = new ArrayList<>();
        while ((tokens = reader.readNext()) != null){
            emails.add(Email.getEmail(tokens));
        }
        datastoreService.ofy().save().entities(emails);
        log.info("Email stored");
        sendEmails(emails);
    }

    private void sendEmails(List<Email> emails) throws IOException, MessagingException {
        for(Email email : emails){
            gmailService.sendEmail(email, "test", "body");
        }
    }

    public void registerView(String id){
        Email email = datastoreService.ofy().load().type(Email.class).id(id).now();
        if(email == null) {
            log.error("Email not found for id {}", id);
            return;
        }
        email.setOpen(true);
        datastoreService.ofy().save().entity(email);
    }
}
