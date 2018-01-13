package it.simostefi.wedding.config;

import com.google.api.client.http.HttpTransport;
import com.google.appengine.api.utils.SystemProperty;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppEnvironment {

    public static final String APP_NAME = "Simo & Stefi Wedding";

    public static String APP_ID;

    public static AppConfig CONFIG;

    public static String emailAddressMaintenance = "simone.tallevi@gmail.com";

    private static HttpTransport HTTP_TRANSPORT;

    public static void init() {
        CONFIG = AppConfig.get();
        APP_ID = CONFIG.applicationId;
    }
}
