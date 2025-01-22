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

@Controller("/api/v1/reverse")
public class GeoReverseController {
    @Get
    @Produces(MediaType.TEXT_PLAIN)
    @ExecuteOn(TaskExecutors.BLOCKING)
    public String reverse(@QueryValue double latitude, @QueryValue double longitude) throws Exception {
        return TranslateLocation.get(
                new GISPoint2D(
                        latitude,
                        longitude
                )
        );
    }
}