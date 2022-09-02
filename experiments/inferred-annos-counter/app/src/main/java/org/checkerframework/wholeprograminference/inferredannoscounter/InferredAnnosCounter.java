package org.checkerframework.wholeprograminference.inferredannoscounter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.Patch;

/**
 * The entry point for the inferred annos counter.
 * Pass it a list of files, as detailed in
 * README.md at the top-level of this source tree.
 */
public class InferredAnnosCounter {
    /**
     * The main entry point. Running this outputs the percentage of annotations in some source file
     * that were inferred by WPI.
     *
     * @param args the files. The first element is the original source file. All remaining
     *             elements should be corresponding .ajava files produced by WPI. This program
     *             assumes that all inputs have been converted to some unified formatting style
     *             to eliminate unnecessary changes (e.g., by running gjf on each input).
     */
    public static void main(String[] args) {
        if (args.length <= 1) {
            throw new RuntimeException("Provide at least one .java file and one or more" +
                    ".ajava files.");
        }
        List<String> originalFile;
        try {
            originalFile = Files.readAllLines(new File(args[0]).toPath());
        } catch (IOException e) {
            throw new RuntimeException("Could not read file: " + args[0] + ". Check that it exists?");
        }
        List<Patch<String>> diffs = new ArrayList<>(args.length - 1);
        for (int i = 1; i < args.length; ++i) {
            try {
                diffs.add(DiffUtils.diff(originalFile, Files.readAllLines(new File(args[i]).toPath())));
            } catch (IOException e) {
                throw new RuntimeException("Could not read file: " + args[i] + ". Check that it exists?");
            }
        }
    }
}
