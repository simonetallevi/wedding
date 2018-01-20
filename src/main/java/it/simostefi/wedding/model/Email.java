package it.simostefi.wedding.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Email {

    @Id
    private String id;

    private List<String> emails;

    @Index
    private Boolean sent = false;

    @Index
    private Boolean open = false;
}
