package ovh.astarivi;

import ovh.astarivi.gis.GISLandmarksManager;
import ovh.astarivi.gis.GISReverseGeocoder;
import ovh.astarivi.gis.models.GISPoint2D;
import ovh.astarivi.gis.remote.exceptions.PrematureStopException;


public class Main {
    public static void main(String[] args) throws PrematureStopException {
        System.out.println(
            GISLandmarksManager.getInstance().getClosestLandmarkTo(
                    new GISPoint2D(
                            46.21571D,
                            2.03529D
                    )
            )
        );

        System.out.println(
                GISReverseGeocoder.getClosestElementAccurate(
                        new GISPoint2D(4.59921,-74.18845)
                )
        );
    }
}