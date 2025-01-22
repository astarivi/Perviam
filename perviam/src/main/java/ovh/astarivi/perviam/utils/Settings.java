package ovh.astarivi.perviam.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Settings {
    public String overpassUrl;
    public List<String> landmarks;
    public String country;
    public Integer remoteTimeout;
    public Integer reverseGeocoderDistance;
    public String languageCode;
    public List<Integer> cityAdminLevels;
    public List<Integer> ruralAdminLevels;
    public int adminLevelCity;

    // Used by Jackson
    public Settings() {

    }

    // Used to initialize defaults
    @JsonIgnore
    public Settings(int i) {
        landmarks = List.of("city");
        country = "CO";
        overpassUrl = "https://overpass-api.de/api/interpreter";
        remoteTimeout = 60;
        reverseGeocoderDistance = 50;
        languageCode = "es";
        cityAdminLevels = Arrays.asList(2, 4, 8, 10);
        ruralAdminLevels = Arrays.asList(2, 4, 6, 8, 10);
        adminLevelCity = 7;

        initialize();
    }

    private void initialize() {
        cityAdminLevels.sort(Collections.reverseOrder());
        ruralAdminLevels.sort(Collections.reverseOrder());
    }

    private boolean isHealthy() {
        Field[] fields = Settings.class.getFields();

        for (Field field : fields){
            try {
                if (field.get(this) == null) throw new IllegalStateException("Missing field at settings");
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (IllegalStateException e) {
                Logger.warn("Loaded settings key {} was missing, or invalid.", field.getName());
                return false;
            }
        }

        return true;
    }

    @JsonIgnore
    public static Settings load() {
        Path storageFolder = Path.of(Utils.assureEnv("STORAGE_FOLDER"));

        if (Files.notExists(storageFolder)) {
            try {
                Files.createDirectories(storageFolder);
            } catch (IOException e) {
                Logger.error("Error while trying to create STORAGE_FOLDER at {}", storageFolder.toString());
                Logger.error(e);

                System.exit(1);
            }
        }

        File settingsFile = new File(
                storageFolder.toFile(),
                "settings.json"
        );

        try {
            if (settingsFile.exists()) {
                Settings loadedSettings = Serialization.getObjectReader().readValue(settingsFile, Settings.class);

                loadedSettings.initialize();

                if (loadedSettings.isHealthy()) return loadedSettings;

                Logger.warn("Settings loaded from {} are unhealthy, falling back to defaults...", settingsFile.getPath());

                return new Settings(1);
            } else {
                Settings defaultSettings = new Settings(1);

                Logger.info("Writing settings file...");
                Serialization.getObjectWriter().withDefaultPrettyPrinter().writeValue(settingsFile, defaultSettings);

                return defaultSettings;
            }
        } catch(SecurityException | IOException e) {
            Logger.error("Couldn't read or write to settings file {}.", settingsFile);
            Logger.error("This could happen if the settings file is corrupted, or badly formatted. It must contain " +
                    "valid JSON, and the right keys. Delete the file if unsure, but back it up first.");
            Logger.error(e);
            throw new RuntimeException(e);
        }
    }
}
