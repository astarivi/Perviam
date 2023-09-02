package ovh.astarivi;

import ovh.astarivi.gis.GISLandmarksManager;
import ovh.astarivi.gis.models.GISPoint2D;


public class Main {
    public static void main(String[] args) {
        System.out.println(
            GISLandmarksManager.getInstance().getClosestLandmarkTo(
                    new GISPoint2D(
                            46.21571D,
                            2.03529D
                    )
            )
        );
    }
}