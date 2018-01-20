package it.simostefi.wedding.servlet.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.services.oauth2.model.Userinfoplus;
import it.simostefi.wedding.config.EnvConstants;
import it.simostefi.wedding.model.Config;
import it.simostefi.wedding.model.Sender;
import it.simostefi.wedding.service.credential.CredentialService;
import it.simostefi.wedding.service.datastore.DatastoreService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class OauthCallback extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("error") != null) {
            resp.sendRedirect(EnvConstants.getBaseURL());
            return;
        }

        String code = req.getParameter("code");

        CredentialService oauthService = new CredentialService(
                Config.K.clientId.get(),
                Config.K.clientSecret.get(),
                Config.K.scopes.getSet());

        try{
            if (code != null){
                GoogleTokenResponse tokenResponse = oauthService.getTokenResponse(code, EnvConstants.getBaseURL() + "auth/oauthcallback");
                String accessToken = tokenResponse.getAccessToken();
                String refreshToken = tokenResponse.getRefreshToken();
                Userinfoplus userinfoplus = oauthService.getCurrentUser(oauthService.getOauthCredential(accessToken));

                Sender sender = new Sender();
                sender.setNome(userinfoplus.getName());
                sender.setEmail(userinfoplus.getEmail());
                sender.setAccessToken(accessToken);
                sender.setRefreshToken(refreshToken);

                DatastoreService datastoreService = new DatastoreService();
                datastoreService.ofy().save().entity(sender);

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.sendRedirect(EnvConstants.getBaseURL());
            }

        } catch (Throwable throwable) {
            throw new ServletException(throwable);
        }
    }
}
