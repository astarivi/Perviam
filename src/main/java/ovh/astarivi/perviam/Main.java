package ovh.astarivi.perviam;

import ovh.astarivi.perviam.gis.models.GISPoint2D;
import ovh.astarivi.perviam.location.TranslateLocation;


public class Main {
    public static void main(String[] arg) {
        GISPoint2D testingPoint = new GISPoint2D(4.67289, -74.10969);

        String location = TranslateLocation.get(
                testingPoint
        );

        System.out.println(
                location
        );
    }
}