# Translation and Localization

Is Perviam not available in your language? Not to worry—Perviam can be translated to any language, and even better, translations can be contributed back to help others save time during setup.

## Creating a Translation

To begin your translation, find the [IETF BCP 47](https://en.wikipedia.org/wiki/IETF_language_tag) language tag for your target language. Prefer [broader tags](https://en.wikipedia.org/wiki/ISO_639-1), such as `en` over `en-CA`, whenever possible.

With your target language tag in mind, create a new entry in the Resource Bundle `Messages`, located at `perviam/src/main/resources`. For example:
- `perviam/src/main/resources/Messages_fr.properties` for a French translation.
- `perviam/src/main/resources/Messages_fr-CA.properties` for Canadian French.

This file will contain seven lines, each with a single message, formatted as `key=value`. Below are the required keys and their meanings:

### `placeholder`
This is the placeholder Perviam uses when the target location couldn't be resolved.

- **Example**: `placeholder=Unknown address`

### `unknown_street`
This placeholder is used when a target street has no name, but intersects with other streets that provide enough data for naming.

Set this value to the most common, general term for a "road" in the target language. If multiple terms are available, such as "road" and "street," choose the one that applies broadly to both urban and rural scenarios.

- **Example**: `unknown_street=Road`

### `address_between_two`
This key is used when the resolved street address is between two other streets. For example:

![A street between two other streets](res/address_between_two.png)

The black pin represents the target location on "North Kensington Street" (green), which is between "6th Street North" and "5th Street North" (blue).

**Parameters:**
- `{0}`: Target street (e.g., "North Kensington Street").
- `{1}`: First intersecting street (e.g., "6th Street North").
- `{2}`: Second intersecting street (e.g., "5th Street North").

**Example:**
- `address_between_two={0} between {1} and {2}` would produce:
  *"North Kensington Street between 6th Street North and 5th Street North"*

### `address_between_one`
This key is used when the resolved address intersects only one street in the opposite axis.

**Parameters:**
- `{0}`: Target street.
- `{1}`: Intersecting street.

**Example:**
- `address_between_one={0} at {1}` would produce:
  *"North Kensington Street at 6th Street North"*

### `address_lone`
This key is used when only the current street is found, with no intersections.

**Parameter:**
- `{0}`: Target street.

**Example:**
- `address_lone={0}` would produce:
  *"North Kensington Street"*

### `street_ref_prefix`
This key is used when the found street has no name, but has an associated number. For example, roads in the [United States Numbered Highway System](https://en.wikipedia.org/wiki/United_States_Numbered_Highway_System) such as Route 66.

**Parameter:**
- `{0}`: Target road number.

**Example:**
- `street_ref_prefix=Route {0}` would produce:
  *"Route 66"* (when `{0}` is 66).

### `rural_address`
Rural addresses are found outside city boundaries, where intersecting streets are often absent. Instead of referencing street intersections, Perviam uses the nearest landmark.

**Parameters:**
- `{0}`: Target road.
- `{1}`: Formatted distance to the nearest landmark.
- `{2}`: Name of the nearest landmark.

**Example:**
- `rural_address={0} at {1} of {2}` would produce:
  *"Route 66 at 102 miles of Amarillo"*

## Wrapping Up

Once your translation is complete, build Perviam and enable it by setting the `languageCode` configuration value to your language tag.

If everything looks good, feel free to contribute your translation back to the repository for others to use!

