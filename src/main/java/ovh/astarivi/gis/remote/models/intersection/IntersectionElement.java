package ovh.astarivi.gis.remote.models.intersection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.TreeMap;


@JsonIgnoreProperties(ignoreUnknown = true)
public class IntersectionElement implements Comparable<IntersectionElement>{
    public String type;
    public long id;
    public Double lat;
    public Double lon;
    public List<Long> nodes;
    public TreeMap<String, String> tags;

    @Override @JsonIgnore
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

    @Override @JsonIgnore
    public int compareTo(@NotNull IntersectionElement o) {
        return tags.get("name").compareTo(o.tags.get("name"));
    }
}
