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

Prerequisites:
* TODO: list required software. Incomplete list of software you must have installed:
git, a JDK, the Checker Framework (see next item)
* follow the "Building from Source" instructions in the Checker Framework manual
(https://checkerframework.org/manual/#build-source), especially making sure
that your $CHECKERFRAMEWORK environment variable is set, because that is used
in some of the steps of the experimental procedure below.

Example outputs of this experimental procedure (which may become inputs for downstream tools) 
can be found in (/main/experiments/inferred-annos-counter/inputExamples). 
These example inputs should only be created for smaller projects.

The procedure:

1. Fork and then clone the project, and checkout the commit in the
projects/ directory. In the file `projects.in`, record the URL (of your fork) and
commit ID. Then, add a row to the "summary" sheet in the spreadsheet
(https://docs.google.com/spreadsheets/d/1r_NhumolEp5CiOL7CmsvZaa4-FDUxCJXfswyJoKg8uM/edit?usp=sharing)
with both the original and forked url as well as the commit ID.

2. Create a new branch called "baseline" at that commit:
`git checkout -b baseline ; git push origin baseline`

3. Create a new branch called "unannotated" starting at the same commit:
`git checkout -b unannotated`

4. Run the `RemoveAnnotationsForInference` program on the source, no ouput means it ran successfully:
`java -cp "$CHECKERFRAMEWORK/checker/dist/checker.jar" org.checkerframework.framework.stub.RemoveAnnotationsForInference .`

5. Push the unannotated code:
`git commit -am "output of RemoveAnnotationsForInference" ; git push origin unannotated`

6. Collect the number of original annotations in the code:
   a. run `git checkout -b annotation-statistics origin/baseline`
   b. modify the project's build file so that it
        i. does not run the typecheckers that it was running before, and
        ii. does runs the `org.checkerframework.common.util.count.AnnotationStatistics` processor
   c. add the `-Aannotations` and `-Anolocations` options, and make sure that you remove any `-Werror` argument to javac.
   d. compile the program and record the output in the spreadsheet. (You should
   create a new "sheet" in the spreadsheet for each project. Copy one that's
   already there and delete the data in it.)
   TODO: consider writing a script for interpreting the output of AnnotationStatistics by checker?
   e. run `git commit -am "annotation statistics configuration" ; git push origin annotation-statistics`

7. Collect the number of lines of code:
   a. run `git checkout baseline`
   b. run `scc .` and record the number of non-comment, non-blanks lines of Java code (the "Code" column of the "Java" row) in the spreadsheet (in the "LoC" column), on the summary page (if you don't have scc installed, https://github.com/boyter/scc)
   
8. Run WPI:
   a. run `git checkout -b wpi-enabled origin/unannotated`
   b. choose any temporary directory for $WPITEMPDIR
   c. modify the build file to run with `-Ainfer=ajava`, `-Awarns`, '-AinferOutputOriginal', and `-Aajava=$WPITEMPDIR` (modifying the latter as appropriate for project structure, 
   Ex: '-Aajava=/path/to/temp/dir/'). Make sure that you remove any `-Werror` argument to javac, because otherwise WPI will fail.
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
   a. Create a branch: `git checkout -b wpi-annotations annotation-statistics`
   b. Create a new directory for the inferred ajava files: `mkdir wpi-annotations`
   c. copy all the ajava files: `rsync -r ${WPITEMPDIR}/ wpi-annotations`
   d. commit the results: `git add wpi-annotations/** ; git commit -am "WPI annotations" ; git push origin wpi-annotations`

10. Measure the number of annotations that WPI inferred, by checker:
    a. switch to the `wpi-annotations` branch: `git checkout wpi-annotations`
    b. create a copy of the script `compute-annos-inferred.sh` in the target project directory
    c. modify the variables at the beginning of the script as appropriate for the target project
    d. run the script
    e. transcribe the output after "====== COMBINED RESULTS =======" is printed to the spreadsheet, combining rows that mention the same annotation (this happens when e.g., different @RequiresQualifier annotations are inferred by different checkers)
    f. commit and push the script: `git add compute-annos-inferred.sh ; git commit -m "inference output summarization script" ; git push origin wpi-annotations`

11. Measure the percentage of hand-written annotations that WPI inferred
    a. copy the formatter script as `format-mycopy.sh` found in the experiments directory into your project's top level directory. 
    b. download the `google-java-format.jar`. The link for it is in the script's documentation. Confirm the variable path in the script aligns with the location of your download.
    c. change the variable, `WPI_RESULTS_DIR` to the path of your project's WPI annotations `wpi-annotations`.
    d. confirm that the `JAVA_SRC_DIR` is appropriate to your project's file tree which may look something like `./src/main/java/` and should contain `your-project.java` in the lowest directory, modify if needed.
    e. run the script `./format-mycopy.sh`.
    f. copy outputs of this experimental procedure into (`/main/experiments/inferred-annos-counter/inputExamples`). This can be done by creating a directory in (`/inferred-annos-counter/inputExamples`) with the name of your project and two sub folders, `generated` and `human-written`. (This and following steps are optional. Use as input for downstream tools on small projects only).
    g. copy all of the contents in your `$WPITEMPDIR` directory used in the previous steps into the `generated` subfolder. 
    h. copy all of the human-written code (the human-annotated, i.e. original, code, but with a formatter run over it) from your project's source folder (e.g., ./src/main/java/ in a Gradle project) into the `human-written` directory that was created
    i. run the inferred-annos-counter, with the first agrument being the path of the human-written code, and other agruments being the path of the .ajava files produced by the WPI. The program assumes that a formatter has been applied, so it is important to do so before passing the files as input.

12. Copy summary numbers from the project-specific spreadsheet page to the "summary" tab, and color code the project row green once it is finished.
