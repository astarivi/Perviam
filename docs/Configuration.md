# Configuration
Perviam is built for flexibility, allowing most operational parameters to be customized using environment variables or configuration files.

## What is a Landmark?
In Perviam, all cities within the configured country are considered as Landmarks. On first run, Overpass will
be queried for these, and they will be stored at the `landmarks.json` file. These are used as positional references
for targets that are far away from urban areas.

# Environment Variables
Key configuration values are first sourced from the environment. If not found, they default to values specified in the configuration file. Below are the primary environment variables:

## `STORAGE_FOLDER`
- Defines the location of the storage folder. If the folder does not exist, it will be created.
- **Example**: `/home/perviam/config`
- **Default**: `./config`

## `LANGUAGE`
- Sets the response language. Use an [IETF BCP 47](https://en.wikipedia.org/wiki/IETF_language_tag) [two-letter code](https://en.wikipedia.org/wiki/ISO_639-1), or a more specific tag such as `fr-CA`.
- The selected translation must exist, and Perviam must be built with the corresponding translation file. Refer to the [Translation docs](Translation.md) for details on creating translations.
- **Example**: `en`

## `COUNTRY`
- Specifies the country to avoid generating unnecessary landmarks during the initial run. Use the [ISO3166-1](https://en.wikipedia.org/wiki/ISO_3166-1) tag format.
- **Example**: `US`

## `OVERPASS_URL`
- Overrides the default Overpass URL. Useful for testing or load balancing.
- **Example**: `https://overpass-api.de/api/interpreter`

## `REMOTE_TIMEOUT`
- Sets the timeout (in seconds) for Overpass API responses. Use a reasonable value to avoid hanging connections.
- **Example**: `60`

# Configuration Keys
Upon the first run, the `settings.json` file is created in the directory specified by `STORAGE_FOLDER`. If `STORAGE_FOLDER` is not set, a new `config` folder will be created in the current working directory to hold the file.

**Note**: To regenerate landmarks, manually delete `landmarks.json`.

## `overpassUrl`
- Defines the Overpass URL for queries.
- **Default**: `https://overpass-api.de/api/interpreter`

## `landmarks`
- Specifies items to consider as landmarks. Currently unused.
- **Default**: `["city"]`

## `country`
- Sets the instance's country. Only one country can be served per instance, and this value is used to generate landmarks.
- For multi-country setups, run multiple instances.
- Use the [ISO3166-1](https://en.wikipedia.org/wiki/ISO_3166-1) tag format.
- **Default**: `US`

## `remoteTimeout`
- Configures the Overpass API response timeout in seconds. Maintain a reasonable value to prevent hanging connections.
- **Default**: `60`

## `reverseGeocoderDistance`
- Adjusts the distance (in meters) used for reverse geocoding. This impacts result quality and Overpass API load:
  - Higher values resolve targets farther from roads but reduce accuracy.
  - Lower values improve accuracy but may fail to resolve distant targets.
- **Default**: `50`

## `languageCode`
- Sets the response language using an [IETF BCP 47](https://en.wikipedia.org/wiki/IETF_language_tag) code. Optionally use a more specific tag, e.g., `fr-CA`.
- The translation must exist, and Perviam must be built with the appropriate translation file. Refer to the [Translation docs](Translation.md) for details.
- **Default**: `en`

## `adminLevelCity`
- Specifies the OSM `admin_level` representing a "city" for the configured country.
- Refer to the [OSM wiki](https://wiki.openstreetmap.org/wiki/Tag:boundary%3Dadministrative) for admin level definitions.
- **Default**: `8` (for USA)

## `cityAdminLevels`
- Lists `admin_level`s to include in city query results. For example, setting `[8, 4, 2]` includes city, state, and country in the output:
  - **Example**: `Des Moines, Iowa, United States of America`
- Order is irrelevant; results are always sorted from most to least specific.
- Missing `admin_level`s are skipped.
- **Default**: `[2, 4, 8, 10]` (for USA)

## `ruralAdminLevels`
- Similar to `cityAdminLevels`, but used for queries outside city boundaries. This allows fine-tuning of rural address details.
- Refer to the [OSM wiki](https://wiki.openstreetmap.org/wiki/Tag:boundary%3Dadministrative) for admin level definitions.
- **Default**: `[2, 4, 6, 8, 10]` (for USA)

