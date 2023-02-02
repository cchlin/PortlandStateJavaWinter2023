package edu.pdx.cs410J.chlin;

import edu.pdx.cs410J.AirlineDumper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;

/**
 * A skeletal implementation of the <code>TextDumper</code> class for Project 2.
 */
public class TextDumper implements AirlineDumper<Airline> {
  private final Writer writer;

  public TextDumper(Writer writer) {
    this.writer = writer;
  }

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
}
