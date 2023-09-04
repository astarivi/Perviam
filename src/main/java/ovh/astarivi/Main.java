package ovh.astarivi;

import ovh.astarivi.gis.GISIntersectionFinder;
import ovh.astarivi.gis.GISLandmarksManager;
import ovh.astarivi.gis.GISReverseGeocoder;
import ovh.astarivi.gis.models.GISPoint2D;
import ovh.astarivi.gis.remote.exceptions.PrematureStopException;


public class Main {
    public static void main(String[] args) throws PrematureStopException {

        GISPoint2D testingPoint = new GISPoint2D(47.45975,-0.55251);

        System.out.println(
            GISLandmarksManager.getInstance().getClosestLandmarkTo(
                    testingPoint
            )
        );

        System.out.println(
                GISIntersectionFinder.getClosestIntersections(
                    testingPoint,
                    GISReverseGeocoder.getClosestElementAccurate(
                            testingPoint
                    )
                )
        );
    }
}