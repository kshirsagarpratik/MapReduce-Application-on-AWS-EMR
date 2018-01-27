YouTube link for Amazon EMR Deployment movie: https://youtu.be/xvPofa1GKYs

The source code is in /src/main/java/org/imgscalr

The test code is in /src/test/java/org/imgscalr. There are 29 test methods in 7 test classes.

The gradle and sbt build scripts are at /build.gradle and /build.sbt respectively

The input and output for the MapReduce program are at /input and /output respectively.

The source code for MapReduce is at /MapReduce. It contains the .java, .class and .jar files.

The build.gradle can be run from the terminal as 'gradle build'

The build.sbt can be run from terminal as 'sbt compile' followed by 'sbt test'

The tests were instrumented using IntelliJ's built in coverage tool. Line by line coverage(tracing) generated HTML files located in '/test report'. These reports were used to construct the 'input.txt' file located at /input.

The video includes details about how I deployed the MR application on AWS EMR. Link has been attached at top of this README file.

The same application was run locally on Hadoop 2.7.4 via Terminal, following the official Hadoop documentation.
