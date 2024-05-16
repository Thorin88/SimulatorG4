# AI to Play Pokemon Battles

[SimulatorG4 in Action](./readme_resources/intro.png)

# Contents

[Introduction](#introduction)\
[Tech Used](#tech-used)\
[Features](#features)\
[Running the Simulator](#running-the-simulator)\
[File Information](#file-information)\
[Other Notes](#other-notes)

# Introduction

AI agents that can play Pokemon battles, running on a custom simulator which was built from scratch in Java.

The simulator includes a GUI for humans to play against AI agents, can simulate and record the results of 1000s of AI vs AI battles, and supports a large number of Pokemon battle features.

# Tech Used

Java, JavaSwing, GSON, APIs, Gradle, Python (for data plotting), LaTeX

# Features

## Highlights

Human players can play Pokemon battles against AI. Human players can also be supported by AI, where the AI gives recommendations to the human player who is playing against another AI.

The custom-built simulator supports 100s of Pokemon, and 1000s of moves.

## AI Features

Builds and analyses a tree of game possible states which spans two turns into the future.

Can be asked to play with different modes of knowledge
- Full: AI can always see ALL information about the game state
- Partial: AI can only see what humans can see, requiring the AI to work with partial information and build up what it knows as it sees more during a battle

Considers multiple possible futures, making its choice based on a weighted average of the outcome of each future based on the chance of this future occurring.

Considers players switching Pokemon, turn order changing, and more during its simulations.

## Simulator Features

As well as supporting human players, the simulator can run AI agents against each other, to test which agents come out on top.

Supports a large number of moves and Pokemon, supporting anything that was able to be expressed through the metadata available in the PokeAPI.

Additionally supports concepts which required dedicated implementation work, such as weather, entry hazards, and field affects.

The figure below shows the concepts that are supported by the simulator, for those who are familar with Pokemon.

[Simulator Features](./readme_resources/simulator_features.png)

The simulator can provide summaries of your Pokemon, shown in the figure below.

[Pokemon Summary](./readme_resources/pokemon_summary.png)

# Running the Simulator

Requirements: [Gradle](https://gradle.org/install/) and [Java](https://www.java.com/en/download/help/download_options.html) installed

1. Clone this repository

2. Clone the submodules for this repository

3. Navigate to the `simulator` directory

4. Run `gradle build` followed by `gradle run` (or use `./gradlew build` followed by `./gradlew run`)

# File Information

`simulator` contains the core of the simulator.

`simulator/src/main/java/simulator/SimulatorV3.java` is the main driver file. The Pokemon, and all of their stats and moves, can be customised in the `main` function of this file.

The PlayerStrategy files contain the code for the agents developed.

`api-data` and `sprites` are forked from the https://github.com/PokeAPI/api-data and https://github.com/PokeAPI/sprites repositories respectively.

# Other Notes

If the GUI does not run for any reason, or if you do not want to run the GUI, changing the line `mainClassName = "SimulatorV3"` in `simulator/build.gradle` to `mainClassName = "SimulatorV1"` will run the simulator without the GUI. Note that human players can only participate in battles through the GUI, so this solution can only be used to see a simulation of two AI agents playing against each other.
