# Configuration and setup
Perviam was designed with flexibility in mind, and most meaningful operation parameters can be configured through
environment variables, or configuration files.

# Environment variables
Some key values are loaded from the environment first, and if not found, they fall back to the configuration file value.
These are:

- ## `STORAGE_FOLDER`
  - This is the first environment variable you may want to set. This allows the storage folder location to change.
  If the folder doesn't exist, it will be created.
  - Example: `/home/perviam/config`
  - Default: `./config`
- ## `LANGUAGE`
  - Changes the response language. Must be set to an [IETF BCP 47](https://en.wikipedia.org/wiki/IETF_language_tag)
    [two-letter](https://en.wikipedia.org/wiki/ISO_639-1) code. Optionally, a tighter tag may be used, ex: `fr-CA`.
    The translation must exist, and Perviam must have been built with the translation file. Check [Translation docs](Translation.md)
    for info on how to create translations.
  - Example: `en`
- ## `COUNTRY`
  - Set the country before startup. You may want to do this to avoid generating useless landmarks on a first run.
  The country must be specified using the [ISO3166-1](https://en.wikipedia.org/wiki/ISO_3166-1) tag format.
  - Example: `US`
- ## `OVERPASS_URL`
  - Overrides the Overpass URL. Useful for testing and load balancing purposes.
  - Example: `https://overpass-api.de/api/interpreter`
- ## `REMOTE_TIMEOUT`
  - Sets the remote read timeout. This represents the Overpass response timeout. It's set in seconds, and must be a number.
  It's recommended to keep a sane value to avoid hanging connections.
  - Example: `60`

# Configuration keys
After first run, `STORAGE_FOLDER` will contain the `settings.json` configuration file. If `STORAGE_FOLDER` is unset, a
new folder `config` will be created at the current working directory and it will contain `settings.json`.

### `landmarks.json` must be removed manually to trigger a landmark regeneration

- ## `overpassUrl`
  - Changes the Overpass URL to use for queries.
  - Default: `https://overpass-api.de/api/interpreter`
- ## `landmarks`
  - A list of what we should consider a landmark. Currently unused.
  - Default: `["city"]`
- ## `country`
  - The country for this instance. An instance can only have a single country, and the value will be used to generate
  the landmarks.
  - Ideally, a single Perviam instance will serve queries from a single country. To work around this
  limitation multiple instances can be run.
  - The country must be specified using the [ISO3166-1](https://en.wikipedia.org/wiki/ISO_3166-1) tag format.
  - Default: `US`
- ## `remoteTimeout`
  - Sets the remote read timeout. This represents the Overpass response timeout. 
  - It's recommended to keep a sane value to avoid hanging connections.
  - Set in seconds.
  - Default: `60`
- ## `reverseGeocoderDistance`
  - Changes the distance used for reverse geocoding resolution. This directly affects the quality of the results.
  - A higher value means targets further away from the road will resolve, but accuracy suffers.
  - A lower value leads to an increase in accuracy, but a total failure to resolve targets that are further away from roads.
  - Higher values will load the Overpass API significantly more.
  - In meters.
  - Default: `50`
- ## `languageCode`
  - Changes the response language. 
  - Must be set to an [IETF BCP 47](https://en.wikipedia.org/wiki/IETF_language_tag)
    [two-letter](https://en.wikipedia.org/wiki/ISO_639-1) code. 
  - A tighter tag may be used for localization purposes, ex: `fr-CA`.
  - The translation must exist, and Perviam must have been built with the translation file. Check [Translation docs](Translation.md)
    for info on how to create translations.
  - Default: `en`
- ## `adminLevelCity`
  - The OSM `admin_level` that's considered a "city" at the currently configured country.
  - Check the [boundary=administrative](https://wiki.openstreetmap.org/wiki/Tag:boundary%3Dadministrative) OSM wiki
  page for an up-to-date list of admin level definitions.
  - Default: `8` (for USA)
- ## `cityAdminLevels`
  - The `admin_level`s to include in the result of a query inside a city.
  - Check the [boundary=administrative](https://wiki.openstreetmap.org/wiki/Tag:boundary%3Dadministrative) OSM wiki
  page for an up-to-date list of admin level definitions.
  - Example: If set to `[8, 4, 2]`, it will include the city, state, and country on the output (for the USA): 
  `Des Moines, Iowa, United States of America`
  - Order is irrelevant, it will always be sorted from more to least specific.
  - If a requested `admin_level` is not found, it's skipped.
  - Default: `[2, 4, 8, 10]` (for USA)
- ## `ruralAdminLevels`
  - Behaves similarly to `cityAdminLevels`, but is used whenever the result of a query is outside the boundary of
  a city.
  - Check the [boundary=administrative](https://wiki.openstreetmap.org/wiki/Tag:boundary%3Dadministrative) OSM wiki
  page for an up-to-date list of admin level definitions.
  - This setting allows for further fine-tuning of rural addresses, adding more details where available.
  - Default: `[2, 4, 6, 8, 10]` (for USA)