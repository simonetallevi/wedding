package it.simostefi.wedding.service.credential;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;
import it.simostefi.wedding.config.EnvConstants;
import it.simostefi.wedding.model.TechUser;
import it.simostefi.wedding.service.RetryableService;
import it.simostefi.wedding.service.datastore.DatastoreService;
import it.simostefi.wedding.utils.Utils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;

@Slf4j
public class CredentialService extends RetryableService {

    private String clientId;

    private String clientKey;

    private GoogleAuthorizationCodeFlow authFlow;

    public CredentialService(final String clientId,
                             final String clientKey,
                             final Collection<String> scopes) {
        this.clientId = clientId;
        this.clientKey = clientKey;
        this.authFlow = new GoogleAuthorizationCodeFlow.Builder(
                Utils.HTTP_TRANSPORT, Utils.JSON_FACTORY, clientId, clientKey, scopes)
                .setAccessType("offline")
                .setApprovalPrompt("force").build();
    }

    public GoogleTokenResponse getTokenResponse(String code, String url) throws IOException {
        return authFlow.newTokenRequest(code).setRedirectUri(url).execute();
    }

    public String getAuthURL(String url) {
        return authFlow.newAuthorizationUrl().setRedirectUri(url).build();
    }

    public static Credential getCredential(TechUser techUser, String clientId, String clientKey) throws Throwable {
        DateTime dateTime = new DateTime(techUser.getExpirationTime());
        if (dateTime.plusMinutes(15).isAfterNow()) {
            DatastoreService datastoreService = new DatastoreService();
            Map.Entry<String, Date> map = refreshAccessToken(techUser.getRefreshToken(), clientId, clientKey);
            techUser.setAccessToken(map.getKey());
            techUser.setExpirationTime(map.getValue());
            datastoreService.ofy().save().entity(techUser);
        }
        return new GoogleCredential.Builder()
                .setTransport(Utils.HTTP_TRANSPORT)
                .setJsonFactory(Utils.JSON_FACTORY)
                .setClientSecrets(clientId, clientKey).build().setAccessToken(techUser.getAccessToken());
    }

    public Credential getCredential(String accessToken) {
        return new GoogleCredential.Builder()
                .setTransport(Utils.HTTP_TRANSPORT)
                .setJsonFactory(Utils.JSON_FACTORY)
                .setClientSecrets(clientId, clientKey).build().setAccessToken(accessToken);
    }

    private static Map.Entry<String, Date> refreshAccessToken(String refreshToken, String clientId, String clientKey) throws Throwable {
        TokenResponse response = execute((Callable<TokenResponse>) ()
                -> new GoogleRefreshTokenRequest(Utils.HTTP_TRANSPORT,
                Utils.JSON_FACTORY,
                refreshToken, clientId,
                clientKey).execute());
        String accTok = response.getAccessToken();
        return new AbstractMap.SimpleEntry(accTok, new Date());
    }

    public Userinfoplus getCurrentUser(Credential credential) throws Throwable {
        Oauth2 oauth2 = new Oauth2.Builder(Utils.HTTP_TRANSPORT, Utils.JSON_FACTORY, credential)
                .setApplicationName(EnvConstants.getBaseURL()).build();
        return execute(() -> oauth2.userinfo().v2().me().get().execute());
    }
}
