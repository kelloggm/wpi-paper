This document describes the procedure for adding a new project to the
experiments in the WPI paper.

The input to the procedure is a program
that typechecks using one or more Checker Framework typecheckers.

The outputs of the procedure are the following, which are the
numbers in tables 1 & 2 in the WPI paper:
* the number of lines of code in the project
* the number of annotations in the project, before running WPI,
split by checker ("original annotations")
* the number of annotations inferred by WPI, split by checker
("inferred annotations")
* the number of remaining errors after running WPI
* the percentage of the original annotations that WPI inferred

The procedure:

1. Fork and then clone the project, and checkout the commit in the
projects/ directory. In the file `projects.in`, record the URL and
commit ID. Then, add a row to the "summary" sheet in the spreadsheet
without any data.

2. Create a new branch called "baseline" at that commit:
`git checkout -b baseline ; git push origin baseline`

3. Create a new branch called "unannotated" starting at the same commit:
`git checkout -b unannotated`

4. Run the `RemoveAnnotationsForInference` program on the source:
`java -cp "$CHECKERFRAMEWORK/checker/dist/checker.jar" org.checkerframework.framework.stub.RemoveAnnotationsForInference .`

5. Push the unannotated code:
`git commit -am "output of RemoveAnnotationsForInference" ; git push origin unannotated`

6. Collect the number of original annotations in the code:
   a. run `git checkout -b annotation-statistics origin/baseline`
   b. modify the project's build file so that it runs the
   `org.checkerframework.common.util.count.AnnotationStatistics` processor
   instead of the typecheckers it was running before
   c. add the `-Aannotations` and `-Anolocations` options
   d. compile the program and record the output in the spreadsheet. (You should
   create a new "sheet" in the spreadsheet for each project. Copy one that's
   already there and delete the data in it.)
   TODO: consider writing a script for interpreting the output of AnnotationStatistics by checker?
   e. run `git commit -am "annotation statistics configuration" ; git push origin annotation-statistics`

7. Collect the number of lines of code:
   a. run `git checkout baseline`
   b. run `scc .` and record the results in the spreadsheet, on the summary page

8. Run WPI:
   a. run `git checkout -b wpi-enabled origin/unannotated`
   b. choose any temporary directory for $WPITEMPDIR
   c. modify the build file to run with `-Ainfer=ajava`, `-Awarns`, '-AinferOutputOriginal', and `-Aajava=$WPITEMPDIR` (modifying the latter as appropriate for project structure). Make sure that you remove any `-Werror` argument to javac, because otherwise WPI will fail.
   d. write a short script based on the template in `wpi-template.sh`. The script should:
      i. copy the content of `build/whole-program-inference` into $WPITEMPDIR
      ii. compile the code
      iii. compare `build/whole-program-inference` and $WPITEMPDIR. If they're the same, exit. Otherwise, go to step i.
   e. add this script: `chmod +x wpi.sh ; git add wpi.sh`
   f. commit the script and build changes: `git commit -am "enable WPI" ; git push origin wpi-enabled`
   g. execute the script: `./wpi.sh`
   h. record the number of errors issued by the typecheckers (and which
   typechecker issued the error) after the script is finished

9. Create a branch for the code with inferred annotations
   a. Create a branch: `git checkout -b wpi-annotations wpi-enabled`
   b. Create a new directory for the inferred ajava files: `mkdir wpi-annotations`
   c. copy all the ajava files: `rsync -r ${WPITEMPDIR}/ wpi-annotations`
   d. commit the results: `git add wpi-annotations/** ; git commit -am "WPI annotations" ; git push origin wpi-annotations`

10. Measure the number of annotations that WPI inferred, by checker:
   a. create a list of the checkers used by WPI. You can collect this information from the produced ajava files.
   b. write a script that, for each checker in the above list (TODO: write this script and add it to this repo):
      i. copies the ajava files associated with that checker over top
      of the original files from which they were produced
      ii. runs AnnotationStatistics on the resulting program
      iii. resets the state of the repository