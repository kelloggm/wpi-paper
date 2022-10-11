package org.checkerframework.wholeprograminference.inferredannoscounter;

import org.junit.Test;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import org.junit.Before;
import org.junit.After;
import org.junit.Rule;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.rules.ExpectedException;


public class InferredAnnosCounterTest {

  private final PrintStream standardOut = System.out;
  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

  @Rule
  public final ExpectedException exception = ExpectedException.none();

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
    InferredAnnosCounter.main(new String[]{});
  }

  @Test
  public void throwsRunTimeExceptionForInvalidInputFiles() {
    exception.expect(RuntimeException.class);
    exception.expectMessage("Could not read file: " + "meaningless.java" + ". Check that it exists?");
    InferredAnnosCounter.main(new String[]{"meaningless.java", "testbca.ajava"});
  }

  @Test
  public void MatchThreeAnnotations() {
    InferredAnnosCounter.main(new String[]{"MatchThreeAnnotations.java", "MatchThreeAnnotations.ajava"});
    String line1="@Pure got 1/1";
    String line2="@NonNull got 1/1";
    String line3="@SideEffectFree got 1/1";
    assertTrue(outputStreamCaptor.toString().trim().contains(line1));
    assertTrue(outputStreamCaptor.toString().trim().contains(line2));
    assertTrue(outputStreamCaptor.toString().trim().contains(line3));
  }

  @Test
  public void ignoreOneAnnoInCommentOfComputer() {
    InferredAnnosCounter.main(new String[]{"ignoreOneAnnoInComment.java", "ignoreOneAnnoInComment.ajava"});
    String line1="@Pure got 1/1";
    String line2="@NonNull got 1/1";
    String line3="@SideEffectFree got 1/1";
    assertTrue(outputStreamCaptor.toString().trim().contains(line1));
    assertTrue(!outputStreamCaptor.toString().trim().contains(line2));
    assertTrue(outputStreamCaptor.toString().trim().contains(line3));
  }

  @Test
  public void commentInMiddle() {
    InferredAnnosCounter.main(new String[]{"commentInMiddle.java", "commentInMiddle.ajava"});
    String line1="@Pure got 1/1";
    String line2="@NonNull got 1/1";
    String line3="@SideEffectFree got 1/1";
    assertTrue(outputStreamCaptor.toString().trim().contains(line1));
    assertTrue(!outputStreamCaptor.toString().trim().contains(line2));
    assertTrue(outputStreamCaptor.toString().trim().contains(line3));
  }

}
