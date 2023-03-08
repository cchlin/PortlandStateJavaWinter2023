package edu.pdx.cs410J.chlin;

import edu.pdx.cs410J.ParserException;

import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.Map;

/**
 * The main class that parses the command line and communicates with the
 * Airline server using REST.
 */
public class Project5 {

    public static final String MISSING_ARGS = "Missing command line arguments";

    public static void main(String... args) {
        String hostName = null;
        String portString = null;
        String airlineName = null;
        String flightNumber = null;
        String src = null;
        String depart = null;
        String dest = null;
        String arrive = null;

        for (String arg : args) {
            if (hostName == null) {
                hostName = arg;

            } else if ( portString == null) {
                portString = arg;

            } else if (airlineName == null) {
                airlineName = arg;

            } else if (flightNumber == null) {
                flightNumber = arg;

            } else {
                usage("Extraneous command line argument: " + arg);
            }
        }

        if (hostName == null) {
            usage( MISSING_ARGS );
            return;

        } else if ( portString == null) {
            usage( "Missing port" );
            return;
        }

        int port;
        try {
            port = Integer.parseInt( portString );

        } catch (NumberFormatException ex) {
            usage("Port \"" + portString + "\" must be an integer");
            return;
        }

        AirlineRestClient client = new AirlineRestClient(hostName, port);

//        String message = "";
        try {
            if (airlineName == null) {
                error("Airline name required");
//                // Print all word/definition pairs
//                Map<String, String> dictionary = client.getAllAirlines();
//                StringWriter sw = new StringWriter();
//                PrettyPrinter pretty = new PrettyPrinter(sw);
//                pretty.dump(dictionary);
//                message = sw.toString();

            } else if (flightNumber == null) {
                // Print all dictionary entries
//                message = PrettyPrinter.formatDictionaryEntry(airlineName, client.getAirline(airlineName));
                Airline airline = client.getAirline(airlineName);
                System.out.println(airline.toString());
            } else {
                // Post the word/definition pair
                client.addFlight(airlineName, flightNumber, src, depart, dest, arrive);
//                message = Messages.definedWordAs(airlineName, flightNumber);
            }

        } catch (IOException | ParserException ex ) {
//        } catch (IOException ex) {
            error("While contacting server: " + ex.getMessage());
            return;
        }

//        System.out.println(message);
    }

    private static void error( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);
    }

    /**
     * Prints usage information for this program and exits
     * @param message An error message to print
     */
    private static void usage( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);
        err.println();
        err.println("usage: java Project5 host port [word] [definition]");
        err.println("  host         Host of web server");
        err.println("  port         Port of web server");
        err.println("  word         Word in dictionary");
        err.println("  definition   Definition of word");
        err.println();
        err.println("This simple program posts words and their definitions");
        err.println("to the server.");
        err.println("If no definition is specified, then the word's definition");
        err.println("is printed.");
        err.println("If no word is specified, all dictionary entries are printed");
        err.println();
    }
}