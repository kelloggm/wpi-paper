#!/bin/bash

# List of Github repositories to clone
repositories=(
    "https://github.com/kelloggm/icalavailable"
    "https://github.com/dd482IT/require-javadoc-WPI"
    "https://github.com/dd482IT/randoop-wpi-testing.git"
    "https://github.com/dd482IT/randoop-wpi.git"
    "https://github.com/dd482IT/RxNorm-explorer-wpi.git"
)

# How to handle dmn-check?
# How to handle randoop?

# Branch to checkout in each repository
branch="wpi-enabled"
JAVA_HOME="/Library/Java/JavaVirtualMachines/openjdk-17.jdk/Contents/Home"
CHECKERFRAMEWORK="/Users/daniel/Documents/NJIT/WPI-Research/checker-framework/checker-framework-dd482IT"
#PROJECT_DIR="WPI_EXPERIMENTS" 
# Make a root directory 
#mkdir $PROJECT_DIR
#pushd $PROJECT_DIR

for repository in "${repositories[@]}"
do
    # Extract repository name from URL
    repo_name=$(echo $repository | cut -d'/' -f5 | cut -d'.' -f1)

    # Clone repository
    git clone $repository
    pushd $repo_name
    git checkout $branch

    source wpi.sh 2>&1 logErrors$repo_name.txt # Source will bring in the enviroment variables # How to get the warnings from gradle? 
    # Run wpi.sh > file 
    popd
    exit
done




