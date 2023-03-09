package edu.pdx.cs410J.chlin;

import edu.pdx.cs410J.AirportNames;
import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.UncaughtExceptionInMain;
import edu.pdx.cs410J.web.HttpRequestHelper.RestException;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.MethodOrderer.MethodName;

/**
 * An integration test for {@link Project5} that invokes its main method with
 * various arguments
 */
@TestMethodOrder(MethodName.class)
class Project5IT extends InvokeMainTestCase {
    private static final String HOSTNAME = "localhost";
    private static final String PORT = System.getProperty("http.port", "8080");

//    @Test
//    void test0RemoveAllMappings() throws IOException {
//        AirlineRestClient client = new AirlineRestClient(HOSTNAME, Integer.parseInt(PORT));
//        client.removeAllAirlines();
//    }

    @Test
    void test1NoCommandLineArguments() {
        MainMethodResult result = invokeMain(Project5.class);
        String message = "Missing command line arguments\n" +
                "usage: java -jar target/airline-2023.0.0.jar [options] <args>\n" +
                "  args order:\n" +
                "  airline flightNumber src departDate departTime AM/PM dest arriveDate arriveTime AM/PM\n";
        assertThat(result.getTextWrittenToStandardError(), containsString(message));
    }

    @Test
    @Disabled
    void test2EmptyServer() {
        MainMethodResult result = invokeMain(Project5.class, HOSTNAME, PORT);

        assertThat(result.getTextWrittenToStandardError(), equalTo(""));

        String out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString(""));
    }

    @Test
    void testCommandWrontOption() {
        MainMethodResult result = invokeMain(Project5.class, "Alaska", "25", "SEA", "3/15/2023", "10:39", "pm", "PDX", "3/15/2023", "11:39", "pm", "-wrong");
        String message = "Error: unknown option: -wrong\n" +
                "options:\n" +
                "  -host hostname               Host computer on which the server runs\n" +
                "  -port port                   Port on which teh server is listening\n" +
                "  -search airline [src dest]   search for flights\n" +
                "  -print                       Prints a description of the entered flight\n" +
                "  -README                      Information about the project\n";
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo(message));
    }
    @Test
    void testCommandReadme() {
        MainMethodResult result = invokeMain(Project5.class, "Alaska", "25", "SEA", "3/15/2023", "10:39","pm", "PDX", "3/15/2023", "11:39","pm", "-readme");
        assertThat(result.getTextWrittenToStandardOut(), CoreMatchers.equalTo("To use the program, enter the following will create a airline\n" +
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
                "    -host hostname               Host computer on which the server runs\n" +
                "    -port port                   Port on which the server is listening\n" +
                "    -search airline [src dest]   search for flights\n" +
                "    -print                       Prints the description of the new flight\n" +
                "    -README                      Prints this README file\n" +
                "\n" +
                "Cheng Lin\n" +
                "chlin@pdx.edu\n\n"));
    }

    @Test
    void testIsValidDateAndTime() {
        String dateAndTime = "3/15/2023 10:89 PM";
        assertFalse(Project5.isValidDateAndTime(dateAndTime));
        String dateAndTime2 = "3/15/2023 28:19 PM";
        assertFalse(Project5.isValidDateAndTime(dateAndTime2));
        String dateAndTime3 = "3/15/20233 10:29 PM";
        assertFalse(Project5.isValidDateAndTime(dateAndTime3));
        String dateAndTime4 = "3/0/2023 10:29 PM";
        assertFalse(Project5.isValidDateAndTime(dateAndTime4));
        String dateAndTime5 = "30/15/2023 10:29 PM";
        assertFalse(Project5.isValidDateAndTime(dateAndTime5));
        String dateAndTime6 = "03/15/2023 10:29 PM";
        assertTrue(Project5.isValidDateAndTime(dateAndTime6));
        String dateAndTime7 = "3/2/2023 10:29 PM";
        assertTrue(Project5.isValidDateAndTime(dateAndTime7));
        String dateAndTime8 = "3/2/2023 1:29 PM";
        assertTrue(Project5.isValidDateAndTime(dateAndTime8));
        String dateAndTime9 = "4/31/2023 1:29 PM";
        assertFalse(Project5.isValidDateAndTime(dateAndTime9));
        String dateAndTime10 = "2/30/2023 1:29 PM";
        assertFalse(Project5.isValidDateAndTime(dateAndTime10));
    }

    @Test
    void testIsInt() {
        String s1 = "abc";
        assertFalse(Project5.isInt(s1));
        String s2 = "123";
        assertTrue(Project5.isInt(s2));
    }

    @Test
    void testContainsLetter() {
        String s1 = "1234";
        assertFalse(Project5.containsLetter(s1));
        String s2 = "12a34";
        assertTrue(Project5.containsLetter(s2));
    }

    @Test
    void testStringHasNumber() {
        assertThat(true, CoreMatchers.equalTo(Project5.hasInt("aa1cc")));
    }

    @Test
    void testArgsNoErrorAndMissing() {
        String[] args = {"0025", "0025", "SEA", "3/15/2023", "10:39","PM", "PDX", "3/15/2023", "11:39","PM"};
        assertFalse(Project5.argsNoErrorAndMissing(args));
        String[] args2 = {"Some Airline", "Express", "SEA", "3/15/2023", "10:39", "PM","PDX", "3/15/2023", "11:39","PM"};
        assertFalse(Project5.argsNoErrorAndMissing(args2));
        String[] args3 = {"Some Airline", "0025", "Seattle", "3/15/2023", "10:39","PM", "PDX", "3/15/2023", "11:39","PM"};
        assertFalse(Project5.argsNoErrorAndMissing(args3));
        String[] args4 = {"Some Airline", "0025", "0025", "3/15/2023", "10:39", "PM","PDX", "3/15/2023", "11:39","PM"};
        assertFalse(Project5.argsNoErrorAndMissing(args4));
        String[] args5 = {"Some Airline", "0025", "SEA", "30/15/2023", "10:39", "PM","PDX", "3/15/2023", "11:39","PM"};
        assertFalse(Project5.argsNoErrorAndMissing(args5));
        String[] args6 = {"Some Airline", "0025", "SEA", "tomorrow", "24:39", "PM","PDX", "3/15/2023", "11:39","PM"};
        assertFalse(Project5.argsNoErrorAndMissing(args6));
        String[] args7 = {"Some Airline", "0025", "SEA", "30/15/2023", "tonight", "PM","PDX", "3/15/2023", "11:39","PM"};
        assertFalse(Project5.argsNoErrorAndMissing(args7));
        String[] args8 = {"Some Airline", "0025", "SEA", "3/15/2023", "10:39","PM" ,"Portland", "3/15/2023", "11:39","PM"};
        assertFalse(Project5.argsNoErrorAndMissing(args8));
        String[] args9 = {"Some Airline", "0025", "SEA", "3/15/2023", "10:39","PM", "PDX", "13/0/202", "11:39","PM"};
        assertFalse(Project5.argsNoErrorAndMissing(args9));
        String[] args10 = {"Some Airline", "0025", "SEA", "3/15/2023", "10:39","PM","PDX", "3/15/2023", "11:89","PM"};
        assertFalse(Project5.argsNoErrorAndMissing(args10));
        String[] args11 = {"Some Airline", "0025", "SEA", "3/15/2023", "10:39","PM" ,"PDX", "3/15/2023", "11:39","PM"};
        assertTrue(Project5.argsNoErrorAndMissing(args11));
        String[] args12 = {"Some Airline", "0025", "SEA", "3/15/2023", "10:39", "PM","PDX", "today", "11:39","PM"};
        assertFalse(Project5.argsNoErrorAndMissing(args12));
        String[] args13 = {"Some Airline", "0025", "SEA", "3/15/2023", "10:39", "PM","PDX", "3/15/2023", "afternoon","PM"};
        assertFalse(Project5.argsNoErrorAndMissing(args13));
    }

    @Test
    void testMissingArg() {
        MainMethodResult result = invokeMain(Project5.class, "Alaska Airline");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo("** missing: flight number\n"));
        result = invokeMain(Project5.class, "Alaska Airline", "25");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo("** missing: departure airport code\n"));
        result = invokeMain(Project5.class, "Alaska Airline", "25", "P1X");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo("** Departure airport code should be letters only: P1X\n"));
        result = invokeMain(Project5.class, "Alaska Airline", "25", "PDX");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo("** missing: departure date\n"));
        result = invokeMain(Project5.class, "Alaska Airline", "25", "PDX", "3/15/2023");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo("** missing: departure time\n"));
        result = invokeMain(Project5.class, "Alaska Airline", "25", "PDX", "3/15/2023", "10:36");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo("** missing: departure am/pm marker\n"));
        result = invokeMain(Project5.class, "Alaska Airline", "25", "PDX", "3/15/2023", "10:36", "8m");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo("** The departure am/pm marker should not include integer: 8m\n"));
        result = invokeMain(Project5.class, "Alaska Airline", "25", "PDX", "3/15/2023", "10:36", "ampm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo("** The departure am/pm marker should be 2 letters only: ampm\n"));
        result = invokeMain(Project5.class, "Alaska Airline", "25", "PDX", "3/15/2023", "10:36", "pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo("** missing: arrival airport code\n"));
        result = invokeMain(Project5.class, "Alaska Airline", "25", "PDX", "3/15/2023", "10:36", "pm", "S1A");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo("** Arrival airport code should be letters only: S1A\n"));
        result = invokeMain(Project5.class, "Alaska Airline", "25", "PDX", "3/15/2023", "10:36", "pm", "SEA");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo("** missing: arrival date\n"));
        result = invokeMain(Project5.class, "Alaska Airline", "25", "PDX", "3/15/2023", "10:36", "pm", "SEA","3/15/2023");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo("** missing: arrival time\n"));
        result = invokeMain(Project5.class, "Alaska Airline", "25", "PDX", "3/15/2023", "10:36", "pm", "SEA","3/15/2023","11:39");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo("** missing: arrival am/pm marker\n"));
        result = invokeMain(Project5.class, "Alaska Airline", "25", "PDX", "3/15/2023", "10:36", "pm", "SEA","3/15/2023","11:39", "5m");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo("** The arrival am/pm marker should not include integer: 5m\n"));
        result = invokeMain(Project5.class, "Alaska Airline", "25", "PDX", "3/15/2023", "10:36", "pm", "SEA","3/15/2023","11:39", "pma");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo("** The arrival am/pm marker should be 2 letters only: pma\n"));
    }

    @Test
    void testTooManyArgs() {
        MainMethodResult result = invokeMain(Project5.class, "Alaska Airline", "25", "PDX", "3/15/2023", "10:36", "pm", "SEA","3/15/2023","11:39", "pm", "extraArgs");
        String errOut = "Too many arguments\nusage: java -jar target/airline-2023.0.0.jar [options] <args>\n  args order:\n  airline flightNumber src departDate departTime AM/PM dest arriveDate arriveTime AM/PM\n";
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo(errOut));
    }

    @Test
    void testNonexistedAirportCode() {
        MainMethodResult result = invokeMain(Project5.class, "Alaska Airline", "25", "ZZZ", "3/15/2023", "10:36", "pm", "SEA","3/15/2023","11:39", "pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo("** Source airport code does not exist: ZZZ\n"));
        result = invokeMain(Project5.class, "Alaska Airline", "25", "PDX", "3/15/2023", "10:36", "pm", "ZZZ","3/15/2023","11:39", "pm");
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo("** Destination airport code does not exist: ZZZ\n"));
    }

    @Test
    void testArrivalTimeTooEarly() {
        MainMethodResult result = invokeMain(Project5.class, "Alaska Airline", "25", "PDX", "3/15/2023", "10:36", "pm", "SEA", "3/15/2023", "9:39", "pm");
        String errOut = "** Arrival time is earlier than departure time\n**   Departure: 3/15/2023 10:36 pm\n**   Arrival  : 3/15/2023 9:39 pm\n";
        assertThat(result.getTextWrittenToStandardError(), CoreMatchers.equalTo(errOut));
    }

    @Test
    void testPrintSearchUsage() {
        MainMethodResult result = invokeMain(Project5.class, "-search", "Airline", "PDX");
        String message = "Enter only \"airlineName\" or \"airnlineName [source destiation]\" when -search is entered\n";
        assertThat(result.getTextWrittenToStandardError(),CoreMatchers.equalTo(message) );
    }
    @Test
    void testMissingPort() {
        MainMethodResult result = invokeMain(Project5.class, "-host", "localhost");
        String message = "** Missing -port\n";
        assertThat(result.getTextWrittenToStandardError(),CoreMatchers.equalTo(message) );
    }
    @Test
    void testMissingHostFlag() {
        MainMethodResult result = invokeMain(Project5.class, "-port", "8080");
        String message = "** Missing -host\n";
        assertThat(result.getTextWrittenToStandardError(),CoreMatchers.equalTo(message) );
    }

