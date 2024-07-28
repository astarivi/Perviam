package ovh.astarivi.perviam.gis.remote.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class OverpassResponse<E> {
    public Float version;
    public String generator;
    public OSM3S osm3s;
    public E[] elements;

    public static class OSM3S {
        public String timestamp_osm_base;
        public String timestamp_areas_base;
        public String copyright;
    }
}
