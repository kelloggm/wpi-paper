package org.checkerframework.wholeprograminference.inferredannoscounter;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** This integration test checks that the IAC produces the expected outputs on ICalAvailable. */
public class CollectionUtilsTest {

  private final PrintStream standardOut = System.out;
  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

  @Before
  public void setUp() {
    System.setOut(new PrintStream(outputStreamCaptor));
  }

  @After
  public void tearDown() {
    System.setOut(standardOut);
  }

  private final String EXPECTED_OUTPUT = "@NonNull got 0/10\n";

  @Test
  public void iCalAvailableTest() {
    InferredAnnosCounter.main(
        new String[] {
          "../inputExamples/RxNorm-explorer/human-written/main/java/gov/fda/nctr/util/CollectionUtils.java",
          "../inputExamples/RxNorm-explorer/generated/gov/fda/nctr/util/CollectionUtils-org.checkerframework.checker.nullness.NullnessChecker.ajava",
        });

    assertTrue(
        "CollectionUtilsTest got wrong output.\n"
            + "Expected: "
            + EXPECTED_OUTPUT
            + "\nActual: "
            + outputStreamCaptor.toString(),
        outputStreamCaptor
            .toString()
            .replaceAll("\\s", "")
            .contentEquals(EXPECTED_OUTPUT.replaceAll("\\s", "")));
  }
}
