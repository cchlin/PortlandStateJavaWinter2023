package edu.pdx.cs410J.chlin;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

/**
 * A unit test for the {@link AirlineServlet}.  It uses mockito to
 * provide mock http requests and responses.
 */
class AirlineServletTest {

  @Test
  void gettingFlightsForNonExistentAirlineReturns404() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn("Airline");

    HttpServletResponse response = mock(HttpServletResponse.class);
//    PrintWriter pw = mock(PrintWriter.class);
//
//    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);

    verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
  }

  @Test
  void addFlightInNewAirline() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    String airlineName = "Fake Airline";
    int flightNumber = 123;
    String flightNumberAsString = String.valueOf(flightNumber);
    String src = "SEA";
    String depart = "3/7/2023 10:36 AM";
    String dest = "PDX";
    String arrive = "3/7/2023 11:39 AM";

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request.getParameter(AirlineServlet.FLIGHT_NUMBER_PARAMETER)).thenReturn(flightNumberAsString);
    when(request.getParameter(AirlineServlet.SRC_PARAMETER)).thenReturn(src);
    when(request.getParameter(AirlineServlet.DEPART_PARAMETER)).thenReturn(depart);
    when(request.getParameter(AirlineServlet.DEST_PARAMETER)).thenReturn(dest);
    when(request.getParameter(AirlineServlet.ARRIVE_PARAMETER)).thenReturn(arrive);

    HttpServletResponse response = mock(HttpServletResponse.class);

    // Use a StringWriter to gather the text from multiple calls to println()
//    StringWriter stringWriter = new StringWriter();
//    PrintWriter pw = new PrintWriter(stringWriter, true);
//
//    when(response.getWriter()).thenReturn(pw);

    servlet.doPost(request, response);

//    String xml = stringWriter.toString();
//    assertThat(xml, containsString(airlineName));
//    assertThat(xml, containsString(flightNumberAsString));
//    assertThat(xml, containsString(src));
//    assertThat(xml, containsString(depart));
//    assertThat(xml, containsString(dest));
//    assertThat(xml, containsString(arrive));

    // Use an ArgumentCaptor when you want to make multiple assertions against the value passed to the mock
    ArgumentCaptor<Integer> statusCode = ArgumentCaptor.forClass(Integer.class);
    verify(response).setStatus(statusCode.capture());

    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_OK));

    Airline airline = servlet.getAirline(airlineName);
    assertThat(airline.getName(), equalTo(airlineName));

    Flight flight = airline.getFlights().iterator().next();
    assertThat(flight.getNumber(), equalTo(flightNumber));
  }

  @Test
  void testGetFlightsWithSrcAndDest() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    String airlineName = "Fake Airline";
    int flightNumber = 123;
    String flightNumberAsString = String.valueOf(flightNumber);
    String src = "SEA";
    String depart = "3/7/2023 10:36 AM";
    String dest = "PDX";
    String arrive = "3/7/2023 11:39 AM";

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request.getParameter(AirlineServlet.FLIGHT_NUMBER_PARAMETER)).thenReturn(flightNumberAsString);
    when(request.getParameter(AirlineServlet.SRC_PARAMETER)).thenReturn(src);
    when(request.getParameter(AirlineServlet.DEPART_PARAMETER)).thenReturn(depart);
    when(request.getParameter(AirlineServlet.DEST_PARAMETER)).thenReturn(dest);
    when(request.getParameter(AirlineServlet.ARRIVE_PARAMETER)).thenReturn(arrive);

    HttpServletResponse response = mock(HttpServletResponse.class);

    servlet.doPost(request, response);
    verify(response).setStatus(HttpServletResponse.SC_OK);


    Flight f1 = new Flight(123, src, "3/7/2023 10:36 AM", dest, "3/7/2023 11:39 AM");
    Flight f2 = new Flight(456, src, "3/8/2023 2:18 PM", dest, "3/8/2023 3:29 PM");
    Airline airline = new Airline(airlineName);
    airline.addFlight(f1);
    airline.addFlight(f2);

    HttpServletRequest request2 = mock(HttpServletRequest.class);
    when(request2.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request2.getParameter(AirlineServlet.SRC_PARAMETER)).thenReturn(src);
    when(request2.getParameter(AirlineServlet.DEST_PARAMETER)).thenReturn(dest);

    HttpServletResponse response2 = mock(HttpServletResponse.class);

    servlet.doGet(request2, response2);

    ArgumentCaptor<Integer> statusCode = ArgumentCaptor.forClass(Integer.class);
    verify(response2).setStatus(statusCode.capture());
    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_OK));

    Airline a = servlet.getAirline(airlineName);
    assertThat(a.getName(), equalTo(airlineName));

    Flight tf1 = a.getFlights().iterator().next();
    assertThat(tf1.getNumber(), equalTo("123"));
    Flight tf2 = a.getFlights().iterator().next();
    assertThat(tf2.getNumber(), equalTo("456"));

  }

}
