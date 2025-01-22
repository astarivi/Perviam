package ovh.astarivi.perviam.gis.models;

import org.jetbrains.annotations.NotNull;
import ovh.astarivi.perviam.gis.remote.exceptions.PrematureStopException;
import ovh.astarivi.perviam.gis.remote.models.reverse.BoundaryElement;
import ovh.astarivi.perviam.i18n.Translation;
import ovh.astarivi.perviam.utils.Data;

import java.util.List;
import java.util.TreeSet;


public record GISBoundaries(TreeSet<BoundaryElement> boundaries, boolean isCity) {
    public @NotNull String verboseBoundaries() throws PrematureStopException {
        StringBuilder verboseBoundaries = new StringBuilder();

        List<Integer> acceptedBoundaries = isCity ? Data.getInstance().getSettings().cityAdminLevels :
                Data.getInstance().getSettings().ruralAdminLevels;

        for (BoundaryElement boundaryElement : boundaries) {
            if (!boundaryElement.type.equals("area")) continue;

            int currentAdminLevel = Integer.parseInt(boundaryElement.tags.get("admin_level"));

            if (!acceptedBoundaries.contains(currentAdminLevel)) continue;

            verboseBoundaries.append(Translation.getLocalizedTag(boundaryElement.tags, "name")).append(", ");
        }

        verboseBoundaries.delete(verboseBoundaries.length() - 2, verboseBoundaries.length());

        return verboseBoundaries.toString();
    }
}
