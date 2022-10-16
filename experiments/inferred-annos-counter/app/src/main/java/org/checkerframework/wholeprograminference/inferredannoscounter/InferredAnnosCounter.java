package org.checkerframework.wholeprograminference.inferredannoscounter;

import org.checkerframework.common.value.qual.MinLen;
import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The entry point for the inferred annos counter. Pass it a list of files, as detailed in README.md
 * at the top-level of this source tree.
 */
public class InferredAnnosCounter {

  /**
   * For example, if we have @Annotation] coming from the diff algorithm, we will extract the @Annotation part.
   */
  static String ExtractHumanAnno( @MinLen(2) String Anno) {
    /// this is for cases such as ArrayList<@Anno(abcd)...., we only want the "@Anno(abcd)" part.
    int index1 = Anno.indexOf("@");
    // this is for cases such as @Option(abc), we only want the "@Option" part.
    int indexOfLastElement = 1;
    boolean CheckJustAlphabet=true;
    for (int county = index1 + 1; county < Anno.length(); county++) {
      if (!Character.isLetter(Anno.charAt(county))) {
        CheckJustAlphabet=false;
        indexOfLastElement = county;
        break;
      }
      indexOfLastElement = county;
    }
    if (CheckJustAlphabet)
    {
        Anno = Anno.substring(index1, indexOfLastElement+1);
    }
    else
    {
        Anno = Anno.substring(index1, indexOfLastElement);
    }
    return Anno;
  }

