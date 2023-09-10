package ovh.astarivi.i18n;

import org.jetbrains.annotations.Nullable;
import ovh.astarivi.utils.Data;

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

        i18nTag = tags.get(
                key
        );

        return i18nTag;
    }
}
