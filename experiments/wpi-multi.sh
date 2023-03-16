#!/bin/bash

# Have list of the project sub names (ie, Project1, Project2, Project3)

PROJECT_NAME="$1"
mkdir /tmp/WPITEMP-$PROJECT_NAME
TOP_LEVEL=/tmp/WPITEMP-$PROJECT_NAME

# Loop over each user specified project to confirm it exists and containts java code
# The list of sub projects should collected by the script, not the user. 


while read subProject; do
    mkdir $TOP_LEVEL/$subProject
    WPITEMPDIR=$TOP_LEVEL/$subProject
    WPITOUTDIR=./$subProject/build/
    /bin/bash wpi.sh $WPITEMPDIR $WPIOUTDIR
    wait
done <subProjects.txt




#WPIOUTDIRECTORY 
#WPITEMPDIRECTORY 

# This script will iterate over each project name (ie, Project1, Project2, Project3)
# It will create a root WPITEMPDIR (ie, /tmp/Project/)
# It will pass the root dir + the specific project dir to WPI and to pom.xml as a variable 

# wpi.sh (#WPITEMPDIR = root + sub dir) (WPIOUTDIRECTORY = ./subdir + /build/
# This will iterate until the wpi script returns a status code of 0 which will then continue on to the next project