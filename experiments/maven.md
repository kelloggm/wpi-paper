## What to do when Maven swallows the output of Annotation Statistics

Maven doesn't permit annotation processors (or javac itself) to print to
standard out. I don't think there's a way to avoid this (based on web searches),
so we need an alternative to running AnnotationStatistics via the build system.

### Alternative 1: -X + javac

If this works, it's the easiest way to collect the numbers (but, it doesn't always
work). Follow these steps (after enabling AnnotationStatistics in the build system as usual):
1. clean the project (`mvn clean`)
2. re-run the compilation in debug mode and pipe the output to a file (`mvn -X compile &> out`)
3. locate the arguments passed to javac in `out`. Maven prints "Command line options:" right before it prints
this list, so I recommend searching for that. Warning: in multi-project builds, there might be more than one
of these in the output, so you should check all of them.
4. copy the options passed to javac into a new shell script, and precede them with `javac`
5. run this script. You should get output from AnnotationStatistics.

### Alternative 2: count by hand

If that doesn't work, the easiest way is to count annotations by hand. To do this, follow these steps:
1. compile a list of relevant annotations. To do that, first determine which checkers are running. Then,
visit their manual sections in the Checker Framework [manual](checkerframework.org/manual). Each manual
section lists the relevant annotations for the checker.
2. place these annotations, one per line, in a text file. Call this file `annos.txt`.
3. then run something like the following: for anno in `cat annos.txt`; do rg $anno *; done
4. count the results by hand

TODO: improve the second method: it's fine for dealing with hand-annotated projects, but doesn't
scale to ajava files. Needs to be automated.