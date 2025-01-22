package ovh.astarivi.perviam;

import io.micronaut.runtime.Micronaut;
import org.tinylog.Logger;
import ovh.astarivi.perviam.gis.GISLandmarksManager;
import ovh.astarivi.perviam.utils.Data;

public class Application {

    public static void main(String[] args) {
        Logger.info("...Setting up...");
        Data.getInstance();
        GISLandmarksManager.getInstance();

        Micronaut.run(Application.class, args);
    }
}