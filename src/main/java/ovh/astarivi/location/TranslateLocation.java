package ovh.astarivi.location;

import org.tinylog.Logger;
import ovh.astarivi.gis.GISIntersectionFinder;
import ovh.astarivi.gis.GISLandmarksManager;
import ovh.astarivi.gis.GISReverseGeocoder;
import ovh.astarivi.gis.models.GISPoint2D;
import ovh.astarivi.gis.remote.exceptions.PrematureStopException;
import ovh.astarivi.gis.remote.models.intersection.IntersectionElement;
import ovh.astarivi.gis.remote.models.reverse.ReverseElement;
import ovh.astarivi.utils.Data;
import ovh.astarivi.utils.Utils;

import java.text.MessageFormat;
import java.util.TreeSet;

import static ovh.astarivi.i18n.Translation.getI18nString;
import static ovh.astarivi.i18n.Translation.getLocalizedTag;


public class TranslateLocation {
    private static final double LANDMARK_AREA = Data.getInstance().getSettings().landmarkAreaKm;

    public static String get(GISPoint2D location) {
        try {
            GISLandmarksManager.ClosestLandmark closestLandmark = GISLandmarksManager
                    .getInstance()
                    .getClosestLandmarkTo(
                            location
                    );

            boolean isInCity = isInCity(closestLandmark);

            String boundaries = GISReverseGeocoder
                    .getBoundaries(
                            location
                    ).verboseBoundaries(
                            isInCity
                    );

            ReverseElement closestElement = GISReverseGeocoder.getClosestElement(
                    location
            );

            return "%s, %s".formatted(
                    isInCity ? translateCityLocation(location, closestElement)
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

    private static boolean isInCity(GISLandmarksManager.ClosestLandmark closestLandmark) {
        return closestLandmark.distanceKm() < LANDMARK_AREA;
    }

    private static String translateRuralLocation(
            ReverseElement closestElement,
            GISLandmarksManager.ClosestLandmark closestLandmark
    ) throws PrematureStopException {
        String currentStreet = getLocalizedTag(closestElement.tags, "name");

        if (currentStreet == null) {
            currentStreet = closestElement.tags.get("ref");
            if (currentStreet == null) throw new PrematureStopException("No name for this rural route");

            currentStreet = MessageFormat.format(
                    getI18nString("street_ref_prefix"),
                    currentStreet
            );
        }

        if (Utils.isMostlyNumeric(currentStreet)) {
            currentStreet = MessageFormat.format(
                    getI18nString("street_ref_prefix"),
                    currentStreet
            );
        }

        return currentStreet;
    }

    private static String translateCityLocation(
            GISPoint2D location,
            ReverseElement closestElement
    ) throws PrematureStopException {
        TreeSet<IntersectionElement> intersections = GISIntersectionFinder.getClosestIntersections(location, closestElement);

        String currentStreet = getLocalizedTag(closestElement.tags, "name");

        if (currentStreet == null) {
            currentStreet = closestElement.tags.get("ref");
            if (currentStreet == null) throw new PrematureStopException("No name for this main street");

            currentStreet = MessageFormat.format(
                    getI18nString("street_ref_prefix"),
                    currentStreet
            );
        }

        String mainStreet;

        switch (intersections.size()) {
            case 0:
                mainStreet = MessageFormat.format(
                        getI18nString("address_lone"),
                        currentStreet
                );
                break;
            case 1:
                mainStreet = MessageFormat.format(
                        getI18nString("address_between_one"),
                        currentStreet,
                        getLocalizedTag(intersections.first().tags, "name")
                );
            default:
            case 2:
                mainStreet = MessageFormat.format(
                        getI18nString("address_between_two"),
                        currentStreet,
                        getLocalizedTag(intersections.first().tags, "name"),
                        getLocalizedTag(intersections.last().tags, "name")
                );
        }

        return mainStreet;
    }
}
