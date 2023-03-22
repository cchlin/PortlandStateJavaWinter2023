package edu.pdx.cs410j.chlin;

import edu.pdx.cs410J.AirlineDumper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;

/**
 * A skeletal implementation of the <code>TextDumper</code> class for Project 2.
 */
public class TextDumper implements AirlineDumper<Airline>, AutoCloseable {

    /**
     * The writer private field that is used in dump() method
     * for the method to write the content of the airline to
     */
    private final Writer writer;

    /**
     * constructor of the TextDumper that set the FileWriter to its field
     * @param writer the FileWriter that specified the file in Project4.java
     */
    public TextDumper(Writer writer) {
        this.writer = writer;
    }

    /**
     * This method writes the airline and its flights into the FileWriter specifeid in TextDumper
     * constructor
     * @param airline the airline that is to be written
     * @throws IOException throws exception when the fight is empty, since the usage of this program is to enter airline with a flight
     */
    @Override
    public void dump(Airline airline) throws IOException {
        try (
                PrintWriter pw = new PrintWriter(this.writer)
        ) {
            pw.println(airline.getName());

            Collection<Flight> flights = airline.getFlights();

            if (!flights.isEmpty()) {
                for (Flight flight : flights) {
                    // convert flight information to one line with "," separates them
                    StringBuilder sb = new StringBuilder();
                    sb.append(flight.getNumber()).append(",");
                    sb.append(flight.getSource()).append(",");
                    sb.append(flight.getDepartureString()).append(",");
                    sb.append(flight.getDestination()).append(",");
                    sb.append(flight.getArrivalString());
                    pw.println(sb);
                }
            }
            else {
                throw new IOException("Empty flights");
            }

            pw.flush();
        }
    }

    @Override
    public void close() throws IOException {
        if (writer != null) {
            writer.close();
        }
    }
}