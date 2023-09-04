package ovh.astarivi.gis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.tinylog.Logger;
import ovh.astarivi.gis.models.GISPoint2D;
import ovh.astarivi.gis.remote.Overpass;
import ovh.astarivi.gis.remote.models.landmarks.LandmarksElement;
import ovh.astarivi.gis.remote.models.OverpassResponse;
import ovh.astarivi.utils.Data;
import ovh.astarivi.utils.Serialization;
import ovh.astarivi.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;


public class GISLandmarksManager {
    private static volatile GISLandmarksManager _instance = null;
    private final File landmarksFile = new File(
            Utils.assureEnv("STORAGE_FOLDER"),
            "landmarks.json"
    );
    private final GISLandmark[] landmarks;

    private GISLandmarksManager() {
        if (landmarksFile.exists()) {
            landmarks = loadLandmarks();

            if (landmarks.length == 0) {
                 Logger.error("Loaded landmarks file is empty. Is it corrupt?. Consider removing this file if it is" +
                         "corrupt, or empty.");
                 System.exit(1);
            }
        } else {
            landmarks = generateLandmarks();
            saveLandmarks();
        }
    }

    private GISLandmark[] generateLandmarks() {
        String country = System.getenv().getOrDefault("COUNTRY", Data.getInstance().getSettings().country);

        Logger.info("Landmarks generation is about to start.");

        if (Data.getInstance().getSettings().landmarks.isEmpty()) {
            Logger.error("Landmarks settings is an empty list, and the Landmarks functionality was activated.");
            Logger.error("Check your settings.json file.");
            Logger.error("The app will now terminate.");
            System.exit(1);
        }

        StringBuilder formattedLandmarks = new StringBuilder();
        for (String landmark : Data.getInstance().getSettings().landmarks) {
            formattedLandmarks.append(String.format("node[\"place\"=\"%s\"](area.plimit);", landmark));
        }

        String overpassQuery =
            """
            area["ISO3166-1"="%s"]->.plimit;(%s);out;
            """.formatted(
                    country,
                    formattedLandmarks
            );

        OverpassResponse<LandmarksElement> response;

        try {
            response = Overpass.executeQuery(overpassQuery, LandmarksElement.class);
        } catch (Exception e) {
            Logger.error("LandmarksManager couldn't generate the Landmarks list due to the last error reported.");
            Logger.info("Note that landmark generation may require a higher timeout");
            Logger.error("As Landmarks are active, but they couldn't be generated, the app will now terminate...");
            throw new RuntimeException(e);
        }

        Logger.info("Landmarks generation will use remote generator {}", response.generator);

        if (response.elements.length == 0) {
            Logger.error("Landmarks couldn't be generated, as Overpass returned no information for this query.");
            Logger.error("Perhaps something is wrong with the given country, or the given landmarks list is invalid.");
            Logger.error("Query was: {}", overpassQuery);
            Logger.error("As Landmarks are active, but they couldn't be generated, the app will now terminate...");
            System.exit(1);
        }

        int count = 0;

        ArrayList<GISLandmark> landmarksList = new ArrayList<>();

        for (LandmarksElement element : response.elements) {
            try {
                landmarksList.add(
                        new GISLandmark(element.lat, element.lon, Objects.requireNonNull(element.tags.get("name")))
                );
            } catch (NullPointerException e) {
                continue;
            }

            count++;
        }

        if (landmarksList.isEmpty()) {
            Logger.error("No valid landmarks were found in this data set. Please try another one, or disable the " +
                    "Landmarks functionality altogether.");
            Logger.error("Query was: {}", overpassQuery);
            Logger.error("As Landmarks are active, but they couldn't be generated, the app will now terminate...");
            System.exit(1);
        }

        Logger.info("{} landmarks generated", count);

        return landmarksList.toArray(new GISLandmark[0]);
    }

    private GISLandmark[] loadLandmarks() {
        Logger.info("Loading landmarks from disk to memory");
        try {
            return Serialization.getObjectReader().readValue(landmarksFile, GISLandmark[].class);
        } catch (IOException e) {
            Logger.error("Error while reading file {}, it may be corrupt.", landmarksFile.getPath());
            Logger.error(e);
            throw new RuntimeException(e);
        }
    }

    private void saveLandmarks() {
        try {
            Serialization.getObjectWriter().writeValue(landmarksFile, landmarks);
        } catch (IOException e) {
            Logger.error("Error while saving landmarks file {}, there may be a permissions error.",
                    landmarksFile.getPath());
            Logger.error(e);
            throw new RuntimeException(e);
        }
    }

    public static GISLandmarksManager getInstance() {
        if (_instance == null) {
            synchronized (GISLandmarksManager.class) {
                if (_instance == null) _instance = new GISLandmarksManager();
            }
        }
        return _instance;
    }

    public ClosestLandmark getClosestLandmarkTo(GISPoint2D point) {
        GISLandmark closestSoFar = null;
        Double distance = null;

        for (GISLandmark landmark : landmarks) {
            double currentDistance = landmark.location.haversineDistance(point);

            if (distance == null || currentDistance < distance) {
                distance = currentDistance;
                closestSoFar = landmark;
            }
        }

        if (closestSoFar == null) throw new IllegalStateException("Landmarks manager list was empty.");

        return new ClosestLandmark(closestSoFar, distance);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record GISLandmark(GISPoint2D location, String label) {
        @JsonIgnore
        public GISLandmark(double latitude, double longitude, String name) {
            this(
                    new GISPoint2D(latitude, longitude),
                    name
            );
        }
    }

    public record ClosestLandmark(GISLandmark landmark, double distanceKm) {

    }
}
