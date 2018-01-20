package it.simostefi.wedding.manager;

import au.com.bytecode.opencsv.CSVReader;
import it.simostefi.wedding.model.Email;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SenderManager extends Manager {

    public SenderManager(){

    }

    public void sendEmails() throws IOException {
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
    }

    private void sendEmail(Email email){

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
