package edu.pdx.cs410J.chlin;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A unit test for code in the <code>Project1</code> class.  This is different
 * from <code>Project1IT</code> which is an integration test and can capture data
 * written to {@link System#out} and the like.
 */
class Project1Test {

  @Test
  void readmeCanBeReadAsResource() throws IOException {
    try (
      InputStream readme = Project1.class.getResourceAsStream("README.txt")
    ) {
      assertThat(readme, not(nullValue()));
      BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
      String line = reader.readLine();
      String readmeInput = line + "\n";
      while ((line = reader.readLine()) != null)  {
        readmeInput += line + "\n";
      }
      assertEquals(readmeInput, Project1.printReadMe());
    }
  }

  @Test
  void testIsValidDateAndTime() {
    String dateAndTime = "3/15/2023 10:89";
    assertFalse(Project1.isValidDateAndTime(dateAndTime));
    String dateAndTime2 = "3/15/2023 28:19";
    assertFalse(Project1.isValidDateAndTime(dateAndTime2));
    String dateAndTime3 = "3/15/20233 10:29";
    assertFalse(Project1.isValidDateAndTime(dateAndTime3));
    String dateAndTime4 = "3/0/2023 10:29";
    assertFalse(Project1.isValidDateAndTime(dateAndTime4));
    String dateAndTime5 = "30/15/2023 10:29";
    assertFalse(Project1.isValidDateAndTime(dateAndTime5));
    String dateAndTime6 = "03/15/2023 10:29";
    assertTrue(Project1.isValidDateAndTime(dateAndTime6));
    String dateAndTime7 = "3/2/2023 10:29";
    assertTrue(Project1.isValidDateAndTime(dateAndTime7));
    String dateAndTime8 = "3/2/2023 1:29";
    assertTrue(Project1.isValidDateAndTime(dateAndTime8));
    String dateAndTime9 = "4/31/2023 1:29";
    assertFalse(Project1.isValidDateAndTime(dateAndTime9));
    String dateAndTime10 = "2/30/2023 1:29";
    assertFalse(Project1.isValidDateAndTime(dateAndTime10));
  }

  @Test
  void testIsInt() {
    String s1 = "abc";
    assertFalse(Project1.isInt(s1));
    String s2 = "123";
    assertTrue(Project1.isInt(s2));
  }

  @Test
  void testContainsLetter() {
    String s1 = "1234";
    assertFalse(Project1.containsLetter(s1));
    String s2 = "12a34";
    assertTrue(Project1.containsLetter(s2));
  }

  @Test
  void testArgsNoErrorAndMissing() {
    String[] args = {"0025", "0025", "SEA", "3/15/2023", "10:39", "PDX", "3/15/2023", "11:39"};
    assertFalse(Project1.argsNoErrorAndMissing(args));
    String[] args2 = {"Some Airline", "Express", "SEA", "3/15/2023", "10:39", "PDX", "3/15/2023", "11:39"};
    assertFalse(Project1.argsNoErrorAndMissing(args2));
    String[] args3 = {"Some Airline", "0025", "Seattle", "3/15/2023", "10:39", "PDX", "3/15/2023", "11:39"};
    assertFalse(Project1.argsNoErrorAndMissing(args3));
    String[] args4 = {"Some Airline", "0025", "0025", "3/15/2023", "10:39", "PDX", "3/15/2023", "11:39"};
    assertFalse(Project1.argsNoErrorAndMissing(args4));
    String[] args5 = {"Some Airline", "0025", "SEA", "30/15/2023", "10:39", "PDX", "3/15/2023", "11:39"};
    assertFalse(Project1.argsNoErrorAndMissing(args5));
    String[] args6 = {"Some Airline", "0025", "SEA", "tomorrow", "24:39", "PDX", "3/15/2023", "11:39"};
    assertFalse(Project1.argsNoErrorAndMissing(args6));
    String[] args7 = {"Some Airline", "0025", "SEA", "30/15/2023", "tonight", "PDX", "3/15/2023", "11:39"};
    assertFalse(Project1.argsNoErrorAndMissing(args7));
    String[] args8 = {"Some Airline", "0025", "SEA", "3/15/2023", "10:39", "Portland", "3/15/2023", "11:39"};
    assertFalse(Project1.argsNoErrorAndMissing(args8));
    String[] args9 = {"Some Airline", "0025", "SEA", "3/15/2023", "10:39", "PDX", "13/0/202", "11:39"};
    assertFalse(Project1.argsNoErrorAndMissing(args9));
    String[] args10 = {"Some Airline", "0025", "SEA", "3/15/2023", "10:39", "PDX", "3/15/2023", "11:89"};
    assertFalse(Project1.argsNoErrorAndMissing(args10));
    String[] args11 = {"Some Airline", "0025", "SEA", "3/15/2023", "10:39", "PDX", "3/15/2023", "11:39"};
    assertTrue(Project1.argsNoErrorAndMissing(args11));
    String[] args12 = {"Some Airline", "0025", "SEA", "3/15/2023", "10:39", "PDX", "today", "11:39"};
    assertFalse(Project1.argsNoErrorAndMissing(args12));
    String[] args13 = {"Some Airline", "0025", "SEA", "3/15/2023", "10:39", "PDX", "3/15/2023", "afternoon"};
    assertFalse(Project1.argsNoErrorAndMissing(args13));
  }
}
