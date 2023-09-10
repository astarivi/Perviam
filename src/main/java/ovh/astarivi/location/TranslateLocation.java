package ovh.astarivi.location;

import org.jetbrains.annotations.NotNull;
import org.tinylog.Logger;
import ovh.astarivi.gis.GISIntersectionFinder;
import ovh.astarivi.gis.GISReverseGeocoder;
import ovh.astarivi.gis.models.GISPoint2D;
import ovh.astarivi.gis.remote.exceptions.PrematureStopException;
import ovh.astarivi.gis.remote.models.intersection.IntersectionElement;
import ovh.astarivi.gis.remote.models.reverse.BoundaryElement;
import ovh.astarivi.gis.remote.models.reverse.ReverseElement;
import ovh.astarivi.i18n.Translation;
import ovh.astarivi.utils.Data;

import java.text.MessageFormat;
import java.util.TreeSet;

import static ovh.astarivi.i18n.Translation.getI18nString;
import static ovh.astarivi.i18n.Translation.getLocalizedTag;


public class TranslateLocation {
    public static String get(GISPoint2D location) {
        try {
            String boundaries = getBoundaries(location);

            ReverseElement closestElement = GISReverseGeocoder.getClosestElementAccurate(
                    location
            );

            return "%s, %s".formatted(
                    translateCityLocation(location, closestElement),
                    boundaries
            );
        } catch (PrematureStopException ignored) {
        } catch (Exception e) {
            Logger.error("Error translating location {}", location);
            Logger.error(e);
        }

        return getI18nString("placeholder");
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

    private static @NotNull String getBoundaries(GISPoint2D location) throws PrematureStopException {
        TreeSet<BoundaryElement> boundaryElements = GISReverseGeocoder.getBoundaries(location);

        StringBuilder boundaries = new StringBuilder();

        for (BoundaryElement boundaryElement : boundaryElements) {
            int currentAdminLevel = Integer.parseInt(boundaryElement.tags.get("admin_level"));

            if (!Data.getInstance().getSettings().cityAdminLevels.contains(currentAdminLevel)) continue;

            boundaries.append(Translation.getLocalizedTag(boundaryElement.tags, "name")).append(", ");
        }

        boundaries.delete(boundaries.length() - 2, boundaries.length());

        return boundaries.toString();
    }
}
