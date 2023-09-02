package ovh.astarivi.gis.models;

import com.fasterxml.jackson.annotation.JsonIgnore;


public record GISCoordinate(double value, double valueRad) {
    @JsonIgnore
    public GISCoordinate(double value){
        this(value, Math.toRadians(value));
    }

    @Override
    public String toString() {
        return "GISCoordinate{" +
                "value=" + value +
                ", valueRad=" + valueRad +
                '}';
    }
}
