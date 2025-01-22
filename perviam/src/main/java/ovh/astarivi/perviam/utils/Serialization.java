package ovh.astarivi.perviam.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Getter;


public class Serialization {
    @Getter
    private static final ObjectMapper mapper = new ObjectMapper();

    public static ObjectReader getObjectReader() {
        return mapper.reader();
    }

    public static ObjectWriter getObjectWriter() {
        return mapper.writer();
    }
}
