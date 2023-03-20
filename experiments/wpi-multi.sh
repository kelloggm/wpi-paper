#!/bin/bash

# Have list of the project sub names (ie, Project1, Project2, Project3)

# Check if project name exists
if [ -z "$1" ] 
then 
    echo "Usage: <Projectname> <ProjectsFile>" 
    exit
else 
    PROJECT_NAME="$1"
fi

if [ -z "$2" ] 
then 
    echo "Usage: <Projectname> <ProjectsFile>" 
    exit
fi

TOP_LEVEL=~/Documents/WPITEMP-${PROJECT_NAME} #This directory should be chosen by the user?
mkdir -p ${TOP_LEVEL}

# Check if all sub-project directories exist and contain java files 
while read subProject; do
    if [ -d ./$subProject ]
    then
        echo "$subProject Found"
        mkdir -p ${TOP_LEVEL}/${subProject}
    else 
        echp "$subProject Missing"
        exit
    fi
done <$2

while read subProject; do
    WPIOUTDIR="./${subProject}/build/" #This should not assume that the files will be found in build but it is most likely they will
    #For some reason ".${subproject}/build/ doesn't work when passing to wpi script"
    WPITEMPDIR=${TOP_LEVEL}/${subProject}
    echo "Running WPI ON $subProject"
    echo $WPIOUTDIR
    ./wpi-multi.sh $WPITEMPDIR $WPIOUTDIR
    wait
done <$2




#WPIOUTDIRECTORY 
#WPITEMPDIRECTORY 

# This script will iterate over each project name (ie, Project1, Project2, Project3)
# It will create a root WPITEMPDIR (ie, /tmp/Project/)
# It will pass the root dir + the specific project dir to WPI and to pom.xml as a variable 

# wpi.sh (#WPITEMPDIR = root + sub dir) (WPIOUTDIRECTORY = ./subdir + /build/
# This will iterate until the wpi script returns a status code of 0 which will then continue on to the next project
