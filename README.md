# Running the Simulator #

Within the `simulator` directory:

For Gradle 6.6.1: Use `gradle build` followed by `gradle run`.

For any other version: Use `./gradlew build` followed by `./gradlew run`.

# Other Notes #

If the GUI does not run for any reason, or if you do not want to run the GUI, changing the line `mainClassName = "SimulatorV3"` in `simulator/build.gradle` to `mainClassName = "SimulatorV1"` will run the simulator without the GUI. Note that human players can participate in battles through the GUI.