  /**
   * For example, if we have @org.checkerframwork....Annotation, we will extract the @Annotation part.
   */
  static String ExtractCompAnno(String Anno) {
    //for example, if we have @org.checkerframework.dataflow.qual.SideEffectFree, we will extract the "@SideEffectFree"
    String[] breakk = Anno.split("[.]");
    int size = breakk.length;
    String notic = breakk[size - 1];
    int indexOfLastElement = 0;
    for (int county = 0; county < notic.length(); county++) {
      if (!Character.isLetter(notic.charAt(county))) {
        break;
      }
      indexOfLastElement++;
    }
    if (indexOfLastElement <= notic.length()) {
      notic = notic.substring(0, indexOfLastElement);
    }
    notic = "@" + notic;
    return notic;
  }
  /**
   * The main entry point. Running this outputs the percentage of annotations in some source file
   * that were inferred by WPI.
   *
   * @param args the files. The first element is the original source file. All remaining elements
   *     should be corresponding .ajava files produced by WPI. This program assumes that all inputs
   *     have been converted to some unified formatting style to eliminate unnecessary changes
   *     (e.g., by running gjf on each input).
   */
  public static void main(String[] args) {
    if (args.length <= 1) {
      throw new RuntimeException(
          "Provide at least one .java file and one or more" + ".ajava files.");
    }
    List<String> originalFile = new ArrayList<String>();
    List<String> ImportantAnno = new ArrayList<String>();
    int count = 0;
    // specific annotations and the number of computer-written files missing them
    Map<String, Integer> AnnoLocate = new HashMap<String, Integer>();
    // the name of the types of annotation and their numbers in the human-written file
    Map<String, Integer> AnnoCount = new HashMap<String, Integer>();
    /* the name of the types of annotations and their "correct" numbers (meaning the number of annotations of that
    type not missed by computer-written files) */
    Map<String, Integer> AnnoSimilar = new HashMap<String, Integer>();
    File file = new File(args[0]);
    try (FileReader fr = new FileReader(file)) {
      String[] words = null;
      BufferedReader br = new BufferedReader(fr);
      String str;
      // lines counter, since somehow DiffAlgorithm counts the first line as 0
      int pos = -1;
      boolean checkcomment=true;
      while ((str = br.readLine()) != null) {
        pos++;
        originalFile.add(str);
        checkcomment=true;
        // Split the word using space
        words = str.split(" ");
        for (int i = 0; i < words.length; i++) {
          String word = words[i];
          // the next two ifs are to check if "word" is a valid annotation
          if (word.length() != 0) {
            int prePos=i-1;
            if (prePos>=0) {
              if (words[prePos].equals("*") || words[prePos].equals("//"))
              {
                checkcomment=false;
              }
            }
            if (word.contains("@") && checkcomment) {
              word = ExtractHumanAnno(word);
              // get the position of that annotation in the code
              String posi = String.valueOf(pos);
              if (AnnoCount.containsKey(word)) {
                int numberOfAnno = AnnoCount.get(word);
                AnnoCount.put(word, numberOfAnno + 1);
              } else {
                AnnoCount.put(word, new Integer(1));
              }
              AnnoSimilar.put(word, new Integer(0));
              // we want the keys in the map AnnoLocate has this following format: type_position
              String annoLocate = word + "_" + posi;
              AnnoLocate.put(annoLocate, new Integer(0));
            }
          }
        }
      }
      fr.close();
    } catch (Exception e) {
      throw new RuntimeException("Could not read file: " + args[0] + ". Check that it exists?");
    }
    List<Patch<String>> diffs = new ArrayList<>(args.length - 1);
    for (int i = 1; i < args.length; ++i) {
      file=new File(args[i]);
      try (FileReader fr = new FileReader(file)) {
        List<String> newFile = new ArrayList<String>();
        String[] words = null;
        BufferedReader br = new BufferedReader(fr);
        String str;
        String search = "@org.checkerframework";
        boolean checkcomment=true;
        while ((str = br.readLine()) != null) {
          newFile.add(str);
          words = str.split(" ");
          checkcomment = true;
          for (int in = 0; in < words.length; in++) {
            String word = words[in];
            // the next four ifs are to check if "word" is a valid annotation
            if (word.length() != 0) {
              int prePos = in - 1;
              if (prePos >= 0) {
                if (words[prePos].equals("*") || words[prePos].equals("/")) {
                  checkcomment = false;
                }
              }
              // finding string that has annotation but not belong to a comment
              if (word.contains(search) && checkcomment) {
                  /* for an annotation such as "@org.checkerframework...Nullable", we will extract the
                  "@Nullable" part */
                word = ExtractCompAnno(word);
                ImportantAnno.add(word);
              }
            }
          }
        }
        diffs.add(DiffUtils.diff(originalFile, newFile));
      } catch (Exception e) {
        throw new RuntimeException("Could not read file: " + args[i] + ". Check that it exists?");
      }
    }

    for (int i = 0; i < args.length - 1; i++) {
      Patch<String> patch = diffs.get(i);
      boolean notcomment = false;
      for (AbstractDelta<String> delta : patch.getDeltas()) {
        // get the delta in string format
        String deltaInString = delta.toString();
        notcomment = true;
        // we change the delta output to a string, then break that string into different parts
        List<String> myList = new ArrayList<String>(Arrays.asList(deltaInString.split(" ")));
        // just take the delta with annotations into consideration
        if (deltaInString.contains("@")) {
          // get the position of that annotation in the delta, which is something like "5," or "6,".
          String pos = myList.get(2);
          // take the "," out
          String newpos = "";
          if (pos.length() > 1) newpos = pos.substring(0, pos.length() - 1);
          String result = "";
          for (String element : myList) {
            // we dont take differences in the comment section into consideration
            if (element.equals("[")) {
              notcomment=true;
            }
            if (element.equals("//")) {
              notcomment = false;
            }
            if (notcomment && element.contains("@")) {
              /**
               * The Diff algorithm will state that "@org.checkerframework.dataflow.qual.Pure" is different from
               * "@Pure". We will let it do that and update the data of AnnoLocate based on that observation,
               * which is to add the value of the @Pure key by 1. But we will add a piece of codes that see
               * "@org.checkerframework.dataflow.qual.Pure" as "@Pure" and look it up on AnnoLocate, then
               * decrease the value of that @Pure key in the AnnoLocate by 1.
               */
              if (element.contains("@org.checkerframework")) {
                String notic = ExtractCompAnno(element);
                notic = notic + "_" + newpos;
                if (AnnoLocate.containsKey(notic)) {
                  int value = AnnoLocate.get(notic);
                  value = value - 1;
                  AnnoLocate.put(notic, value);
                }
              } else {
                element = ExtractHumanAnno(element);
                int currLine=Integer.parseInt(newpos);
                // to match the one in AnnoLocate
                result = element + "_" + newpos;
                // update the data of AnnoLocate
                if (AnnoLocate.containsKey(result)) {
                  int value = AnnoLocate.get(result);
                  value = value + 1;
                  AnnoLocate.put(result, value);
                }
                else {
                  while (currLine<AnnoLocate.size()) {
                    currLine++;
                    String curLine=String.valueOf(currLine);
                    result= element+ currLine;
                    if (AnnoLocate.containsKey(result)) {
                      int value = AnnoLocate.get(result);
                      value = value + 1;
                      AnnoLocate.put(result, value);
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
    // update the data of AnnoSimilar
    for (Map.Entry<String, Integer> me : AnnoLocate.entrySet()) {
      String AnnoName = me.getKey();
      /*if the number of computer-written code missing that element is less than the total number of codes written
      by computer, the at least one of those computer-written code must have gotten the annotation correct*/
      if (me.getValue() < args.length - 1) {
        // for example, if we have @Option_345, we will only need "@Option" since we want the
        // general type here
        int index = AnnoName.indexOf("_");
        if (index >= 0) AnnoName = AnnoName.substring(0, index);
        int value = AnnoSimilar.get(AnnoName);
        value = value + 1;
        AnnoSimilar.put(AnnoName, value);
      }
    }
    System.out.println();
    for (Map.Entry<String, Integer> e : AnnoCount.entrySet()) {
      int totalCount = e.getValue();
      String value = e.getKey();
      int correctCount = AnnoSimilar.get(value);
      if (ImportantAnno.contains(value)) {
        System.out.println(value + " got " + correctCount + "/" + totalCount);
      }
    }
    System.out.println();
  }
}
