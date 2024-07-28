package ovh.astarivi.perviam.i18n;

import org.tinylog.Logger;
import ovh.astarivi.perviam.utils.Settings;

import java.util.Locale;
import java.util.ResourceBundle;


public class I18N {
    public ResourceBundle bundle;

    public I18N(Settings settings) {
        String locale = System.getenv().getOrDefault("LANGUAGE", settings.languageCode);

        Logger.info("Loading IETF BCP 47 language tag {}", locale);

        bundle = ResourceBundle.getBundle("Messages", Locale.forLanguageTag(locale));

        int size = bundle.keySet().size();

        if (size == 0) {
            Logger.error("No strings have been loaded for tag {}", locale);
            Logger.error("Is the tag correct?. Terminating now...");

            System.exit(1);
        }

        Logger.info("Loaded {} strings for {} tag", size, locale);
    }

    public String getString(String key) {
        return bundle.getString(key);
    }
}
