package it.simostefi.wedding.manager;

import it.simostefi.wedding.config.EnvConstants;
import it.simostefi.wedding.model.Config;
import it.simostefi.wedding.service.credential.CredentialService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RegisterAuthManager extends Manager {

    public RegisterAuthManager(){
        super();
    }

    public String registerUser() {
        CredentialService oauthService = new CredentialService(
                Config.K.clientId.get(),
                Config.K.clientSecret.get(),
                Config.K.scopes.getSet());
        return oauthService.getAuthURL(EnvConstants.getBaseURL() + "auth/oauthcallback");
    }
}
