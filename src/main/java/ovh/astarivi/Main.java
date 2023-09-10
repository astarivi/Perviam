package ovh.astarivi;

import ovh.astarivi.gis.models.GISPoint2D;
import ovh.astarivi.location.TranslateLocation;


public class Main {
    public static void main(String[] arg) {
        GISPoint2D testingPoint = new GISPoint2D(46.4447,-0.0375);

        String location = TranslateLocation.get(
                testingPoint
        );

        System.out.println(
                location
        );
    }
}