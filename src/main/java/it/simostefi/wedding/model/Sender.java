package it.simostefi.wedding.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import lombok.Data;

@Data
@Entity
public class Sender {

    @Id
    private String email;

    private String nome;

    private String accessToken;

    private String refreshToken;
}
