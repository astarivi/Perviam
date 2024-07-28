package ovh.astarivi.perviam.gis.remote.models.landmarks;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.TreeMap;


@JsonIgnoreProperties(ignoreUnknown = true)
public class LandmarksElement {
    public String type;
    public long id;
    public double lat;
    public double lon;
    public TreeMap<String, String> tags;
}
