package edu.pdx.cs410J.chlin;

import edu.pdx.cs410J.AirlineDumper;
import edu.pdx.cs410J.AirportNames;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Class that implements the <code>PrettyPrinter</code> for project 3.
 */
public class PrettyPrinter implements AirlineDumper<Airline> {
    /**
     * The writer private field that is used in dump() method
     * for the method to write the content of the airline to
     */
    private final Writer writer;

    /**
     * Constructor of the PrettyPrinter that set the FileWriter to its field
     * @param writer the FileWriter that specified the file in Project3.java
     */
    public PrettyPrinter(Writer writer) {
        this.writer = writer;
    }

    /**
     * Constructor to use when -pretty - optiton does not specify file
     */
    public PrettyPrinter() {
        writer = null;
    }

    /**
     * dump method that takes in an airline and write the formatted output
     * to the file using formatOutput method to format
     * @param airline the airline that contains flights
     */
    @Override
    public void dump(Airline airline) {
        try (
          PrintWriter pw = new PrintWriter(this.writer)
        ) {

            ArrayList<String> outputString = formatOutput(airline);

            for (String line : outputString) {
                pw.println(line);
            }
            pw.flush();
        }
    }

    /**
     * The foramtting method that foramt the airline and its flights
     * into an ArrayList of Strings so later the dump method can call it
     * and just loop through the objects as they are lines. Also
     * when the -pretty option does not specify a file, this
     * method could be called and use to output to cammand line
     * @param airline the airline that contains the flights
     * @return A list of lines that store in ArrayList
     */
    public ArrayList<String> formatOutput(Airline airline) {
        ArrayList<String> output = new ArrayList<>();

        output.add(airline.toString());

        Collection<Flight> flights = airline.getFlights();

        String format = "%10s %30s %30s";
        String line8 = "------------";
        String line30 = "------------------------------";
        StringBuilder horizontalLine = new StringBuilder();
        horizontalLine.append(line8).append(line30).append(line30);

        String head = String.format(format, "Flight", "From", "To");
        output.add(head);
        output.add(horizontalLine.toString());

        if (!flights.isEmpty()) {

            for (Flight flight : flights) {
                int flightNumber = flight.getNumber();
                String source = AirportNames.getName(flight.getSource());
                String departTime = flight.getDepartureString();
                String dest = AirportNames.getName(flight.getDestination());
                String arrivalTime = flight.getArrivalString();

                long duration = flight.getArrival().getTime() - flight.getDeparture().getTime();
                String hour =String.valueOf(duration / (60 * 60 * 1000));
                String minute = String.valueOf(duration / (60 * 1000) % 60);
                StringBuilder travelTime = new StringBuilder();
                travelTime.append("(");
                if (Integer.parseInt(hour) != 0) {
                    travelTime.append(hour).append("h");
                }
                if (Integer.parseInt(minute) != 0) {
                    travelTime.append(minute).append("m)");
                } else {
                    travelTime.append(")");
                }

                String flightFormatted = String.format(format, flightNumber, source, dest);
                String flightTimeFormatted = String.format(format, travelTime, departTime, arrivalTime);
                output.add(flightFormatted);
                output.add(flightTimeFormatted);
                output.add(horizontalLine.toString());

            }
        }

        return output;
    }
}
