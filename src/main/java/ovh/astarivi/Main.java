package ovh.astarivi;

import ovh.astarivi.gis.GISIntersectionFinder;
import ovh.astarivi.gis.GISLandmarksManager;
import ovh.astarivi.gis.GISReverseGeocoder;
import ovh.astarivi.gis.models.GISPoint2D;
import ovh.astarivi.gis.remote.exceptions.PrematureStopException;
import ovh.astarivi.gis.remote.models.reverse.BoundaryElement;
import ovh.astarivi.location.TranslateLocation;

import java.util.TreeSet;


public class Main {
    public static void main(String[] args) throws PrematureStopException {

        GISPoint2D testingPoint = new GISPoint2D(47.759177519676484, 1.326297387571581);

        System.out.println(
            GISLandmarksManager.getInstance().getClosestLandmarkTo(
                    testingPoint
            )
        );

        TreeSet<BoundaryElement> boundaries = GISReverseGeocoder.getBoundaries(testingPoint);

        System.out.println(
                boundaries
        );

        System.out.println(
                GISIntersectionFinder.getClosestIntersections(
                    testingPoint,
                    GISReverseGeocoder.getClosestElementAccurate(
                            testingPoint
                    )
                )
        );

        String location = TranslateLocation.get(
                testingPoint
        );

        System.out.println(
                location
        );
    }
}