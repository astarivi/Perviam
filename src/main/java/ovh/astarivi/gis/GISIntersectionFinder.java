package ovh.astarivi.gis;


import org.tinylog.Logger;
import ovh.astarivi.gis.remote.Overpass;
import ovh.astarivi.gis.remote.exceptions.BadRequestException;
import ovh.astarivi.gis.remote.exceptions.PrematureStopException;
import ovh.astarivi.gis.remote.models.OverpassResponse;
import ovh.astarivi.gis.remote.models.reverse.ReverseElement;

import java.io.IOException;


public class GISIntersectionFinder {
    private final static String query =
            "(way(id:%d);)->.y;" +
            "node(w)->.b;" +
            "(way(around:0)[highway~\".\"][highway!~\"path|track|cycleway|footway|secondary_link|service|primary_link\"];)->.z;" +
            "(.z; - .y;)->.x;" +
            "(.x;.x>;)->.k;" +
            "(node.b.k;)->.c;" +
            ".y out;" +
            ".x out qt;" +
            ".c out qt;";

    public static ReverseElement[] getClosestIntersections(ReverseElement element) throws PrematureStopException {
        OverpassResponse<ReverseElement> response;

        try {
            response = Overpass.executeQuery(
                    query.formatted(
                            element.id
                    ),
                    ReverseElement.class
            );
        } catch (BadRequestException | IOException e) {
            Logger.info("Dropping last request for intersection {} due to last issue", element);
            throw new PrematureStopException(e);
        }

        if (response.elements.length == 0) return null;


    }
}
