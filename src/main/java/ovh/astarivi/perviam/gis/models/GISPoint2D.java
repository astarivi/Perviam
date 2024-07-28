package ovh.astarivi.perviam.gis.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import static java.lang.Math.*;


public record GISPoint2D (GISCoordinate latitude, GISCoordinate longitude){
    @JsonIgnore
    public GISPoint2D(double latitude, double longitude) {
        this(
                new GISCoordinate(latitude),
                new GISCoordinate(longitude)
        );
    }

    @JsonIgnore
    public double haversineDistance(GISPoint2D to) {
        double fromLatitudeRad = latitude.valueRad();
        double fromLongitudeRad = longitude.valueRad();
        double toLatitudeRad = to.latitude.valueRad();
        double toLongitudeRad = to.longitude.valueRad();

        double hav =
                0.5 - cos(toLatitudeRad-fromLatitudeRad) / 2
                        + cos(fromLatitudeRad) * cos(toLatitudeRad)
                        * (1-cos(toLongitudeRad - fromLongitudeRad)) / 2;

        // Convert to km
        return 12742 * asin(sqrt(hav));
    }

    public String toString() {
        return "GISPoint2D["+latitude+", "+longitude+"]";
    }
}
