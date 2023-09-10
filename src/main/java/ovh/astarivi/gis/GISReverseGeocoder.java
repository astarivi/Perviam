package ovh.astarivi.gis;

import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;
import ovh.astarivi.gis.models.GISBoundaries;
import ovh.astarivi.gis.models.GISPoint2D;
import ovh.astarivi.gis.models.GISSegment2D;
import ovh.astarivi.gis.remote.Overpass;
import ovh.astarivi.gis.remote.exceptions.BadRequestException;
import ovh.astarivi.gis.remote.exceptions.PrematureStopException;
import ovh.astarivi.gis.remote.models.OverpassResponse;
import ovh.astarivi.gis.remote.models.reverse.BoundaryElement;
import ovh.astarivi.gis.remote.models.reverse.ReverseElement;
import ovh.astarivi.utils.Data;

import java.io.IOException;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.TreeSet;


public class GISReverseGeocoder {
    private static final String query = "way(around:%d,%f,%f)[!building][highway~\"motorway|trunk|primary|secondary|tertiary|residential|unclassified\"];out geom qt;";
    private static final String queryBoundaries = "is_in(%f,%f);area._[admin_level][type=boundary][boundary=administrative];out;";

    // Gets the element by searching for the closest node
    public static ReverseElement getClosestElement(@NotNull GISPoint2D location) throws PrematureStopException {
        OverpassResponse<ReverseElement> overpassResponse;

        try {
            overpassResponse = Overpass.executeQuery(
                    query.formatted(
                            Data.getInstance().getSettings().reverseGeocoderDistance,
                            location.latitude().value(),
                            location.longitude().value()
                    ),
                    ReverseElement.class
            );
        } catch (BadRequestException | IOException e) {
            Logger.info("Dropping last request for location {} due to last issue", location);
            throw new PrematureStopException(e);
        }

        if (overpassResponse.elements.length == 0) throw new PrematureStopException("No results for these coordinates");
        if (overpassResponse.elements.length == 1) return overpassResponse.elements[0];

        ReverseElement closestSoFar = null;
        Double distance = null;

        for (ReverseElement element : overpassResponse.elements) {
            for (TreeMap<String, Double> coordinates : element.geometry) {
                double currentDistance = location.haversineDistance(
                        new GISPoint2D(
                                coordinates.get("lat"),
                                coordinates.get("lon")
                        )
                );

                if (distance == null || currentDistance < distance) {
                    distance = currentDistance;
                    closestSoFar = element;
                }
            }
        }

        if (closestSoFar == null)
            throw new IllegalStateException("Empty ReverseElement at ReverseGeocoder for " + location);

        return closestSoFar;
    }

    // Gets the element by searching the closest segment
    public static ReverseElement getClosestElementAccurate(@NotNull GISPoint2D location) throws PrematureStopException {
        OverpassResponse<ReverseElement> overpassResponse;

        try {
            overpassResponse = Overpass.executeQuery(
                    query.formatted(
                            Data.getInstance().getSettings().reverseGeocoderDistance,
                            location.latitude().value(),
                            location.longitude().value()
                    ),
                    ReverseElement.class
            );
        } catch (BadRequestException | IOException e) {
            Logger.info("Dropping last request for location {} due to last issue", location);
            throw new PrematureStopException(e);
        }

        if (overpassResponse.elements.length == 0) throw new PrematureStopException("No results for these coordinates");
        if (overpassResponse.elements.length == 1) return overpassResponse.elements[0];

        ReverseElement closestSoFar = null;
        Double distance = null;

        for (ReverseElement element : overpassResponse.elements) {
            if (element.geometry.length == 1) {
                continue;
            }

            for (int i = 0 ; i != element.geometry.length - 1 ; i++) {
                TreeMap<String, Double> firstPart = element.geometry[i];
                TreeMap<String, Double> secondPart = element.geometry[i + 1];

                double distanceToSegment = new GISSegment2D(
                        new GISPoint2D(
                                firstPart.get("lat"),
                                firstPart.get("lon")
                        ),
                        new GISPoint2D(
                                secondPart.get("lat"),
                                secondPart.get("lon")
                        )
                ).calculateProjectionDistanceOn(
                        location
                );

                if (distance == null || distanceToSegment < distance) {
                    distance = distanceToSegment;
                    closestSoFar = element;
                }
            }
        }

        if (closestSoFar == null)
            throw new IllegalStateException("Empty ReverseElement at ReverseGeocoder for " + location);

        return closestSoFar;
    }

    public static GISBoundaries getBoundaries(@NotNull GISPoint2D location) throws PrematureStopException {
        OverpassResponse<BoundaryElement> overpassResponse;

        try {
            overpassResponse = Overpass.executeQuery(
                    queryBoundaries.formatted(
                            location.latitude().value(),
                            location.longitude().value()
                    ),
                    BoundaryElement.class
            );
        } catch (BadRequestException | IOException e) {
            Logger.info("Dropping last request for location {} due to last issue", location);
            throw new PrematureStopException(e);
        }

        if (overpassResponse.elements.length == 0) throw new PrematureStopException("No boundaries for this set of data");

        return new GISBoundaries(new TreeSet<>(Arrays.asList(overpassResponse.elements)));
    }
}
