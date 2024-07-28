package ovh.astarivi.perviam.i18n;

import org.jetbrains.annotations.Nullable;
import ovh.astarivi.perviam.utils.Data;

import java.util.TreeMap;


public class Translation {
    public static final String LANGUAGE_CODE = Data.getInstance().getSettings().languageCode.toLowerCase();

    public static String getI18nString(String key) {
        return Data.getInstance().getI18n().getString(key);
    }

    public static @Nullable String getLocalizedTag(TreeMap<String, String> tags, String key) {
        String i18nTag = tags.get(
                String.format(
                        "%s:%s",
                        key,
                        LANGUAGE_CODE
                )
        );

        if (i18nTag != null) return i18nTag;

        return tags.get(
                key
        );
    }

    public static @Nullable String getNameForWay(TreeMap<String, String> tags) {
        String result = getLocalizedTag(tags, "name");

        if (result != null) return result;

        return tags.get(
                "ref"
        );
    }
}
