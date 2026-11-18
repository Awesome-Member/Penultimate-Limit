# Penultimate-Limit
An unofficial desktop client :p

## Build and run
Requires JDK 8 and ... Maven.

```sh
mvn clean install
cd target
java -javaagent:Penultimate-Limit.jar -jar Penultimate-Limit.jar
```

## Mods
The game supports mods. See [this]() branch for an example.

To install a mod, create a `mods/` folder next to the game JAR and place
the mod JAR inside.
