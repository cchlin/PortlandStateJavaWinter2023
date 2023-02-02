package edu.pdx.cs410J.chlin;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.net.*;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * An integration test for the {@link Project2} main class.
 */
class Project2IT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Project2} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Project2.class, args );
    }

  /**
   * Tests that invoking the main method with no arguments issues an error
   */
  @Test
  void testNoCommandLineArguments() {
    MainMethodResult result = invokeMain(Project2.class);
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing command line arguments"));
  }

  @Test
  void testCommandLinePrint() {
      MainMethodResult result = invokeMain(Project2.class, "Alaska", "25", "SEA", "3/15/2023", "10:39", "PDX", "3/15/2023", "11:39", "-PRINT");
      assertThat(result.getTextWrittenToStandardOut(), equalTo("Flight 25 departs SEA at 3/15/2023 10:39 arrives PDX at 3/15/2023 11:39\n"));
  }

    @Test
    void testCommandWrontOption() {
        MainMethodResult result = invokeMain(Project2.class, "Alaska", "25", "SEA", "3/15/2023", "10:39", "PDX", "3/15/2023", "11:39", "-wrong");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Error: unknown option: -wrong\noptions:\n  -print      Prints a description of the entered flight\n  -README     Information about the project\n"));
    }

    @Test
    void testCommandReadme() {
        MainMethodResult result = invokeMain(Project2.class, "Alaska", "25", "SEA", "3/15/2023", "10:39", "PDX", "3/15/2023", "11:39", "-readme");
        assertThat(result.getTextWrittenToStandardOut(), equalTo("To use the program, enter the following will create a airline\n" +
                "and a flight, and add the flight with its information into the\n" +
                "airline's flight list.\n" +
                "\n" +
                "java -jar target/airline-2023.0.0.jar [options] <args>\n" +
                "  args are (in this order):\n" +
                "    airline         Airline name\n" +
                "    flightNumber    The flight number, digits  only\n" +
                "    src             Three-letter departure airport code \n" +
                "    depart          Departure date and time (MM/DD/YYYY hh:mm)\n" +
                "    dest            Three-letter destination airport code\n" +
                "    arrive          Arrival date and time (MM/DD/YYYY hh:mm)\n" +
                "  list of options:\n" +
                "    -print     Prints the description of the new flight\n" +
                "    -README    Prints this README file\n" +
                "\n" +
                "Cheng Lin\n" +
                "chlin@pdx.edu\n\n"));
    }

  @Test
  void testReadFromFile(@TempDir File tempDir) {
      String airlineName = "Test Airline";
      File textFile = new File(tempDir, "airline.txt");
      Airline airline = Project2.readFromFile(textFile, airlineName);
      assertNotNull(airline);
      assertThat(airline.getName(), equalTo("Test Airline"));
  }

  @Test
    void readFromValidTxtFile() throws URISyntaxException {
      File file = new File(getClass().getResource("valid-airline.txt").getFile());

      Airline airline = Project2.readFromFile(file, "Some Airline");
      assertNotNull(airline);
      assertThat(airline.getName(), equalTo("Test Airline"));
  }

  @Test
    void testWriteToFile() {
      ByteArrayOutputStream errContent = new ByteArrayOutputStream();

      System.setErr(new PrintStream(errContent));
      File file = new File(getClass().getResource("valid-airline.txt").getFile());

      Project2.writeToFile(file,"Invalid Airline", new Flight(321,"PDX","1/1/2023 00:00","SEA","1/1/2023 0:01"));

      assertEquals("Airline not found in this file\n", errContent.toString());

  }


}