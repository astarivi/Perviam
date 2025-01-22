package ovh.astarivi.perviam.gis.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import static java.lang.Math.sqrt;


public record GISSegment2D(GISPoint2D start, GISPoint2D end) {
    @JsonIgnore
    public double calculateProjectionDistanceOn(GISPoint2D point) {
        double lat1 = start.latitude().valueRad();
        double lon1 = start.longitude().valueRad();
        double lat2 = end.latitude().valueRad();
        double lon2 = end.longitude().valueRad();
        double lat = point.latitude().valueRad();
        double lon = point.longitude().valueRad();

        double dx = lat2 - lat1;
        double dy = lon2 - lon1;
        double dxp = lat - lat1;
        double dyp = lon - lon1;

        double dotProduct = dx * dxp + dy * dyp;
        double squaredLength = dx * dx + dy * dy;

        double t = dotProduct / squaredLength;

        if (t < 0) {
            return sqrt(squaredLength);
        } else if (t > 1) {
            double dxE = lat - lat2;
            double dyE = lon - lon2;
            return sqrt(dxE * dxE + dyE * dyE);
        } else {
            double projectionLat = lat1 + t * dx;
            double projectionLon = lon1 + t * dy;
            double dxE = lat - projectionLat;
            double dyE = lon - projectionLon;
            return sqrt(dxE * dxE + dyE * dyE);
        }
    }

    @Override
    public String toString() {
        return "GISSegment2D{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
