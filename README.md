# Perviam
Perviam is a tool to find the human-readable street address of a moving object from a given set of coordinates.
This usually results in an extra set of positional references to better identify the physical position of a moving object.

"Perviam is a movement based reverse geocoding tool, designed around vehicle tracking."

# Usage
There are multiple ways to get started with using Perviam, let's review them all.

## Prebuilt releases
Download the latest universal prebuilt Java jar from the [Releases page](https://github.com/astarivi/Perviam/releases)
and run it with Java 21.

## Docker
For every release, a Docker image is created.

## Compile from source
To build Perviam from source you will need JDK >21.
```shell
# Clone the source
git clone https://github.com/astarivi/Perviam.git
cd Perviam

# Build executable package
./gradlew shadowJar

# Run it
java -jar build/libs/perviam.jar
```