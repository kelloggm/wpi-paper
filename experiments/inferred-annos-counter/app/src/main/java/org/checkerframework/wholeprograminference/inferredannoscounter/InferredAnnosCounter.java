package org.checkerframework.wholeprograminference.inferredannoscounter;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.DeltaType;
import com.github.difflib.patch.Patch;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.checkerframework.checker.index.qual.IndexFor;

/**
 * The entry point for the inferred annos counter. Run this by passing arguments, with the first
 * being the path of the human-written file, and other arguments being the path of the .ajava files
 * produced by the WPI. The path here should be relative to "experiments\inferred-annos-counter."
 * The program assumes that a formatter has been applied, so it is important to run the script
 * format.sh in the directory "experiments" beforehand. InferredAnnosCounter only takes one
 * human-written source file at a time. So in case it is needed to run multiple human-written files
 * with corresponding computer-generated files, InferredAnnosCounter needs to be invoked multiple
 * times. The way to run InferredAnnosCounter is like this: cd experiments\inferred-annos-counter
 * (going to the working directory) and then gradle run --args = "(a path to the human-written file)
 * (one or more paths to the computer-generated files)". The result will not be in alphabetical
 * order.
 */
public class InferredAnnosCounter {

  /**
   * This enum classifies input lines. A line is OPEN if it contains the beginning of a multi-line
   * annotation, CLOSE if it contains the ending of a multi-line annotation. For other cases, it is
   * considered COMPLETE.
   */
  enum LineStatus {
    OPEN,
    COMPLETE,
    CLOSE
  }

  /**
   * This method returns true if the first not-a-whitespace character of a line is a dot. It returns
   * false in other cases
   *
   * @param line a line to be analyzed
   * @return true of the first non-whitespace character of that line is a dot, false otherwise.
   */
  private static boolean firstIsDot(String line) {
    String trimmed = line.trim();
    return trimmed.length() == 0 ? false : trimmed.charAt(0) == '.';
  }

  /**
   * If an annotation is too long, Google Java Format will make it multi-line. This method checks if
   * a line contains the beginning of those annotations.
   *
   * <p>For example, the first line contains the beginning of MonotonicNonNull:
   * static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker
   * .nullness.qual.MonotonicNonNull TimeZone tz2;
   *
   * @param line a line from the input file
   * @return true if it contains the beginning of a multi-line annotation
   */
  private static boolean checkGoogleFormatOpenCase(String line) {
    if (line.length() == 0) {
      return false;
    }
    String elements[] = line.split(" ");
    int n = elements.length;
    if (n >= 1 && elements[n - 1].contains("@org.checkerframework")) {
      String breaks[] = elements[n - 1].split("[.]");
      int numberOfParts = breaks.length;
      if (numberOfParts < 2) {
        throw new RuntimeException("Invalid annotation form in line: " + line);
      }
      return (!breaks[numberOfParts - 2].equals("qual"));
    }
    return false;
  }

  /**
   * If an annotation is too long, Google Java Format will make it multi-line. This method checks if
   * a line contains the ending of those annotations.
   *
   * <p>For example, the second line contains the ending of MonotonicNonNull:
   * static @org.checkerframework.checker.initialization.qual.Initialized @org.checkerframework.checker
   * .nullness.qual.MonotonicNonNull TimeZone tz2;
   *
   * @param line a line from the input file
   * @return true if it contains the ending of a multi-line annotation
   */
  private static boolean checkGoogleFormatCloseCase(String line) {
    return (line.length() > 0 && firstIsDot(line));
  }

  /**
   * This method checks the status of a line. If that line is the openning of a multi-line
   * annotation, we call it "Open". If the line is the closing of a multi-line annotation, we call
   * it "Close". And if the line does not contain any multi-line annotation, we call it "Complete".
   *
   * @param line a line from the input file
   * @return the status of that line
   */
  private static LineStatus checkLine(String line) {
    if (checkGoogleFormatOpenCase(line)) {
      return LineStatus.OPEN;
    }
    if (checkGoogleFormatCloseCase(line)) {
      return LineStatus.CLOSE;
    }
    int openParen = 0;
    int closeParen = 0;
    for (int i = 0; i < line.length(); i++) {
      char c = line.charAt(i);
      if (c == '(') {
        openParen++;
      }
      if (c == ')') {
        closeParen++;
      }
    }
    if (openParen < closeParen) {
      return LineStatus.CLOSE;
    }
    if (openParen > closeParen) {
      return LineStatus.OPEN;
    }
    return LineStatus.COMPLETE;
  }

