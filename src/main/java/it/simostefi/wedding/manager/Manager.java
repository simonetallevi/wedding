package it.simostefi.wedding.manager;

import com.google.api.client.auth.oauth2.Credential;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.gson.Gson;
import it.simostefi.wedding.config.EnvConstants;
import it.simostefi.wedding.model.Config;
import it.simostefi.wedding.model.TechUser;
import it.simostefi.wedding.service.credential.CredentialService;
import it.simostefi.wedding.service.datastore.DatastoreService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class Manager {

    protected Gson gson = new Gson();

    protected DatastoreService datastoreService;

    private static TechUser techUser;

    private static Cache<String, Credential> techUserCredential = CacheBuilder.newBuilder()
            .expireAfterWrite(45L, TimeUnit.MINUTES)
            .build();

    public Manager() {
        datastoreService = new DatastoreService();
    }

    public Credential getTechUserCredential() {
        try {
            Credential credential = techUserCredential.getIfPresent(Config.K.techUser.get());
            if (credential != null) {
                return credential;
            }
            if (techUser == null) {
                techUser = datastoreService.ofy().load().type(TechUser.class).id(Config.K.techUser.get()).now();
            }
            return CredentialService.getCredential(techUser, Config.K.clientId.get(), Config.K.clientSecret.get());
        } catch (Throwable throwable) {
            throw new RuntimeException("Error loading credentials", throwable);
        }
    }
}
