package ovh.astarivi.gis.remote.models.reverse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.TreeMap;


@JsonIgnoreProperties(ignoreUnknown = true)
public class BoundaryElement {
    public String type;
    public long id;
    public TreeMap<String, String> tags;

    @JsonIgnore
    @Override
    public String toString() {
        return "BoundaryElement{" +
                "type='" + type + '\'' +
                ", id=" + id +
                ", tags=" + tags +
                '}';
    }
}
