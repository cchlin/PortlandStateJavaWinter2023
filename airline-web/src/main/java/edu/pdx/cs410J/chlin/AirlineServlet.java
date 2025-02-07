package edu.pdx.cs410J.chlin;

import com.google.common.annotations.VisibleForTesting;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>Airline</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple dictionary of words
 * and their definitions.
 */
public class AirlineServlet extends HttpServlet {
  static final String AIRLINE_NAME_PARAMETER = "airline";
  static final String FLIGHT_NUMBER_PARAMETER = "flightNumber";
  static final String SRC_PARAMETER = "src";
  static final String DEPART_PARAMETER = "depart";
  static final String DEST_PARAMETER = "dest";
  static final String ARRIVE_PARAMETER = "arrive";

  private final Map<String, Airline> airlines = new HashMap<>();

  /**
   * Handles an HTTP GET request from a client by writing the definition of the
   * word specified in the "word" HTTP parameter to the HTTP response.  If the
   * "word" parameter is not specified, all of the entries in the dictionary
   * are written to the HTTP response.
   */
  @Override
  protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
      response.setContentType( "text/xml" );

      String airlineName = getParameter(AIRLINE_NAME_PARAMETER, request );
      String src = getParameter(SRC_PARAMETER, request);
      String dest = getParameter(DEST_PARAMETER, request);

      Airline airline = getAirline(airlineName);
      if (airline == null) {
          response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      }
      else if (src != null && dest != null) {
          getFlightsWithSrcAndDest(airline, src, dest, response);
      } else {
          getAllFlightsFromAirline(airline, response);
      }
  }

  /**
   * Handles an HTTP POST request by storing the dictionary entry for the
   * "word" and "definition" request parameters.  It writes the dictionary
   * entry to the HTTP response.
   */
  @Override
  protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
      response.setContentType( "text/plain" );

      String airlineName = getParameter(AIRLINE_NAME_PARAMETER, request );
      if (airlineName == null) {
          missingRequiredParameter(response, AIRLINE_NAME_PARAMETER);
          return;
      }

      String flightNumberAsString = getParameter(FLIGHT_NUMBER_PARAMETER, request );
      if (flightNumberAsString == null) {
          missingRequiredParameter( response, FLIGHT_NUMBER_PARAMETER);
          return;
      }
      int flightNumber = 0;
      try {
         flightNumber = Integer.parseInt(flightNumberAsString);
      } catch (NumberFormatException ex) {
          missingRequiredParameter( response, FLIGHT_NUMBER_PARAMETER);
          return;
      }

      String src = getParameter(SRC_PARAMETER, request);
      if (src == null) {
          missingRequiredParameter(response, SRC_PARAMETER);
          return;
      }

      String depart = getParameter(DEPART_PARAMETER, request);
      if (depart == null) {
          missingRequiredParameter(response, DEPART_PARAMETER);
          return;
      }

      String dest = getParameter(DEST_PARAMETER, request);
      if (dest == null) {
          missingRequiredParameter(response, DEST_PARAMETER);
          return;
      }

      String arrive = getParameter(ARRIVE_PARAMETER, request);
      if (arrive == null) {
          missingRequiredParameter(response, ARRIVE_PARAMETER);
          return;
      }

      Airline airline = this.airlines.get(airlineName);
      if (airline == null) {
          airline = new Airline(airlineName);
          this.airlines.put(airlineName, airline);
      }

      airline.addFlight(new Flight(flightNumber, src, depart, dest, arrive));


//      PrintWriter pw = response.getWriter();
//      pw.println(Messages.addFlightMessage(airlineName, flightNumberAsString, src, depart, dest, arrive));
//      pw.flush();

      response.setStatus( HttpServletResponse.SC_OK);
  }

  /**
   * Handles an HTTP DELETE request by removing all dictionary entries.  This
   * behavior is exposed for testing purposes only.  It's probably not
   * something that you'd want a real application to expose.
   */
  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
      response.setContentType("application/plain");

      this.airlines.clear();

      PrintWriter pw = response.getWriter();
      pw.println(Messages.allAirlinesDeleted());
      pw.flush();

      response.setStatus(HttpServletResponse.SC_OK);

  }

  /**
   * Writes an error message about a missing parameter to the HTTP response.
   *
   * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
   */
  private void missingRequiredParameter( HttpServletResponse response, String parameterName )
      throws IOException
  {
      String message = Messages.missingRequiredParameter(parameterName);
      response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
  }

  /**
   * Writes the definition of the given word to the HTTP response.
   *
   * The text of the message is formatted with {@link TextDumper}
   */
  private void getFlightsWithSrcAndDest(Airline airline, String src, String dest, HttpServletResponse response) throws IOException {
    Airline airlineToReturn = new Airline(airline.getName());

    if (airline == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } else {
        Collection<Flight> flights = airline.getFlights();
        String flightSrc = "";
        String flightDest = "";
        for (Flight flight : flights) {
            flightSrc = flight.getSource();
            flightDest = flight.getDestination();
            if (flightSrc.equals(src) && flightDest.equals(dest)) {
                airlineToReturn.addFlight(new Flight(flight));
            }
        }

        new XmlDumper(new PrintWriter(response.getWriter(), true)).dump(airlineToReturn);

      response.setStatus(HttpServletResponse.SC_OK);
    }
  }

  /**
   * Writes all of the dictionary entries to the HTTP response.
   *
   * The text of the message is formatted with {@link TextDumper}
   */
  private void getAllFlightsFromAirline(Airline airline, HttpServletResponse response ) throws IOException
  {
      Airline airlineToReturn = airlines.get(airline.getName());

      new XmlDumper(new PrintWriter(response.getWriter(), true)).dump(airlineToReturn);

      response.setStatus( HttpServletResponse.SC_OK );
  }

  /**
   * Returns the value of the HTTP request parameter with the given name.
   *
   * @return <code>null</code> if the value of the parameter is
   *         <code>null</code> or is the empty string
   */
  private String getParameter(String name, HttpServletRequest request) {
    String value = request.getParameter(name);
    if (value == null || "".equals(value)) {
      return null;

    } else {
      return value;
    }
  }

  @VisibleForTesting
  Airline getAirline(String airlineName) {
      return this.airlines.get(airlineName);
  }
}