  /**
   * This method reads the input files, and changes all the multi-line annotations into a single
   * line. This method returns a list, each element of that list is a line of the formatted file.
   *
   * @param fileName the name of the input file to be formatted
   * @return inputFiles a list containing lines of the formatted file
   */
  private static List annoMultiToSingle(String fileName) {
    List<String> inputFiles = new ArrayList<String>();
    File file = new File(fileName);
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String originalFileLine;
      String tempLine = "";
      boolean inProgress = false;
      while ((originalFileLine = br.readLine()) != null) {
        originalFileLine = ignoreComment(originalFileLine);
        if (inProgress) {
          if (checkLine(originalFileLine) == LineStatus.CLOSE) {
            /*
            There are two cases that an anotation is multi-line, either by Google Java Format or by default.
            For the first case, it's easy to understand that we don't want any space in the middle of an annotation.
            For the second case, it will be easier for the Diff Algorithm if there's no space in the bracket.
             */
            tempLine = tempLine + originalFileLine.trim();
            inputFiles.add(tempLine);
            inProgress = false;
            tempLine = "";
          } else {
            originalFileLine = originalFileLine.trim();
            tempLine = tempLine + originalFileLine;
          }
        } else if (checkLine(originalFileLine) == LineStatus.COMPLETE
            || !originalFileLine.contains("@")) {
          tempLine = tempLine + originalFileLine;
          inputFiles.add(tempLine);
          tempLine = "";
        } else if (checkLine(originalFileLine) == LineStatus.OPEN
            && originalFileLine.contains("@")) {
          tempLine = tempLine + originalFileLine;
          inProgress = true;
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("Could not read file: " + fileName + ". Check that it exists?");
    }
    return inputFiles;
  }

  /**
   * This method will put each annotation in a separate line.
   *
   * @param inputFiles a list containing lines of the input file after going through
   *     quickReadAndFormat
   * @return a list containing lines of the input files with each annotation in a separate line.
   */
  private static List eachAnnotationInOneSingleLine(List<String> inputFiles) {
    List<String> formated = new ArrayList<String>();
    for (int i = 0; i < inputFiles.size(); i++) {
      String line = inputFiles.get(i);
      String temp[] = line.split(" ");
      String resultLine = "";
      boolean inProgress = false;
      for (String element : temp) {
        int indexOfAnno = element.indexOf('@');
        if (indexOfAnno != -1 && !inProgress) {
          if (resultLine.length() > 0) {
            // sometimes the annotation can be in the middle of a declaration
            formated.add(resultLine + element.substring(0, indexOfAnno));
            element = element.substring(indexOfAnno, element.length());
          }
          resultLine = "";
          if (checkLine(element) == LineStatus.COMPLETE) {
            formated.add(element);
          } else {
            resultLine = resultLine + " " + element;
            inProgress = true;
          }
        } else {
          resultLine = resultLine + " " + element;
          if (inProgress && element.indexOf(')') != -1) {
            formated.add(resultLine);
            inProgress = false;
            resultLine = "";
          }
        }
      }
      formated.add(" " + resultLine);
    }
    return formated;
  }

  /**
   * This method takes a line, which contains at least one annotation, and return the first
   * annotation in that line.
   *
   * @param line a non-empty line containing at least one annotation
   * @return the annotation which the line begins with
   */
  private static String getAnnos(String line) {
    String[] temp = line.split(" ");
    String result = "";
    for (String word : temp) {
      if (word.length() >= 1) {
        int indexOfAnno = word.indexOf('@');
        if (indexOfAnno != -1) {
          int begin = indexOfAnno + 1;
          result = word.substring(begin, word.length());
          break;
        }
      }
    }
    return result;
  }

  /**
   * This method counts the number of annotations in a line
   *
   * @param line a line
   * @return the number of annotations in that line
   */
  private static int countAnnos(String line) {
    int count = 0;
    boolean checkinString = true;
    for (int i = 0; i < line.length(); i++) {
      if (line.charAt(i) == '\"') {
        if (checkinString) {
          checkinString = false;
        } else {
          checkinString = true;
        }
      }
      if (line.charAt(i) == '@' && checkinString) {
        count++;
      }
    }
    return count;
  }

  /**
   * This method checks if a particular index in a line belongs to a string literal
   *
   * @param line a line
   * @param Index index of line
   * @return true if Index belong to a string literal, false otherwise
   */
  private static boolean checkInString(@IndexFor("#2") int Index, String line) {
    int before = 0;
    int after = 0;
    for (int i = 0; i < Index; i++) {
      if (line.charAt(i) == '\"') {
        before++;
      }
    }
    for (int i = Index + 1; i < line.length(); i++) {
      if (line.charAt(i) == '\"') {
        after++;
      }
    }
    if (before % 2 == 0 && after % 2 == 0) {
      return true;
    }
    return false;
  }

  /**
   * This method trims out all the comments in a line from the input files
   *
   * @param line a line from the input files
   * @return that line without any comment
   */
  private static String ignoreComment(String line) {
    int indexComment = line.length() + 1;
    String finalLine = line;
    int indexDash = line.indexOf("// ");
    int indexStar = line.indexOf("*");
    int indexDashStar = line.indexOf("/*");
    if (indexDash != -1) {
      if (indexDash == 0) {
        finalLine = line.substring(0, indexDash);
      } else {
        finalLine = line.substring(0, indexDash - 1);
      }
    }
    if (indexDashStar != -1) {
      finalLine = line.substring(0, indexDashStar);
    }
    if (indexStar != -1) {
      finalLine = line.substring(0, indexStar);
    }
    return finalLine;
  }

  /**
   * This method formats a line that may or may not contain fully-qualified annotation. The way this
   * method formats that line is to change all annotations written in the fully-qualified format to
   * the simple format. For example, changing "@org.checkerframework.dataflow.qual.Pure" to "@Pure."
   * This method should be applied before passing a line to the Diff algorithm.
   *
   * @param line a line that belongs to the input files
   * @return the same line with all the annotations being changed to the simple format.
   */
  private static String extractCheckerPackage(String line) {
    String[] temp = line.split(" ");
    String result = line;
    if (line.length() != 0) {
      for (String word : temp) {
        int indexOfPackage = word.indexOf("org.");
        if (indexOfPackage != -1) {
          int indexOfParen = word.indexOf('(');
          if (indexOfParen != -1) {
            String insideParen = word.substring(indexOfParen + 1, word.length());
            if (insideParen.contains("org.")) {
              String newInsideParen = extractCheckerPackage(insideParen);
              String newWord = word.replace(insideParen, newInsideParen);
              result = result.replace(word, newWord);
              word = newWord;
            }
          }
          String originalPart = word.substring(indexOfPackage, word.length());
          String[] tempo = originalPart.split("[.]");
          String tempResult = tempo[tempo.length - 1];
          String newWord = word.replace(originalPart, tempResult);
          result = result.replace(word, newWord);
        }
      }
    }
    return result;
  }

  /**
   * This method trims out the parenthesized part in an annotation, for example, @Annotation(abc)
   * will be changed to @Annotation.
   *
   * <p>This method needs to be used with care. We want to use it to update the final result. This
   * method should not be used for any list or string that will become the input of the Diff
   * algorithm. If we do that, the Diff algorithm will not be able to recognize any potential
   * difference in the parentheses between an annotation written by human and an annotation
   * generated by the computer anymore.
   *
   * @param anno the annotation which will be trimmed
   * @return that annotation without the parenthesized part
   */
  private static String trimParen(String anno) {
    int para = anno.indexOf("(");
    if (para == -1) {
      return anno;
    }
    return anno.substring(0, para);
  }

  /**
   * This method returns a List containing all the annotations belonging to a line.
   *
   * @param str a line
   * @return a Linked List containing all annotations of str.
   */
  private static List extractString(String str) {
    List<String> result = new ArrayList<String>();
    int countAnno = countAnnos(str);
    String temp = str;
    for (int i = 0; i < countAnno; i++) {
      int index1 = temp.indexOf('@');
      if (index1 == -1) {
        throw new RuntimeException(
            "The extractString method relies on the countAnnos method. Either the countAnnos method is wrong"
                + "or it was not called properly");
      }
      String tempAnno = getAnnos(temp);
      if (checkInString(index1, temp)) {
        if (tempAnno.contains("(")) {
          if (temp.contains(")")) {
            tempAnno = temp.substring(index1 + 1, temp.indexOf(')') + 1);
          } else {
            tempAnno = temp.substring(index1 + 1, temp.length());
          }
          result.add(tempAnno);
        } else {
          result.add(tempAnno);
        }
      }
      temp = temp.substring(index1 + 1, temp.length());
    }
    return result;
  }

  /**
   * The main entry point. Running this outputs the percentage of annotations in some source file
   * that were inferred by WPI.
   *
   * <p>-param args the files. The first element is the original source file. All remaining elements
   * should be corresponding .ajava files produced by WPI. This program assumes that all inputs have
   * been converted to some unified formatting style to eliminate unnecessary changes (e.g., by
   * running google java format on each input).
   */
  public static void main(String[] args) {
    int fileCount = 0;
    List<String> checkerPackage = new ArrayList<String>();
    File file1 = new File("type-qualifiers.txt");
    try (FileReader fr = new FileReader(file1)) {
      BufferedReader br = new BufferedReader(fr);
      String str;
      while ((str = br.readLine()) != null) {
        // the extractCheckerPackage will keep the char element of the string, such as '@' or '"'.
        // So we need to add a
        // space here since the element in this txt does not have a '@'.
        str = extractCheckerPackage('@' + str);
        str = str.replaceAll("\\s", "");
        checkerPackage.add(str);
      }
    } catch (Exception e) {
      throw new RuntimeException("Could not read type-qualifiers.txt, check if it exists?");
    }

    if (args.length <= 1) {
      throw new RuntimeException(
          "Provide at least one .java file and one or more" + ".ajava files.");
    }

    // These variables are maintained throughout:

    // The original file, reformatted to remove comments and clean up annotation names (i.e., remove
    // package names),
    // etc.
    List<String> originalFile = new ArrayList<>();
    // specific annotations and the number of computer-written files missing them
    Map<String, Integer> annoLocate = new HashMap<>();
    // the name of the types of annotation and their numbers in the human-written file
    Map<String, Integer> annoCount = new HashMap<>();
    /* the name of the types of annotations and their "correct" numbers (meaning the number of annotations of that
    type not missed by computer-written files) */
    Map<String, Integer> annoSimilar = new HashMap<>();
    // the number of lines in the original file
    int originalFileLineCount = 0;
    List<String> preFile = annoMultiToSingle(args[0]);
    List<String> inputFile = eachAnnotationInOneSingleLine(preFile);
    int originalFileLineIndex = -1;
    // Read the original file once to determine the annotations that written by the human.
    for (String originalFileLine : inputFile) {
      originalFileLineIndex++;
      originalFileLine = ignoreComment(originalFileLine);
      originalFileLine = extractCheckerPackage(originalFileLine);
      // since it's too difficult to keep the length of whitespace at the beginning of each line the
      // same
      originalFileLine = originalFileLine.trim();
      originalFile.add(originalFileLine);
      String specialAnno = trimParen(originalFileLine);
      if (checkerPackage.contains(specialAnno)) {
        if (originalFileLine.contains("(") && !originalFileLine.contains("{")) {
          originalFileLine = originalFileLine.replace("(", "({");
          originalFileLine = originalFileLine.replace(")", "})");
        }
        if (annoCount.containsKey(specialAnno)) {
          int numberOfAnno = annoCount.get(specialAnno);
          annoCount.put(specialAnno, numberOfAnno + 1);
        } else {
          annoCount.put(specialAnno, 1);
        }
        annoSimilar.put(specialAnno, 0);
        // we want the keys in the map annoLocate has this following format: type_position
        annoLocate.put(originalFileLine + "_" + originalFileLineIndex, 0);
      }
      originalFileLineCount = originalFileLineIndex;
    }
    // Iterate over the arguments from 1 to the end and diff each with the original,
    // putting the results into diffs.
    List<Patch<String>> diffs = new ArrayList<>(args.length - 1);
    // Iterate over the arguments from 1 to the end and diff each with the original,
    // putting the results into diffs.
    for (int i = 1; i < args.length; ++i) {
      List<String> preFile2 = annoMultiToSingle(args[i]);
      List<String> inputFile2 = eachAnnotationInOneSingleLine(preFile2);
      List<String> newFile = new ArrayList<>();
      for (String ajavaFileLine : inputFile2) {
        ajavaFileLine = ignoreComment(ajavaFileLine);
        ajavaFileLine = extractCheckerPackage(ajavaFileLine);
        ajavaFileLine = ajavaFileLine.trim();
        newFile.add(ajavaFileLine);
      }
      diffs.add(DiffUtils.diff(originalFile, newFile));
    }
    // Iterate over the list of diffs and process each. There must be args.length - 1 diffs, since
    // there is one diff between args[0] and each other element of args.
    for (int i = 0; i < args.length - 1; i++) {
      Patch<String> patch = diffs.get(i);
      for (AbstractDelta<String> delta : patch.getDeltas()) {
        // get the delta in string format
        String deltaInString = delta.toString();
        // just take the delta with annotations into consideration
        // INSERT type indicates that the annotations only appear in the computer-generated files.
        // So we don't take it into consideration.
        if (deltaInString.contains("@") && delta.getType() != DeltaType.INSERT) {
          List<String> myList = delta.getSource().getLines();
          // get the position of that annotation in the delta, which is something like "5," or "6,".
          int position = delta.getSource().getPosition();
          String result = "";
          for (String element : myList) {
            if (element.contains("@")) {
              // in case there are other components in the string element other than the
              // annotation itself
              List<String> annoList = extractString(element);
              for (String anno : annoList) {
                // to match the one in AnnoLocate
                result = "@" + anno + "_" + position;
                // update the data of AnnoLocate
                if (annoLocate.containsKey(result)) {
                  int value = annoLocate.get(result);
                  annoLocate.put(result, value + 1);
                } else {
                  int tempPosition = position;
                  while (tempPosition < originalFileLineCount) {
                    tempPosition++;
                    result = "@" + anno + "_" + tempPosition;
                    if (annoLocate.containsKey(result)) {
                      int value = annoLocate.get(result);
                      annoLocate.put(result, value + 1);
                      break;
                    }
                  }
                }
              }
            }
          }
        }
      }
    }

    // Update the data of AnnoSimilar.
    for (Map.Entry<String, Integer> me : annoLocate.entrySet()) {
      String annoName = me.getKey();
      /* If the number of computer-written code missing that element is less than the total number of codes written
      by computer, the at least one of those computer-written code must have gotten the annotation correct. */
      if (me.getValue() < args.length - 1) {
        // For example, if we have @Option_345, we will only need "@Option" since we want the
        // general type here.
        int index = annoName.indexOf("_");
        if (index >= 0) {
          annoName = annoName.substring(0, index);
        }
        annoName = trimParen(annoName);
        int value = annoSimilar.get(annoName);
        value = value + 1;
        annoSimilar.put(annoName, value);
      }
    }

    // Output the results.
    System.out.println();
    for (Map.Entry<String, Integer> e : annoCount.entrySet()) {
      int totalCount = e.getValue();
      String value = e.getKey();
      int correctCount = annoSimilar.get(value);
      System.out.println(value + " got " + correctCount + "/" + totalCount);
    }
  }
}
