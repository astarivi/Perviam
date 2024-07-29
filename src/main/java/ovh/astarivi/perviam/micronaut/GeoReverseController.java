package ovh.astarivi.perviam.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import ovh.astarivi.perviam.gis.models.GISPoint2D;
import ovh.astarivi.perviam.location.TranslateLocation;

import static ovh.astarivi.perviam.i18n.Translation.getI18nString;

@Controller("/api/v1/reverse")
public class GeoReverseController {
    @Get
    @Produces(MediaType.TEXT_PLAIN)
    @ExecuteOn(TaskExecutors.BLOCKING)
    public String reverse(@QueryValue String latitude, @QueryValue String longitude) throws Exception {
        GISPoint2D requestedPoint;

        try {
            requestedPoint = new GISPoint2D(
                    Float.parseFloat(latitude),
                    Float.parseFloat(longitude)
            );
        } catch(Exception e) {
            return getI18nString("placeholder");
        }

        String address = TranslateLocation.get(requestedPoint);

        if (address.equals(getI18nString("placeholder"))) {
            throw new Exception();
        }

        return address;
    }
}