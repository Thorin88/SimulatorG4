# Running the Simulator #

Within the `simulator` directory:

For Gradle 6.6.1: Use `gradle build` followed by `gradle run`.

For any other version: Use `./gradlew build` followed by `./gradlew run`.

# File Information #

`simulator` contains the core of the simulator.

SimulatorV3.java is the main driver file. The Pokemon, and all of their stats and moves, can be customised in the `main` function of this file.

The PlayerStrategy... .java files contain the code for the agents developed.

`api-data` and `sprites` are forked from the https://github.com/PokeAPI/api-data and https://github.com/PokeAPI/sprites repositories respectively.

# Other Notes #

If the GUI does not run for any reason, or if you do not want to run the GUI, changing the line `mainClassName = "SimulatorV3"` in `simulator/build.gradle` to `mainClassName = "SimulatorV1"` will run the simulator without the GUI. Note that human players can participate in battles through the GUI.
