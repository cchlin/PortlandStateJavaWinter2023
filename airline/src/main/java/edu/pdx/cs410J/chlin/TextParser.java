package edu.pdx.cs410J.chlin;

import edu.pdx.cs410J.AirlineParser;
import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.StringTokenizer;

/**
 * A skeletal implementation of the <code>TextParser</code> class for Project 2.
 */
public class TextParser implements AirlineParser<Airline> {
  private final Reader reader;

  public TextParser(Reader reader) {
    this.reader = reader;
  }

  /**
   * This method will read the content of the file that is set in constructor
   * and create a new Airline to return. The first line in the file would be
   * the name of the airline. This method will create a new airline after
   * the first line is read. Then the following lines would be flight
   * information. This method will read the flight information and create
   * new flights to add to the airline before this method returns the
   * airline.
   * @return Airline object that is read from the file
   * @throws ParserException When the airline name is missing or BufferedReader has problem, throw the exception
   */
  @Override
  public Airline parse() throws ParserException {
    try (
      BufferedReader br = new BufferedReader(this.reader)
    ) {

      String airlineName = br.readLine();

      if (airlineName == null) {
        throw new ParserException("Missing airline name");
      }

      Airline airline = new Airline(airlineName);

      // start to read flights here
      // 1 flight per line and the information are separated by ","
      String flight = br.readLine();

//      if (flight == null) {
//        throw new ParserException("Missing flights");
//      }

      while (flight != null) {
        StringTokenizer st = new StringTokenizer(flight, ",");
        int flightNumber = Integer.parseInt(st.nextToken());
        String source = st.nextToken();
        String departureTime = st.nextToken();
        String destination = st.nextToken();
        String arrivalTime = st.nextToken();

        airline.addFlight(new Flight(flightNumber, source, departureTime, destination, arrivalTime));

        flight = br.readLine();
      }


      return airline;

    } catch (IOException e) {
      throw new ParserException("While parsing airline text", e);
    }
  }
}