#!/bin/bash

# List of Github repositories to clone
repositories=(
  "https://github.com/kelloggm/icalavailable"
  "https://github.com/dd482IT/require-javadoc-WPI"
  "https://github.com/dd482IT/randoop-wpi.git"
  "https://github.com/dd482IT/table-wrapper-csv-impl-wpi.git"
  "https://github.com/dd482IT/table-wrapper-csv-impl-wpi.git"
  "https://github.com/dd482IT/RxNorm-explorer-wpi.git"
  "https://github.com/dd482IT/randoop-wpi.git"
  "https://github.com/dd482IT/reflection-util-wpi"
  "https://github.com/dd482IT/multi-version-control-wpi"
  "https://github.com/dd482IT/lookup-wpi"
  "https://github.com/dd482IT/Nameless-Java-API-WPI.git"
)

branch="wpi-enabled"

if [ -z $JAVA_HOME ]; then echo "\$JAVA_HOME is empty"; exit; fi; # 
 # Since all projects support Java 11 (And some require Java 11, set this to JAVA 11)

if [ -z $CHECKERFRAMEWORK ]; then echo "\$JAVA_HOME is empty"; exit; fi; # Only necessary for Gradle Projects, MVN projects will need a local snapshot. 

for repository in "${repositories[@]}"
do
    repo_name=$(echo $repository | cut -d'/' -f5 | cut -d'.' -f1)
    git clone $repository
    pushd $repo_name
    git checkout $branch
    /bin/bash wpi.sh 
    popd
done



