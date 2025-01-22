package ovh.astarivi.perviam.gis.remote.models.reverse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.NotNull;

import java.util.TreeMap;

import static java.lang.Integer.parseInt;


@JsonIgnoreProperties(ignoreUnknown = true)
public class BoundaryElement implements Comparable<BoundaryElement> {
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

    @JsonIgnore
    @Override
    public int compareTo(@NotNull BoundaryElement o) {
        return parseInt(o.tags.get("admin_level")) - parseInt(tags.get("admin_level"));
    }
}
