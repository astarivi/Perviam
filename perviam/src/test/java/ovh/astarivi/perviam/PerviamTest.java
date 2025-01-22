package ovh.astarivi.perviam;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tinylog.Logger;
import ovh.astarivi.perviam.gis.GISLandmarksManager;
import ovh.astarivi.perviam.gis.models.GISPoint2D;
import ovh.astarivi.perviam.location.TranslateLocation;
import ovh.astarivi.perviam.utils.Data;


public class PerviamTest {
    @BeforeAll
    static void setup() {

        GISLandmarksManager.getInstance();
        Data.getInstance();
    }

    @Test
    @DisplayName("CO city address")
    void cityTest() {
        Logger.info(
                TranslateLocation.get(
                        new GISPoint2D(4.67289, -74.10969)
                )
        );
    }


}