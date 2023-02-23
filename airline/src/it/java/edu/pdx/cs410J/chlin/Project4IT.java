package edu.pdx.cs410J.chlin;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.net.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * An integration test for the {@link Project4} main class.
 */
class Project4IT extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Project4} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Project4.class, args );
    }

  /**
   * Tests that invoking the main method with no arguments issues an error
   */
  @Test
  void testNoCommandLineArguments() {
    MainMethodResult result = invokeMain(Project4.class);
    assertThat(result.getTextWrittenToStandardError(), containsString("Missing command line arguments"));
  }

//  @Test
//  void testCommandLinePrint() {
//      MainMethodResult result = invokeMain(Project4.class, "Alaska", "25", "SEA", "3/15/2023", "10:39","pm", "PDX", "3/15/2023", "11:39","pm", "-PRINT");
//      assertEquals(result.getTextWrittenToStandardOut(), "Flight 25 departs SEA at 3/15/23 10:39 PM arrives PDX at 3/15/23 11:39 PM\r\n");
//  }

    @Test
    void testCommandWrontOption() {
        MainMethodResult result = invokeMain(Project4.class, "Alaska", "25", "SEA", "3/15/2023", "10:39","pm", "PDX", "3/15/2023", "11:39","pm", "-wrong");
        assertThat(result.getTextWrittenToStandardError(), equalTo("Error: unknown option: -wrong\noptions:\n  -print           Prints a description of the entered flight\n  -README          Information about the project\n  -textFile file   Read/write the airline info to the file\n  -xmlFile file    Read/write the airline info to xml file\n  -pretty file/-   write the pretty format to file/command line or (file -) do both\n"));
    }

    @Test
    void testCommandReadme() {
        MainMethodResult result = invokeMain(Project4.class, "Alaska", "25", "SEA", "3/15/2023", "10:39","pm", "PDX", "3/15/2023", "11:39","pm", "-readme");
        assertThat(result.getTextWrittenToStandardOut(), equalTo("To use the program, enter the following will create a airline\n" +
                "and a flight, and add the flight with its information into the\n" +
                "airline's flight list.\n" +
                "\n" +
                "java -jar target/airline-2023.0.0.jar [options] <args>\n" +
                "  args are (in this order):\n" +
                "    airline          Airline name\n" +
                "    flightNumber     The flight number, digits  only\n" +
                "    src              Three-letter departure airport code \n" +
                "    depart           Departure date and time (MM/DD/YYYY hh:mm am/pm)\n" +
                "    dest             Three-letter destination airport code\n" +
                "    arrive           Arrival date and time (MM/DD/YYYY hh:mm am/pm)\n" +
                "  list of options:\n" +
                "    -print           Prints the description of the new flight\n" +
                "    -README          Prints this README file\n" +
                "    -textFile file   Read/write the airline info to the file\n" +
                "    -xmlFile file    Read/write the airline info to xml file\n" +
                "    -pretty file/-   write the pretty format to file/command line or (file - ) do both\n" +
                "\n" +
                "Cheng Lin\n" +
                "chlin@pdx.edu\n\n"));
    }

  @Test
  void testReadFromFile(@TempDir File tempDir) {
      String airlineName = "Test Airline";
      File textFile = new File(tempDir, "airline.txt");
      Airline airline = Project4.readFromFile(textFile, airlineName);
      assertNotNull(airline);
      assertThat(airline.getName(), equalTo("Test Airline"));
  }

  @Test
    void readFromValidTxtFile() throws URISyntaxException {
      File file = new File(getClass().getResource("valid-airline.txt").getFile());

      Airline airline = Project4.readFromFile(file, "Some Airline");
      assertNotNull(airline);
      assertThat(airline.getName(), equalTo("Test Airline"));
  }

  @Test
    void testWriteToFile() {
      ByteArrayOutputStream errContent = new ByteArrayOutputStream();

      System.setErr(new PrintStream(errContent));
      File file = new File(getClass().getResource("valid-airline.txt").getFile());

      Airline airline = new Airline("A Valid Airline");

      Project4.writeToFile(file, "Invalid Airline", airline);

      assertEquals("Airline not found in this file", errContent.toString().trim());

  }

  @Test
    void testPretty() {
      MainMethodResult result = invokeMain(Project4.class, "Alaska Airline", "321","PDX","3/15/2023","10:36","pm","SEA","3/15/2023","11:36","PM","-print","-textfile","alaska.txt","-pretty","-");
      System.out.println(result.getTextWrittenToStandardOut());
  }

  @Test
    void testWrongAMPM() {
      assertThat(false, equalTo(Project4.isValidDateAndTime("3/15/2023 10:36 4M")));
  }

  @Test
    void testStringHasNumber() {
      assertThat(true, equalTo(Project4.hasInt("aa1cc")));
  }

    @Test
    void testMissingArg() {
        MainMethodResult result = invokeMain(Project4.class, "Alaska Airline");
        assertThat(result.getTextWrittenToStandardError(), equalTo("** missing: flight number\n"));
        result = invokeMain(Project4.class, "Alaska Airline", "25");
        assertThat(result.getTextWrittenToStandardError(), equalTo("** missing: departure airport code\n"));
        result = invokeMain(Project4.class, "Alaska Airline", "25", "P1X");
        assertThat(result.getTextWrittenToStandardError(), equalTo("** Departure airport code should be letters only: P1X\n"));
        result = invokeMain(Project4.class, "Alaska Airline", "25", "PDX");
        assertThat(result.getTextWrittenToStandardError(), equalTo("** missing: departure date\n"));
        result = invokeMain(Project4.class, "Alaska Airline", "25", "PDX", "3/15/2023");
        assertThat(result.getTextWrittenToStandardError(), equalTo("** missing: departure time\n"));
        result = invokeMain(Project4.class, "Alaska Airline", "25", "PDX", "3/15/2023", "10:36");
        assertThat(result.getTextWrittenToStandardError(), equalTo("** missing: departure am/pm marker\n"));
        result = invokeMain(Project4.class, "Alaska Airline", "25", "PDX", "3/15/2023", "10:36", "8m");
        assertThat(result.getTextWrittenToStandardError(), equalTo("** The departure am/pm marker should not include integer: 8m\n"));
        result = invokeMain(Project4.class, "Alaska Airline", "25", "PDX", "3/15/2023", "10:36", "ampm");
        assertThat(result.getTextWrittenToStandardError(), equalTo("** The departure am/pm marker should be 2 letters only: ampm\n"));
        result = invokeMain(Project4.class, "Alaska Airline", "25", "PDX", "3/15/2023", "10:36", "pm");
        assertThat(result.getTextWrittenToStandardError(), equalTo("** missing: arrival airport code\n"));
        result = invokeMain(Project4.class, "Alaska Airline", "25", "PDX", "3/15/2023", "10:36", "pm", "S1A");
        assertThat(result.getTextWrittenToStandardError(), equalTo("** Arrival airport code should be letters only: S1A\n"));
        result = invokeMain(Project4.class, "Alaska Airline", "25", "PDX", "3/15/2023", "10:36", "pm", "SEA");
        assertThat(result.getTextWrittenToStandardError(), equalTo("** missing: arrival date\n"));
        result = invokeMain(Project4.class, "Alaska Airline", "25", "PDX", "3/15/2023", "10:36", "pm", "SEA","3/15/2023");
        assertThat(result.getTextWrittenToStandardError(), equalTo("** missing: arrival time\n"));
        result = invokeMain(Project4.class, "Alaska Airline", "25", "PDX", "3/15/2023", "10:36", "pm", "SEA","3/15/2023","11:39");
        assertThat(result.getTextWrittenToStandardError(), equalTo("** missing: arrival am/pm marker\n"));
        result = invokeMain(Project4.class, "Alaska Airline", "25", "PDX", "3/15/2023", "10:36", "pm", "SEA","3/15/2023","11:39", "5m");
        assertThat(result.getTextWrittenToStandardError(), equalTo("** The arrival am/pm marker should not include integer: 5m\n"));
        result = invokeMain(Project4.class, "Alaska Airline", "25", "PDX", "3/15/2023", "10:36", "pm", "SEA","3/15/2023","11:39", "pma");
        assertThat(result.getTextWrittenToStandardError(), equalTo("** The arrival am/pm marker should be 2 letters only: pma\n"));
    }

    @Test
    void testTooManyArgs() {
        MainMethodResult result = invokeMain(Project4.class, "Alaska Airline", "25", "PDX", "3/15/2023", "10:36", "pm", "SEA","3/15/2023","11:39", "pm", "extraArgs");
        String errOut = "Too many arguments\nusage: java -jar target/airline-2023.0.0.jar [options] <args>\n  args order:\n  airline flightNumber src departDate departTime AM/PM dest arriveDate arriveTime AM/PM\n";
        assertThat(result.getTextWrittenToStandardError(), equalTo(errOut));
    }

    @Test
    void testBadFileName() {
      String badName = "text-txt";
      assertFalse(Project4.validFileNameFormat(badName));
    }

    @Test
    void testNonexistedAirportCode() {
        MainMethodResult result = invokeMain(Project4.class, "Alaska Airline", "25", "ZZZ", "3/15/2023", "10:36", "pm", "SEA","3/15/2023","11:39", "pm");
        assertThat(result.getTextWrittenToStandardError(), equalTo("** Source airport code does not exist: ZZZ\n"));
        result = invokeMain(Project4.class, "Alaska Airline", "25", "PDX", "3/15/2023", "10:36", "pm", "ZZZ","3/15/2023","11:39", "pm");
        assertThat(result.getTextWrittenToStandardError(), equalTo("** Destination airport code does not exist: ZZZ\n"));
    }

    @Test
    void testArrivalTimeTooEarly() {
        MainMethodResult result = invokeMain(Project4.class, "Alaska Airline", "25", "PDX", "3/15/2023", "10:36", "pm", "SEA","3/15/2023","9:39", "pm");
        String errOut = "** Arrival time is earlier than departure time\n**   Departure: 3/15/2023 10:36 pm\n**   Arrival  : 3/15/2023 9:39 pm\n";
        assertThat(result.getTextWrittenToStandardError(), equalTo(errOut));

    }

    @Test
    void testXmlReadValid(@TempDir File tempDir) {
        File file = new File(tempDir, "testAirline.xml");
        String airlineName = "Valid Airlines";
        Airline airline = Project4.xmlRead(file, airlineName);
        assertThat(airline.getName(), equalTo("Valid Airlines"));
    }

    @Test
    void testXmlReadValidFile() {
      File file = new File(getClass().getResource("valid-airline.xml").getFile());
      Airline airline = Project4.xmlRead(file, "some name");
      assertThat(airline.getName(), equalTo("Valid Airlines"));
    }

    @Test
    void testXmlWrite() {
      ByteArrayOutputStream errContent = new ByteArrayOutputStream();

      System.setErr(new PrintStream((errContent)));

      File file = new File(getClass().getResource("valid-airline.xml").getFile());
      Airline airline = new Airline("someairline");

      Project4.xmlWrite(file, "Invaid airline", airline);
      assertEquals("Airline not found in this file", errContent.toString().trim());
    }

}