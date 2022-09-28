#!/bin/sh

GJF=~/Downloads/google-java-format-1.15.0-all-deps.jar

# the root of the wpi output
WPI_RESULTS_DIR=./wpi-annotations/

# the root of the java source files
JAVA_SRC_DIR=./src/main/java/

java -jar ${GJF} -i $(find ${JAVA_SRC_DIR} -name "*.java")

for ajava_file in $(find ${WPI_RESULTS_DIR} -name "*.ajava"); do
    git mv ${ajava_file} ${ajava_file}.java
done

java -jar ${GJF} -i $(find ${WPI_RESULTS_DIR} -name "*.ajava*")

for ajava_file in $(find ${WPI_RESULTS_DIR} -name "*.ajava.java"); do
    git mv ${ajava_file} ${ajava_file%.java}
done
