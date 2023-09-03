package ovh.astarivi.gis.remote.models.intersection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.TreeMap;


@JsonIgnoreProperties(ignoreUnknown = true)
public class IntersectionElement {
    public String type;
    public long id;
    public Double lat;
    public Double lon;
    public List<Long> nodes;
    public TreeMap<String, String> tags;

    @JsonIgnore
    @Override
    public String toString() {
        return "IntersectionElement{" +
                "type='" + type + '\'' +
                ", id=" + id +
                ", lat=" + lat +
                ", lon=" + lon +
                ", nodes=" + nodes +
                ", tags=" + tags +
                '}';
    }
}
