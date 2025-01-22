package ovh.astarivi.perviam.gis.remote;

import com.fasterxml.jackson.databind.ObjectReader;
import okhttp3.*;
import org.tinylog.Logger;
import ovh.astarivi.perviam.gis.remote.exceptions.BadRequestException;
import ovh.astarivi.perviam.gis.remote.models.OverpassResponse;
import ovh.astarivi.perviam.utils.Data;
import ovh.astarivi.perviam.utils.Serialization;

import java.io.IOException;
import java.util.Objects;


public class Overpass {
    static {
        String buildUrl = System.getenv().getOrDefault("OVERPASS_URL", Data.getInstance().getSettings().overpassUrl);

        HttpUrl parsedOverpassUrl = HttpUrl.parse(
                buildUrl
        );

        if (parsedOverpassUrl == null) {
            Logger.error("Overpass URL couldn't be built with provided string {}", buildUrl);
            Logger.error("Check the settings.json, or ENV variable 'OVERPASS_URL' to fix this issue.");
            Logger.info("Only HTTP and HTTPS urls are supported.");

            throw new RuntimeException("Invalid Overpass URL " + buildUrl);
        }

        overpassUrl = parsedOverpassUrl;
    }

    private static final HttpUrl overpassUrl;

    public static <T> OverpassResponse<T> executeQuery(String query, Class<T> responseType) throws BadRequestException, IOException {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(overpassUrl);
        requestBuilder.addHeader("Accept","application/json");
        requestBuilder.post(
                RequestBody.create(
                        "[out:json];" + query,
                        MediaType.get("text/plain; charset=utf-8")
                )
        );

        try(Response response = Data.getInstance().getHttpClient().newCall(requestBuilder.build()).execute()) {
            if (response.code() != 200) {
                Logger.warn("Overpass responded with code {}, aborting this request...", response.code());
                Logger.info("Dropped request was: {}", query);
                throw new BadRequestException("Server responded with code " + response.code());
            }

            try (ResponseBody body = response.body()) {
                ObjectReader reader = Serialization.getObjectReader();

                return reader.forType(
                        reader.getTypeFactory().constructParametricType(OverpassResponse.class, responseType)
                ).readValue(
                        Objects.requireNonNull(body).bytes()
                );
            }
        } catch (IOException | NullPointerException e) {
            Logger.error("Error while processing Overpass query {} to URL {}", query, overpassUrl);
            Logger.warn("Dropping this request. If you're seeing this error message often, something may be " +
                    "wrong with your Overpass instance, your internet connection may be failing, or the timeout is too low");
            Logger.error(e);
            throw e;
        }
    }
}
