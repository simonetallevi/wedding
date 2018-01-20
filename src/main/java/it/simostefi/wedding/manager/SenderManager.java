package it.simostefi.wedding.manager;

import it.simostefi.wedding.model.Email;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class SenderManager extends Manager {

    public void sendEmails(List<Email> emailList){
        for(Email email : emailList){

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
