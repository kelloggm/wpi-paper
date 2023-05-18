#!/bin/bash

# This script should be used on projects that contain multiple sub projects
# that have multiple directories. This script requires that you execute it
# using the `source` command and have -Ajava=${env.WPITEMPDIR} in the pom.xml
# build file. This script was intended to be used only on Maven projects.

# The names of the subfolders in your project that are considered subprojects.
SUBPROJECTS=(
    "core"                                                    
    "validators"                                              
    "plugin-base"                                              
    "maven-plugin"
    "cli"
)

# Full path to the source project/repo on the system.
PROJECT_SPACE=$(realpath)
PROJECT_NAME=$(basename $PROJECT_SPACE)
# The root directory where all the subprojects WPITEMP directories will be stored, change tmp/ as needed.
TOP_LEVEL=tmp/$PROJECT_NAME
mkdir $TOP_LEVEL

for subProject in "${SUBPROJECTS[@]}"
do
    if [ -d $PROJECT_SPACE/$subProject ]
    then
        echo "$subProject Found"
        mkdir -p ${TOP_LEVEL}/${subProject}
        WPITEMPDIR=${TOP_LEVEL}/${subProject}
        WPIOUTDIR="$PROJECT_SPACE/$subProject/build/"
        echo "Running WPI ON $subProject"
        ./wpi.sh $WPITEMPDIR $WPIOUTDIR    
    else 
        echo "$subProject Missing"
    fi
done