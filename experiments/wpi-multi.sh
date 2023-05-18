#!/bin/bash

# This script is designed to run WPI on projects that have multiple sub projects. These subprojects usally are sub directories within the project space.

# This array requires human input.
SUBPROJECTS=(
    "core"                                                    
    "validators"                                              
    "dmn-check-server"                                                   
    "plugin-base"                                              
    "maven-plugin"
    "cli"
)

# Full path to the souece project or repo on the system.
PROJECT_SPACE="/Users/daniel/Documents/NJIT/WPI-Research/experiments-live/tmp-storage/dmn-check-wpi" 

# The top level directory is the root directory of the project as a whole. A directory should be supplied by the user but tmp should do fine for now.
TOP_LEVEL=~/Documents/WPITEMP-${PROJECT_NAME} 
mkdir -p ${TOP_LEVEL}

# Check if all sub-project directories exist and contain java files 
for subProject in read "${SUBPROJECTS[@]}"
do
    if [ -d $PROJECT_SPACE/$subProject ]
    then
        echo "$subProject Found"
        mkdir -p ${TOP_LEVEL}/${subProject}
        WPITEMPDIR=${TOP_LEVEL}/${subProject}
        echo "Running WPI ON $subProject"
        echo $WPIOUTDIR
        ./wpi-multi.sh $WPITEMPDIR $WPIOUTDIR
        wait
    else 
        echo "$subProject Missing"
        exit
    fi
done
