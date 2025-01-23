package ovh.astarivi.perviam.utils;

import org.tinylog.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class Utils {
    public static String assureEnv(String key) {
        String value = null;
        try {
            value = System.getenv(key);
        } catch(SecurityException e) {
            Logger.error("Error while trying to get mandatory ENV key {}.", key);
            Logger.error(e);

            System.exit(1);
        }

        if (value == null) {
            Logger.error("Mandatory ENV key {} was not declared.", key);
            System.exit(1);
        }

        return value;
    }

    public static Path getConfigurationFolder() {
        String value = null;
        try {
            value = System.getenv("STORAGE_FOLDER");
        } catch(SecurityException e) {
            Logger.error("Error while trying to get mandatory ENV key STORAGE_FOLDER.");
            Logger.error(e);

            System.exit(1);
        }

        Path storageFolder;
        if (value != null) {
            storageFolder = Path.of(value);
        } else {
            Path currentFolder = Path.of(System.getProperty("user.dir"));
            storageFolder = currentFolder.resolve("config");
        }

        if (Files.notExists(storageFolder)) {
            try {
                Files.createDirectories(storageFolder);
            } catch (IOException e) {
                Logger.error("Error while trying to create STORAGE_FOLDER at {}", storageFolder.toString());
                Logger.error(e);

                System.exit(1);
            }
        }

        return storageFolder;
    }

    public static boolean isMostlyNumeric(String s) {
        int count = 0;
        for(int i = 0;i < s.length(); i++) {
            if(Character.isDigit(s.charAt(i))) count++;
        }

        return count > (s.length() / 2);
    }
}
