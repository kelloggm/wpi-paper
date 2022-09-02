## Inferred Annos Counter

This simple Java application is a utility for counting how many annotations
in a project are inferred when running Checker Framework whole-program inference
in .ajava mode. The utility works using a diff-based algorithm: as input,
it takes a single original annotated source file (e.g., `Foo.java`) and one or more corresponding
.ajava files produced by checkers for WPI 
(e.g., `Foo-org.checkerframework.checker.nullness.NullnessChecker.ajava`).
The files should be provided only after running both the original source file
and the .ajava files through a unified formatter, such as Google Java Format,
to remove formatting differences.

TODO: also include a link to a template script in the directory above this that automates
this for a simple project