#!/bin/bash

# This script runs wpi.sh on projects that contain multiple  
# sub projects under their respective directories within your project.

# The changes you will have to make include:
# 1. Add the name of each subproject directory to the `SUBPROJECTS` array. 
# 2. Change WPITEMPDIR to "$1" and WPIOUTDIR to "$2" in your copy 
# of the wpi-template.sh script.
# 3. Set -Ajava=${env.WPITEMPDIR} in the pom.xml.
# 4. Run the script using the `source` command.

SUBPROJECTS=(
# Add the subdirectory names here.
)

# Full path to the source project/repo on the system.
PROJECT_SPACE=$(realpath)
PROJECT_NAME=$(basename "${PROJECT_SPACE}")
# The root directory where all the subprojects WPITEMP directories will be stored, change tmp/ as needed.
TOP_LEVEL=/tmp/"${PROJECT_NAME}"
mkdir "${TOP_LEVEL}"

for subProject in "${SUBPROJECTS[@]}"
do
    if [ -d "${PROJECT_SPACE}"/"${subProject}" ]
    then
        echo "$subProject Found"
        mkdir -p "${TOP_LEVEL}"/"${subProject}"
        WPITEMPDIR="${TOP_LEVEL}"/"${subProject}"
        WPIOUTDIR="$PROJECT_SPACE/$subProject/build/"
        echo "Running WPI ON $subProject"
        # Change the name of your wpi.sh script if needed.
        ./wpi.sh "${WPITEMPDIR}" "${$WPIOUTDIR}"   
    else 
        echo "$subProject Missing"
    fi
done