# Using Perviam
Perviam depends on a running Overpass API instance. Check the excellent
[wiktorn Overpass API Docker image](https://hub.docker.com/r/wiktorn/overpass-api) to host your own
instance, or try a [public Overpass API instance](https://wiki.openstreetmap.org/wiki/Overpass_API#Public_Overpass_API_instances).

By default, `https://overpass-api.de/api/interpreter` will be used.

Check the [configuration docs](Configuration.md) for information on how to change the Overpass API instance URL,
and other settings.


## HTTP API (PORT 8080)

### GET `/api/v1/reverse`
Takes two parameters:
- `latitude` double
- `longitude` double

Returns the human-readable query string result as the body of the response, **UTF-8**.

Example: GET `/api/v1/reverse?latitude=39.127389&longitude=-84.508553`

