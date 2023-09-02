package ovh.astarivi.gis.remote.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.TreeMap;


@JsonIgnoreProperties(ignoreUnknown = true)
public class OverpassElement {
    public String type;
    public long id;
    public double lat;
    public double lon;
    public TreeMap<String, String> tags;
}
