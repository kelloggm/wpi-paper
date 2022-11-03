package org.checkerframework.wholeprograminference.inferredannoscounter;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class InferredAnnosCounterTest {

  private final PrintStream standardOut = System.out;
  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

  @Rule public final ExpectedException exception = ExpectedException.none();

  /**
   * This is to setup the outputStreamCaptor before each test to read what printed out by the main
   * method.
   */
  @Before
  public void setUp() {
    System.setOut(new PrintStream(outputStreamCaptor));
  }

  /** This is to close the outputStreamCaptor after each test. */
  @After
  public void tearDown() {
    System.setOut(standardOut);
  }

  /**
   * Test that the main method crashes with a useful error message when no input files are supplied.
   * Depends on the specific error message that is displayed.
   */
  @Test
  public void throwsRunTimeExceptionForNoInputFiles() {
    exception.expect(RuntimeException.class);
    exception.expectMessage("Provide at least one .java file and one or more" + ".ajava files.");
    InferredAnnosCounter.main(new String[] {});
  }

  /**
   * Test that the main method crashes with a useful error message when invalid input files are
   * supplied. Depends on the specific error message that is displayed.
   */
  @Test
  public void throwsRunTimeExceptionForInvalidInputFiles() {
    exception.expect(RuntimeException.class);
    exception.expectMessage(
        "Could not read file: " + "meaningless.java" + ". Check that it exists?");
    InferredAnnosCounter.main(new String[] {"meaningless.java", "testbca.ajava"});
  }

  /**
   * Test that the main method generates the correct output when there are three annotations in the
   * human-written files and three matching ones in the computer-generated files.
   */
  @Test
  public void MatchThreeAnnotations() {
    InferredAnnosCounter.main(
        new String[] {"MatchThreeAnnotations.java", "MatchThreeAnnotations.ajava"});
    String line1 = "@Pure got 1/1";
    String line2 = "@NonNull got 1/1";
    String line3 = "@SideEffectFree got 1/1";
    assertTrue(outputStreamCaptor.toString().trim().contains(line1));
    assertTrue(outputStreamCaptor.toString().trim().contains(line2));
    assertTrue(outputStreamCaptor.toString().trim().contains(line3));
  }

  /**
   * Test that the main method generates the correct output when there are three annotations in the
   * human-written file and three matching ones in the computer-generated files, but the @NonNull in
   * the computer-generated file is in a comment. The main method should print out only two
   * annotations.
   */
  @Test
  public void ignoreOneAnnoInCommentOfComputer() {
    InferredAnnosCounter.main(
        new String[] {"ignoreOneAnnoInComment.java", "ignoreOneAnnoInComment.ajava"});
    String line1 = "@Pure got 1/1";
    String line2 = "@NonNull got 1/1";
    String line3 = "@SideEffectFree got 1/1";
    assertTrue(outputStreamCaptor.toString().trim().contains(line1));
    assertTrue(!outputStreamCaptor.toString().trim().contains(line2));
    assertTrue(outputStreamCaptor.toString().trim().contains(line3));
  }

  /**
   * Test that the main method generates the correct output when there are three annotations in the
   * human-written file and three matching ones in the computer-generated files, but the @NonNull in
   * the human-written file is in a comment and in the same line with @Pure, which is not in a
   * comment. It is like this: "@Pure //@NonNull". The main method should only print out only two
   * annotations.
   */
  @Test
  public void commentInMiddle() {
    InferredAnnosCounter.main(new String[] {"commentInMiddle.java", "commentInMiddle.ajava"});
    String line1 = "@Pure got 1/1";
    String line2 = "@NonNull got 1/1";
    String line3 = "@SideEffectFree got 1/1";
    assertTrue(outputStreamCaptor.toString().trim().contains(line1));
    assertTrue(!outputStreamCaptor.toString().trim().contains(line2));
    assertTrue(outputStreamCaptor.toString().trim().contains(line3));
  }
}
