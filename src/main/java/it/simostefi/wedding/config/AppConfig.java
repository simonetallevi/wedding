package it.simostefi.wedding.config;

import com.google.appengine.api.utils.SystemProperty;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Clement on 22/06/2015.
 */

@Slf4j
@AllArgsConstructor
public class AppConfig {

    public final String applicationId;

    public static AppConfig get() {
        return new AppConfig(SystemProperty.applicationId.get());
    }
}
