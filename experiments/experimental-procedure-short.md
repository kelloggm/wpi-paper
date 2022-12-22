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

2. Set a temporary directory for $WPITEMPDIR: 

  export WPITEMPDIR=\`pwd\`/tmp

3. From wpi-paper copy the `RunWPI.py` file to the project directory

4. Run `RunWPI.py` from terminal and pass the name of the project as argument. `python RunWPI.py icalavailable`

5. Follow instructions of that program.

6. Go to `wpi-paper/experiments/inferred-annos-counter/` and run inferred-annos-counter: From terminal `python RunIAC.py project-name`.

7. A csv file `AnnotationStats.csv` will be generated in the `wpi-paper/experiments/inferred-annos-counter/inputExamples/project-name/`. Copy summary numbers from the project-specific spreadsheet page to the "summary" tab, and color code the project row green once it is finished.


TODO: If an error was encountered in step 4.
