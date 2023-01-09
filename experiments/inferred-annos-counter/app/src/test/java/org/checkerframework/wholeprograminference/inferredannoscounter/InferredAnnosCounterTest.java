/*
 * This Java source file was generated by the Gradle 'init' task.
 */
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

  @Before
  public void setUp() {
    System.setOut(new PrintStream(outputStreamCaptor));
  }

  @After
  public void tearDown() {
    System.setOut(standardOut);
  }

  @Test
  public void throwsRunTimeExceptionForNoInputFiles() {
    exception.expect(RuntimeException.class);
    exception.expectMessage("Provide at least one .java file and one or more" + ".ajava files.");
    InferredAnnosCounter.main(new String[] {});
  }

  @Test
  public void throwsRunTimeExceptionForInvalidInputFiles() {
    exception.expect(RuntimeException.class);
    exception.expectMessage(
        "Could not read file: " + "meaningless.java" + ". Check that it exists?");
    InferredAnnosCounter.main(new String[] {"meaningless.java", "testbca.ajava"});
  }

  @Test
  public void matchThreeAnnotations() {
    InferredAnnosCounter.main(
        new String[] {
          "testCases/MatchThreeAnnotations.java", "testCases/MatchThreeAnnotations.ajava"
        });
    String line1 = "@Pure got 1/1";
    String line2 = "@NonNull got 1/1";
    String line3 = "@SideEffectFree got 1/1";
    assertTrue(outputStreamCaptor.toString().trim().contains(line1));
    assertTrue(outputStreamCaptor.toString().trim().contains(line2));
    assertTrue(outputStreamCaptor.toString().trim().contains(line3));
  }

  @Test
  public void ignoreOneAnnoInCommentOfComputer() {
    InferredAnnosCounter.main(
        new String[] {
          "testCases/ignoreOneAnnoInComment.java", "testCases/ignoreOneAnnoInComment.ajava"
        });
    String line1 = "@Pure got 1/1";
    String line2 = "@NonNull got 1/1";
    String line3 = "@SideEffectFree got 1/1";
    assertTrue(outputStreamCaptor.toString().trim().contains(line1));
    assertTrue(!outputStreamCaptor.toString().trim().contains(line2));
    assertTrue(outputStreamCaptor.toString().trim().contains(line3));
  }

  @Test
  public void commentInMiddle() {
    InferredAnnosCounter.main(
        new String[] {"testCases/CommentInMiddle.java", "testCases/CommentInMiddle.ajava"});
    String line1 = "@Pure got 1/1";
    String line2 = "@NonNull got 1/1";
    String line3 = "@SideEffectFree got 1/1";
    assertTrue(outputStreamCaptor.toString().trim().contains(line1));
    assertTrue(!outputStreamCaptor.toString().trim().contains(line2));
    assertTrue(outputStreamCaptor.toString().trim().contains(line3));
  }

  @Test
  public void annotationInString() {
    InferredAnnosCounter.main(
        new String[] {"testCases/AnnotationInString.java", "testCases/AnnotationInString.ajava"});
    String line1 = "@Pure got 1/1";
    String line2 = "@NonNull got 1/1";
    String line3 = "@SideEffectFree got 1/1";
    assertTrue(outputStreamCaptor.toString().trim().contains(line1));
    assertTrue(!outputStreamCaptor.toString().trim().contains(line2));
    assertTrue(outputStreamCaptor.toString().trim().contains(line3));
  }

  @Test
  public void annotationInMiddleOfADeclaration() {
    InferredAnnosCounter.main(
        new String[] {
          "testCases/AnnotationInMiddleOfADeclaration.java",
          "testCases/AnnotationInMiddleOfADeclaration.ajava"
        });
    String line1 = "@Pure got 1/1";
    String line2 = "@NonNull got 1/1";
    String line3 = "@SideEffectFree got 1/1";
    assertTrue(outputStreamCaptor.toString().trim().contains(line1));
    assertTrue(outputStreamCaptor.toString().trim().contains(line2));
    assertTrue(outputStreamCaptor.toString().trim().contains(line3));
  }

  @Test
  public void annotationWithArgument() {
    InferredAnnosCounter.main(
        new String[] {
          "testCases/AnnotationWithArgument.java", "testCases/AnnotationWithArgument.ajava"
        });
    String line1 = "@EnsuresNonNull got 1/2";
    String line2 = "@EnsuresCalledMethods got 1/2";
    assertTrue("Didn't find the correct number of @EnsuresNonNull annotations; expected 1/2, got: " + outputStreamCaptor,
            outputStreamCaptor.toString().trim().contains(line1));
    assertTrue("Didn't find the correct number of @EnsuresCalledMethods annotations; expected 1/2, got: " + outputStreamCaptor,
            outputStreamCaptor.toString().trim().contains(line2));
  }
}
