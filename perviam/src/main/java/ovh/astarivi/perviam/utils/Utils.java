package ovh.astarivi.perviam.utils;

import org.tinylog.Logger;


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

    public static boolean isMostlyNumeric(String s) {
        int count = 0;
        for(int i = 0;i < s.length(); i++) {
            if(Character.isDigit(s.charAt(i))) count++;
        }

        return count > (s.length() / 2);
    }
}
