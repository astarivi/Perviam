# Perviam
Perviam is a tool designed to derive human-readable street addresses for moving objects based on their geographic coordinates. It provides additional positional context to better pinpoint the physical location of a moving object.

> Perviam is a reverse geocoding solution optimized for vehicle tracking.

---

## Latest Tags

### Alpine
- `{ver}-temurin-alpine`
- `{ver}-temurin`

`latest` is `{ver}-temurin`.

### Ubuntu Noble
- `{ver}-temurin-noble`

---

## Why Another Reverse Geocoder?
Perviam focuses on generating concise, human-readable positional descriptions for moving objects (primarily vehicles) without requiring users to interpret the results on a map. This is achieved by leveraging nearby landmarks and intersecting streets.

For example:
- A general-purpose geocoder might return:
  > 126, East McMillan Street, Mount Auburn, Cincinnati, Hamilton County, Ohio, 45219, United States

- Perviam would resolve the same location as:
  > East McMillan Street between Auburn Avenue and Paris Street, Mount Auburn, Cincinnati, Ohio, United States

---

## Getting Started
Perviam requires access to a running Overpass API instance. You can either host your own instance using the excellent [wiktorn Overpass API Docker image](https://hub.docker.com/r/wiktorn/overpass-api) or utilize a [public Overpass API instance](https://wiki.openstreetmap.org/wiki/Overpass_API#Public_Overpass_API_instances).

### HTTP API (PORT 8080)

#### **GET** `/api/v1/reverse`

**Parameters:**
- `latitude` (double)
- `longitude` (double)

**Response:**
Returns the human-readable address as the response body in **UTF-8**.

**Example Request:**
```http
GET /api/v1/reverse?latitude=39.127389&longitude=-84.508553
```

---

## Configuration

### General Configuration
For detailed configuration options, refer to the [Perviam Configuration Documentation](https://github.com/astarivi/Perviam/blob/main/docs/Configuration.md).

### Docker Container
The Perviam Docker container provides the following:
- **Volumes:**
  - `/root/storage`: Stores settings and landmarks.
  - `/root/logs`: Contains rotating log files.
- **HTTP API:**
  - Exposed at port `8080` with the **GET** route `/api/v1/reverse`.

For more details, check the [Usage Documentation](https://github.com/astarivi/Perviam/blob/main/docs/Usage.md).

---

## License

- The `Dockerfile` used to build this image is licensed under the Apache License version 2.0. This license applies to the `Dockerfile`, not the resulting container image.
- The base image `Dockerfile` (Temurin) is also licensed under the Apache License version 2.0.
- Perviam itself is licensed under the Apache License version 2.0.

---

## References

- [Perviam Repository](https://github.com/astarivi/Perviam)
- [Perviam Documentation](https://github.com/astarivi/Perviam/blob/main/docs/Index.md)
- [Perviam Dockerfile](https://github.com/astarivi/Perviam/blob/main/docker/Dockerfile)
- [Report Issues with Perviam](https://github.com/astarivi/Perviam/issues)
- [Temurin Base Image](https://hub.docker.com/_/eclipse-temurin)

