This document describes the experimental procedure for adding a new project to the
experiments in the WPI paper.

The input to the procedure is a program
that typechecks using one or more Checker Framework typecheckers.

These instructions create the following branches in a git fork:
 * baseline -- the project with its human-written annotations
 * annotation-statistics -- the buildfile runs the AnnotationStatistics processor
 * unannotated -- the project with human-written annotations removed
 * wpi-enabled -- the buildfile runs WPI instead of just doing typechecking
 * wpi-annotations -- contains the inferred annotations

The outputs of the experimental procedure are the following, which are the
numbers in tables 1 & 2 in the WPI paper:
* the number of lines of code in the project
* the number of annotations in the project, before running WPI,
split by checker ("original annotations")
* the number of annotations inferred by WPI, split by checker
("inferred annotations")
* the number of remaining errors after running WPI
* the number of the annotations in the original source code that WPI was able to infer

Prerequisites:
* TODO: list required software. Incomplete list of software you must have installed:
git, a JDK, the Checker Framework (see next item)
* follow the "Building from Source" instructions in the Checker Framework manual
(https://checkerframework.org/manual/#build-source), especially making sure
that your $CHECKERFRAMEWORK environment variable is set, because that is used
in some of the steps of the experimental procedure below.

The procedure:

1. Clone the project
   1. Fork the project.
   2. Clone your fork in the experiments/projects/ directory (create it if necessary).
   3. In the file `projects.in`, record the URL (of your fork) and commit ID.
   4. Add a row to the "summary" sheet in the spreadsheet
      (https://docs.google.com/spreadsheets/d/1r_NhumolEp5CiOL7CmsvZaa4-FDUxCJXfswyJoKg8uM/)
      with both the original and forked url as well as the commit ID.

2. Create a new branch called "baseline" at that commit:
   a. `git checkout -b baseline ; git push origin baseline`
   b. Ensure that the project builds and typechecks (determine the appropriate command and record it in the spreadsheet).
   c. Inspect the project's build system and determine the list of typecheckers from the Checker
   Framework that the project runs. How this is expressed also varies by build system. For example, in
   a Gradle project that uses the checkerframework-gradle-plugin, you would check the `checkers` block.
   In other build systems, look for a `-processor` argument. Record the names of the typecheckers in the
   "Checkers" column of the spreadsheet.

3. Create a new branch called "unannotated" from the same commit, with annotations removed:
   a. `git checkout -b unannotated`
   b. Run the `RemoveAnnotationsForInference` program on the source; no ouput means it ran successfully:
      `java -cp "$CHECKERFRAMEWORK/checker/dist/checker.jar" org.checkerframework.framework.stub.RemoveAnnotationsForInference .`
   c. Push the unannotated code:
      `git commit -am "output of RemoveAnnotationsForInference" ; git push origin unannotated`
   d. Verify that, because the annotations have been removed, the program no longer typechecks. You should
   see an error from one of the Checker Framework checkers you recorded in step 2c when you re-run whatever
   command you used to run the typechecker before.

4. Collect the number of original annotations in the code:
   a. run `git checkout -b annotation-statistics origin/baseline`
   b. modify the project's build file so that it
        i. does not run the typecheckers that it was running before, and
        ii. does run the processor org.checkerframework.common.util.count.AnnotationStatistics
        iii. add the `-Aannotations` and `-Anolocations` options, and make sure that you remove any `-Werror` argument to javac.
   c. If the build system is maven, add `<showWarnings>true</showWarnings>` to the maven-compiler-plugin `<configuration>`.
   d. If the project is running a formatter (ex: Spotless), disable it in the build system. 
   e. Stage your changes with `git add` (in case you missed a formatter).
   f. Compute annotation statistics.
       i. Clean the program, then compile the program.
       ii. Look in the output for "Found annotations:" or "No annotations found."
          TODO: Make the two tags searchable via a single string or simple regex.
       iii. Create a new "sheet" in the spreadsheet for the project, by copying an existing
          sheet, changing its title, and deleting the data in it.
          TODO: All the current ones have different formats; they should be made uniform.
       iv. Record the output in the spreadsheet (only Checker Framework annotations need to be recorded. Checker Framework
           annotations usually are in a package that starts with "org.checkerframework.*", but if the project uses a custom
	   checker you will need to figure out what its annotations are.).
          TODO: sometimes there are mulitple projects, so there are multiple occurrences of "Found annotations:".  The "Found annotations:" output should indicate in which directory or project the annotations were found, or a script should combine all the tables in the output into a single table.
   TODO: consider writing a script for interpreting the output of AnnotationStatistics by checker?
   g. run `git commit -m "annotation statistics configuration" ; git push origin annotation-statistics`.

5. Collect the number of lines of code:
   a. run `git checkout baseline`
   b. run `scc .` and record the number of non-comment, non-blanks lines of Java code (the "Code" column of the "Java" row) in the spreadsheet at https://docs.google.com/spreadsheets/d/1r_NhumolEp5CiOL7CmsvZaa4-FDUxCJXfswyJoKg8uM (in the "LoC" column, on the summary page)
   If you don't have scc installed, see https://github.com/boyter/scc .
   
6. Run WPI:
   a. run `git checkout -b wpi-enabled origin/unannotated`
   b. choose any temporary directory for $WPITEMPDIR
      TODO: Giving the user choices can be confusing and requires user effort.  Just dictate a temporary directory here, or hardcode it in the commands below.  I'm personally using /scratch/$USER/wpi-output/PROJECTNAME-wpi . (If you're reading this later and are unsure what to chose, this is a good default.)
   c. modify the build file:
       i. run with `-Ainfer=ajava`, `-Awarns`, and `-Aajava=$WPITEMPDIR`
          (modifying the latter as appropriate for project structure, Ex: '-Aajava=/path/to/temp/dir/').
       ii. Remove any `-Werror` argument to javac, because otherwise WPI will fail.
       iii. Disable any non-Checker-Framework annotation processors (e.g., user-defined ones)
   d. Copy `wpi-template.sh` to `wpi.sh` in the project directory.
      Edit the first 5 variables.
      This script should achieve the following effect:
      i. copy the content of `build/whole-program-inference` into $WPITEMPDIR
      ii. compile the code 
      iii. compare `build/whole-program-inference` and $WPITEMPDIR. If they're the same, exit. Otherwise, go to step i.
   e. make git store this script: `chmod +x wpi.sh ; git add wpi.sh`
   f. commit the script and build changes: `git commit -am "enable WPI" ; git push origin wpi-enabled`
   g. execute the script (this may take a while): `./wpi.sh`
      If the Checker Framework crashes, you might need to update to a newer version (on all branches).
   h. Record, under "Warnings after WPI" in the project's tab in the spreadsheet at
      https://docs.google.com/spreadsheets/d/1r_NhumolEp5CiOL7CmsvZaa4-FDUxCJXfswyJoKg8uM/ ,
      the number of errors issued by the typecheckers and which 
      typechecker issued the error.

7. Create a branch for the code with inferred annotations
   a. Create a branch: `git checkout -b wpi-annotations annotation-statistics`
   b. Create a new directory for the inferred ajava files: `mkdir wpi-annotations`
   c. copy all the ajava files: `rsync -r ${WPITEMPDIR}/ wpi-annotations`
   d. commit the results: `git add wpi-annotations/** ; git commit -am "WPI annotations" ; git push origin wpi-annotations`

8. Measure the number of annotations that WPI inferred, by checker:
    a. switch to the `wpi-annotations` branch: `git checkout wpi-annotations`
    b. create a copy of the script `compute-annos-inferred.sh` in the target project directory
    c. modify the variables at the beginning of the script as appropriate for the target project[[TODO: I think it would be better to take those variables as arguments if possible, to avoid the need to make a new version of the script.  The advantage of having a concrete script is that in the future it would not be necessary to know which arguments to pass.  But the concrete script could also be just an invocation of the master `compute-annos-inferred.sh` in the paper repository.]]
    d. run the script
    e. transcribe the output after "====== COMBINED RESULTS =======" is printed to the spreadsheet, combining rows that mention the same annotation (this happens when e.g., different @RequiresQualifier annotations are inferred by different checkers)
    f. commit and push the script: `git add compute-annos-inferred.sh ; git commit -m "inference output summarization script" ; git push origin wpi-annotations`

9. Measure the percentage of hand-written annotations that WPI inferred
    a. copy the experiments directory's `format.sh` to `format-mycopy.sh` found in the experiments directory into your project's top level directory. 
    b. download the `google-java-format.jar`.[[TODO: I think the script should do this.]] The link for it is in the script's documentation. Confirm the variable path in the script aligns with the location of your download.
    c. change the variable, `WPI_RESULTS_DIR` to the path of your project's WPI annotations `wpi-annotations`.[[TODO: Why not make this a command-line argument, or computing it from other information that is available?]]
    d. confirm that the `JAVA_SRC_DIR` is appropriate to your project's file tree which may look something like `./src/main/java/` and should contain `your-project.java` in the lowest directory, modify if needed.[[TODO: command-line argument instead?]]
    e. run the script `./format-mycopy.sh`.
    f. copy[[TODO: Here and elsewhere, instead of giving an English description of the high-level operation to perform, give a concrete command line that can be cut-and-pasted, reducing errors.]] outputs of this experimental procedure into (`/main/experiments/inferred-annos-counter/inputExamples`). This can be done by creating a directory in (`/inferred-annos-counter/inputExamples`) with the name of your project and two sub folders, `generated` and `human-written`. (This and following steps are optional. Use as input for downstream tools on small projects only).
    g. copy all of the contents in your `$WPITEMPDIR` directory used in the previous steps into the `generated` subfolder. 
    h. copy all of the human-written code (the human-annotated, i.e. original, code, but with a formatter run over it) from your project's source folder (e.g., ./src/main/java/ in a Gradle project) into the `human-written` directory that was created
    i. Run the InferredAnnosCounter.
      i. Go to the working directory of InferredAnnosCounter (ie, ./experiments/inferred-annos-counter, where . is the root of the repository containing this document).
      ii. Run the InferredAnnosCounter, with the first argument being the absolute path of the .java file, and other arguments being the absolute path of the .ajava files produced by the WPI. The program assumes that a formatter has been applied, so it is important to do so before passing the files as input. InferredAnnosCounter only takes one .java file at a time. So in case it is needed to run multiple .java files with corresponding .ajava files, InferredAnnosCounter needs to be invoked multiple times. [[ TODO: this should be scripted using a find/exec to locate all the Java files and then run the following command on them.. ]] The way to run InferredAnnosCounter is like this: ```cd experiments/inferred-annos-counter ``` (going to the working directory) and then ``` ./gradlew run --args="(a path to the .java file) (one or more paths to the .ajava files)" ```. The result will not be in alphabetical order.
      iii. Record the result in project-specific tab of the speadsheet at https://docs.google.com/spreadsheets/d/1r_NhumolEp5CiOL7CmsvZaa4-FDUxCJXfswyJoKg8uM/edit#gid=0.  
    
12. Copy summary numbers from the project-specific spreadsheet page to the "summary" tab at https://docs.google.com/spreadsheets/d/1r_NhumolEp5CiOL7CmsvZaa4-FDUxCJXfswyJoKg8uM/edit#gid=0, and color code the project row green once it is finished.
