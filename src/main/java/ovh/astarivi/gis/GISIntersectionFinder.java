package ovh.astarivi.gis;

import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;
import ovh.astarivi.gis.models.GISPoint2D;
import ovh.astarivi.gis.models.GISSegment2D;
import ovh.astarivi.gis.remote.Overpass;
import ovh.astarivi.gis.remote.exceptions.BadRequestException;
import ovh.astarivi.gis.remote.exceptions.PrematureStopException;
import ovh.astarivi.gis.remote.models.OverpassResponse;
import ovh.astarivi.gis.remote.models.intersection.IntersectionElement;
import ovh.astarivi.gis.remote.models.reverse.ReverseElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;


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

    public static @NotNull TreeSet<IntersectionElement> getClosestIntersections(
            GISPoint2D userPosition,
            ReverseElement mainWay
    ) throws PrematureStopException {
        OverpassResponse<IntersectionElement> response;

        try {
            response = Overpass.executeQuery(
                    query.formatted(
                            mainWay.id
                    ),
                    IntersectionElement.class
            );
        } catch (BadRequestException | IOException e) {
            Logger.info("Dropping last request for intersection element {} due to last issue", mainWay);
            throw new PrematureStopException(e);
        }

        if (response.elements.length == 0) return new TreeSet<>();

        ArrayList<IntersectionElement> waysList = new ArrayList<>();
        ArrayList<IntersectionElement> nodesList = new ArrayList<>();

        // Segregate ways and nodes into lists
        for (IntersectionElement interElement : response.elements) {
            if (interElement.type.equals("way") && interElement.id != mainWay.id) {
                waysList.add(interElement);
            } else if (interElement.type.equals("node")) {
                nodesList.add(interElement);
            }
        }

        IntersectionElement lastNode = null;
        IntersectionElement[] closestSegment = null;
        Double distance = null;

        // Get the closest segment (made by intersections) to the user
        for (long mainWayNode : mainWay.nodes) {
            for (IntersectionElement intersectionNode : nodesList) {
                if (intersectionNode.id != mainWayNode) continue;

                if (lastNode == null) {
                    lastNode = intersectionNode;
                    continue;
                }

                double distanceToSegment = new GISSegment2D(
                        new GISPoint2D(
                                lastNode.lat,
                                lastNode.lon
                        ),
                        new GISPoint2D(
                                intersectionNode.lat,
                                intersectionNode.lon
                        )
                ).calculateProjectionDistanceOn(
                        userPosition
                );

                if (distance == null || distanceToSegment < distance) {
                    distance = distanceToSegment;
                    closestSegment = new IntersectionElement[]{
                            lastNode,
                            intersectionNode
                    };
                }

                lastNode = intersectionNode;
            }
        }

        if (distance == null) return new TreeSet<>();

        TreeSet<IntersectionElement> results = new TreeSet<>();

        // Filter out remaining parts of the main way
        for (IntersectionElement currentWay : waysList) {
            for (long wayNode : currentWay.nodes) {
                if ((wayNode != closestSegment[0].id && wayNode != closestSegment[1].id)
                        || mainWay.id == currentWay.id) continue;

                String currentWayName = currentWay.tags.get("name");

                if (currentWayName == null && !currentWay.tags.containsKey("ref")) continue;

                try {
                    if (mainWay.tags.get("name").equals(currentWayName)) continue;
                } catch (NullPointerException ignored) {
                    continue;
                }

                try {
                    if (mainWay.tags.get("ref").equals(currentWay.tags.get("ref"))) continue;
                } catch (NullPointerException ignored) {
                }

                results.add(currentWay);
            }
        }

        return results;
    }
}
