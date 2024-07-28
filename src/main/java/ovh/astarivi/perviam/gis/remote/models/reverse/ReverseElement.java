package ovh.astarivi.perviam.gis.remote.models.reverse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;
import java.util.TreeMap;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ReverseElement {
    public String type;
    public long id;
    public long[] nodes;
    public TreeMap<String, Double> bounds;
    public TreeMap<String, Double>[] geometry;
    public TreeMap<String, String> tags;

    @JsonIgnore
    @Override
    public String toString() {
        return "ReverseElement{" +
                "type='" + type + '\'' +
                ", id=" + id +
                ", nodes=" + Arrays.toString(nodes) +
                ", bounds=" + bounds +
                ", geometry=" + Arrays.toString(geometry) +
                ", tags=" + tags +
                '}';
    }
}
