package it.simostefi.wedding.servlet.auth;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.simostefi.wedding.config.EnvConstants;
import it.simostefi.wedding.model.Config;
import it.simostefi.wedding.service.credential.CredentialService;
import it.simostefi.wedding.servlet.AbstractServlet;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class RegisterAuthServlet extends AbstractServlet {

    @Override
    protected void get(Map<String, String> parameters, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CredentialService oauthService = new CredentialService(
                Config.K.clientId.get(),
                Config.K.clientSecret.get(),
                Config.K.scopes.getSet());
        resp.sendRedirect(oauthService.getAuthURL(EnvConstants.getBaseURL() + "oauthcallback"));
    }

    @Override
    protected void post(JsonObject input, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        throw new IllegalStateException("Not supported");
    }
}
