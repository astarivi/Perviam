# Perviam
Perviam is a tool designed to derive human-readable street addresses for moving objects based on their geographic coordinates. It provides additional positional context to better pinpoint the physical location of a moving object.

> Perviam is a reverse geocoding solution optimized for vehicle tracking.

---

## Why Another Reverse Geocoder?
Perviam focuses on generating concise, human-readable positional descriptions for moving objects (primarily vehicles) without requiring users to interpret the results on a map. This is achieved by leveraging nearby landmarks and intersecting streets.

For example:
- A general-purpose geocoder might return:
  > 126, East McMillan Street, Mount Auburn, Cincinnati, Hamilton County, Ohio, 45219, United States

- Perviam would resolve the same location as:
  > East McMillan Street between Auburn Avenue and Paris Street, Mount Auburn, Cincinnati, Ohio, United States

---

# Quickstart
For all supported platforms, Java version 17 or higher is a must.

## Docker
For every release, a Docker image is created at the [Perviam Docker Hub repository.](https://hub.docker.com/r/astarivi/perviam)

## Windows
Windows-specific executables are available. Only 64-bit and 32-bit architectures are supported, for other architectures, please use the Universal release.

- Download the latest [Windows executable package](https://github.com/astarivi/Perviam/releases/latest/download/perviam-windows.zip) release and extract it.
- Run `Perviam.exe`

## Linux
Linux has its own distributable package, which includes a wrapper script for launch.

```bash
# Download the latest `Perviam-VERSION.zip` release.
# Remember to replace VERSION with the latest release version.
wget https://github.com/astarivi/OGXRepacker/releases/latest/download/Perviam-VERSION.zip

# Extract it
unzip Perviam-VERSION.zip
cd Perviam-VERSION/bin

# Make the script executable
chmod +x Perviam
# Run the script
./Perviam
```

## Universal
Download the latest [universal prebuilt jar](https://github.com/astarivi/Perviam/releases/latest/download/perviam.jar)
and run it: `java -jar perviam.jar`

## Compile from source
To build Perviam from source you will need JDK >17. Prefer JDK 21.
```shell
# Clone the source
git clone https://github.com/astarivi/Perviam.git
cd Perviam/perviam

# Build universal executable package
./gradlew shadowJar

# Run it
java -jar build/libs/perviam.jar
```

---

### [Documentation Index](docs/Index.md)

