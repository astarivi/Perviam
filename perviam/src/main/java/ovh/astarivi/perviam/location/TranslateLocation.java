package ovh.astarivi.perviam.location;

import org.tinylog.Logger;
import ovh.astarivi.perviam.gis.GISIntersectionFinder;
import ovh.astarivi.perviam.gis.GISLandmarksManager;
import ovh.astarivi.perviam.gis.GISReverseGeocoder;
import ovh.astarivi.perviam.gis.models.GISBoundaries;
import ovh.astarivi.perviam.gis.models.GISPoint2D;
import ovh.astarivi.perviam.gis.remote.exceptions.PrematureStopException;
import ovh.astarivi.perviam.gis.remote.models.intersection.IntersectionElement;
import ovh.astarivi.perviam.gis.remote.models.reverse.ReverseElement;
import ovh.astarivi.perviam.utils.Utils;

import java.text.MessageFormat;
import java.util.TreeSet;

import static ovh.astarivi.perviam.i18n.Translation.*;


public class TranslateLocation {
    public static String get(GISPoint2D location) {
        try {
            GISLandmarksManager.ClosestLandmark closestLandmark = GISLandmarksManager
                    .getInstance()
                    .getClosestLandmarkTo(
                            location
                    );


            GISBoundaries gisBoundaries = GISReverseGeocoder.getBoundaries(
                    location
            );

            String boundaries = gisBoundaries.verboseBoundaries();

            ReverseElement closestElement = GISReverseGeocoder.getClosestElementAccurate(
                    location
            );

            return "%s, %s".formatted(
                    gisBoundaries.isCity() ? translateCityLocation(location, closestElement)
                            : translateRuralLocation(closestElement, closestLandmark),
                    boundaries
            );
        } catch (PrematureStopException ignored) {
        } catch (Exception e) {
            Logger.error("Error translating location {}", location);
            Logger.error(e);
        }

        return getI18nString("placeholder");
    }

    private static String translateRuralLocation(
            ReverseElement closestElement,
            GISLandmarksManager.ClosestLandmark closestLandmark
    ) throws PrematureStopException {
        String currentStreet = getLocalizedTag(closestElement.tags, "name");

        if (currentStreet == null) {
            currentStreet = closestElement.tags.get("ref");

            if (currentStreet == null) {
                currentStreet = getI18nString("unknown_street");
            } else {
                currentStreet = MessageFormat.format(
                        getI18nString("street_ref_prefix"),
                        currentStreet
                );
            }
        }

        if (Utils.isMostlyNumeric(currentStreet)) {
            currentStreet = MessageFormat.format(
                    getI18nString("street_ref_prefix"),
                    currentStreet
            );
        }

        return MessageFormat.format(
                getI18nString("rural_address"),
                currentStreet,
                closestLandmark.verboseDistance(),
                closestLandmark.landmark().label()
        );
    }

    private static String translateCityLocation(
            GISPoint2D location,
            ReverseElement closestElement
    ) throws PrematureStopException {
        TreeSet<IntersectionElement> intersections = GISIntersectionFinder.getClosestIntersections(location, closestElement);

        String currentStreet = getLocalizedTag(closestElement.tags, "name");

        if (currentStreet == null) {
            currentStreet = closestElement.tags.get("ref");
            if (currentStreet == null) {
                currentStreet = getI18nString("unknown_street");
            } else {
                currentStreet = MessageFormat.format(
                        getI18nString("street_ref_prefix"),
                        currentStreet
                );
            }
        }

        return switch (intersections.size()) {
            case 0 -> MessageFormat.format(
                    getI18nString("address_lone"),
                    currentStreet
            );
            case 1 -> MessageFormat.format(
                    getI18nString("address_between_one"),
                    currentStreet,
                    getNameForWay(intersections.first().tags)
            );
            default -> MessageFormat.format(
                    getI18nString("address_between_two"),
                    currentStreet,
                    getNameForWay(intersections.first().tags),
                    getNameForWay(intersections.last().tags)
            );
        };
    }
}
