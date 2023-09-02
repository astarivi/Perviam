package ovh.astarivi.gis.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Getter;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;


public class Data {
    private static final ObjectMapper mapper = new ObjectMapper();
    @Getter
    private static final Settings settings = Settings.load();
    @Getter
    private static final OkHttpClient httpClient = new OkHttpClient.Builder()
            .readTimeout(
                    // Dumb double casting call
                    Long.parseLong(
                            System.getenv().getOrDefault("REMOTE_TIMEOUT", String.valueOf(Data.getSettings().remoteTimeout))
                    ),
                    TimeUnit.SECONDS
            )
            .build();

    public static ObjectReader getObjectReader() {
        return mapper.reader();
    }

    public static ObjectWriter getObjectWriter() {
        return mapper.writer();
    }
}
