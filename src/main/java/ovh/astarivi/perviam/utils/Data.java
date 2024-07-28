package ovh.astarivi.perviam.utils;

import lombok.AccessLevel;
import lombok.Getter;
import okhttp3.OkHttpClient;
import ovh.astarivi.perviam.i18n.I18N;

import java.util.concurrent.TimeUnit;


@Getter
public class Data {
    @Getter(AccessLevel.NONE)
    private static volatile Data _instance = null;
    private final Settings settings = Settings.load();
    private final I18N i18n = new I18N(settings);
    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .readTimeout(
                    // Dumb double casting call
                    Long.parseLong(
                            System.getenv().getOrDefault("REMOTE_TIMEOUT", String.valueOf(settings.remoteTimeout))
                    ),
                    TimeUnit.SECONDS
            )
            .build();

    public static Data getInstance() {
        if (_instance == null) {
            synchronized (Data.class) {
                if (_instance == null) _instance = new Data();
            }
        }
        return _instance;
    }
}
