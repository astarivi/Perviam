package ovh.astarivi;

import ovh.astarivi.gis.models.GISPoint2D;
import ovh.astarivi.location.TranslateLocation;


public class Main {
    public static void main(String[] arg) {
        GISPoint2D testingPoint = new GISPoint2D(45.69213561820808, 4.864009724598776);

        String location = TranslateLocation.get(
                testingPoint
        );

        System.out.println(
                location
        );
    }
}