//    @Test
//    void test3NoDefinitionsThrowsAppointmentBookRestException() {
//        String word = "WORD";
//        try {
//            invokeMain(Project5.class, HOSTNAME, PORT, word);
//            fail("Should have thrown a RestException");
//
//        } catch (UncaughtExceptionInMain ex) {
//            RestException cause = (RestException) ex.getCause();
//            assertThat(cause.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_NOT_FOUND));
//        }
//    }

//    @Disabled
//    @Test
//    void test4AddFlight() {
//        String airlineName = "Fake Airline";
//        String flightNumber = "123";
//        String src = "SEA";
//        String depart = "3/7/2023 10:36 AM";
//        String dest = "PDX";
//        String arrive = "3/7/2023 11:39 AM";
//
//        MainMethodResult result = invokeMain( Project5.class, HOSTNAME, PORT, airlineName, flightNumber, src, depart, dest, arrive );
//
//        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
//
//        String out = result.getTextWrittenToStandardOut();
//        assertThat(out, out, containsString(Messages.addFlightMessage(airlineName, flightNumber, src, depart, dest, arrive)));
//
//        result = invokeMain( Project5.class, HOSTNAME, PORT, airlineName );
//
//        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
//
//        out = result.getTextWrittenToStandardOut();
//        assertThat(out, out, containsString(PrettyPrinter.formatDictionaryEntry(airlineName, flightNumber)));
//
//        result = invokeMain( Project5.class, HOSTNAME, PORT );
//
//        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
//
//        out = result.getTextWrittenToStandardOut();
//        assertThat(out, out, containsString(PrettyPrinter.formatDictionaryEntry(airlineName, flightNumber)));
//    }
}