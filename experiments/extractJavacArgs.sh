#!/usr/bin/sh

MVN_COMPILE="mvn -X clean compile"
${MVN_COMPILE} > compile-out.txt
echo -n '$JAVA_HOME/bin/javac' > JavacRaw.txt
grep -E '\s-d .*$' compile-out.txt | cut -c 8- >> JavacRaw.txt
RUN_ANNO_STATS=$(cat JavacRaw.txt)
echo $RUN_ANNO_STATS