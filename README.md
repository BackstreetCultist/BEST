# BEST
## Braitenberg Evolutionary Swarm Technology

Welcome to BEST, a genetic system for the evolution of Braitenberg Vehicles in
a simulated 'petri dish' environment.

BEST is implemented in Java using the Gradle package manager. It requires Java8
or above and Gradle 7 or above.
Parameters can be set within the World.java
and Microbe.java files (look for the CONSTANTS), and the system can be run with
the following command:

    gradle clean build run

The project is pre-configured to run a test with 32 microbes, in a balanced
environment of 48 stimuli sources of various types.
