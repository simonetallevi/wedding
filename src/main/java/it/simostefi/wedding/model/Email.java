package it.simostefi.wedding.model;

import com.google.common.collect.ImmutableList;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
public class Email {

    @Id
    private String id;

    private String name;

    private List<String> emails;

    @Index
    private Boolean sent = false;

    @Index
    private Boolean open = false;

    @Index
    private Answer answer = Answer.NONE;

    public enum Answer{
        NONE,
        OK,
        NO
    }

    public static Email getEmail(String[] tokens){
        Email email = new Email();
        email.setId(UUID.randomUUID().toString());
        email.setEmails(ImmutableList.copyOf(tokens[1].split("|")));
        email.setName(tokens[0]);
        return email;
    }
}
