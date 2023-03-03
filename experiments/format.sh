#!/bin/sh

# the root of the wpi output
WPI_RESULTS_DIR=./wpi-annotations/

# the root of the java source files
JAVA_SRC_DIR=./src/main/java/

for ajava_file in $(find ${WPI_RESULTS_DIR} -name "*.ajava"); do
    git mv ${ajava_file} ${ajava_file}.java
done

for ajava_file in $(find ${WPI_RESULTS_DIR} -name "*.ajava.java"); do
    git mv ${ajava_file} ${ajava_file%.java}
done
