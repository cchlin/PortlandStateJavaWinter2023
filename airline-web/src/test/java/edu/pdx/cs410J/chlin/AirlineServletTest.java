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
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);

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

    servlet.doPost(request, response);

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
    int flightNumber2 = 456;
    int flightNumber3 = 789;
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

    String flightNumberAsString2 = String.valueOf(flightNumber2);

    HttpServletRequest request2 = mock(HttpServletRequest.class);
    when(request2.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request2.getParameter(AirlineServlet.FLIGHT_NUMBER_PARAMETER)).thenReturn(flightNumberAsString2);
    when(request2.getParameter(AirlineServlet.SRC_PARAMETER)).thenReturn("HND");
    when(request2.getParameter(AirlineServlet.DEPART_PARAMETER)).thenReturn(depart);
    when(request2.getParameter(AirlineServlet.DEST_PARAMETER)).thenReturn("TPE");
    when(request2.getParameter(AirlineServlet.ARRIVE_PARAMETER)).thenReturn(arrive);

    HttpServletResponse response2 = mock(HttpServletResponse.class);

    servlet.doPost(request2, response2);
    verify(response2).setStatus(HttpServletResponse.SC_OK);

    String flightNumberAsString3 = String.valueOf(flightNumber3);

    HttpServletRequest request3 = mock(HttpServletRequest.class);
    when(request3.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request3.getParameter(AirlineServlet.FLIGHT_NUMBER_PARAMETER)).thenReturn(flightNumberAsString3);
    when(request3.getParameter(AirlineServlet.SRC_PARAMETER)).thenReturn(src);
    when(request3.getParameter(AirlineServlet.DEPART_PARAMETER)).thenReturn(depart);
    when(request3.getParameter(AirlineServlet.DEST_PARAMETER)).thenReturn(dest);
    when(request3.getParameter(AirlineServlet.ARRIVE_PARAMETER)).thenReturn(arrive);

    HttpServletResponse response3 = mock(HttpServletResponse.class);

    servlet.doPost(request3, response3);
    verify(response3).setStatus(HttpServletResponse.SC_OK);


    Flight f1 = new Flight(123, src, "3/7/2023 10:36 AM", dest, "3/7/2023 11:39 AM");
    Flight f2 = new Flight(456, "NRT", "3/8/2023 2:18 PM", "TPE", "3/8/2023 3:29 PM");
    Flight f3 = new Flight(789, src, "3/8/2023 2:18 PM", dest, "3/8/2023 3:29 PM");
    Airline airline = new Airline(airlineName);
    airline.addFlight(f1);
    airline.addFlight(f2);
    airline.addFlight(f3);


    HttpServletRequest request4 = mock(HttpServletRequest.class);
    when(request4.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request4.getParameter(AirlineServlet.SRC_PARAMETER)).thenReturn(src);
    when(request4.getParameter(AirlineServlet.DEST_PARAMETER)).thenReturn(dest);

    HttpServletResponse response4 = mock(HttpServletResponse.class);

    StringWriter stringWriter = new StringWriter();
    PrintWriter pw = new PrintWriter(stringWriter, true);
    when(response4.getWriter()).thenReturn(pw);

    servlet.doGet(request4, response4);

    ArgumentCaptor<Integer> statusCode = ArgumentCaptor.forClass(Integer.class);
    verify(response4).setStatus(statusCode.capture());
    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_OK));

    String xml = stringWriter.toString();
    assertThat(xml, containsString("123"));
    assertThat(xml, containsString("789"));
  }

  @Test
  void testOnlyAirlineIsSpecified() throws IOException {
    AirlineServlet servlet = new AirlineServlet();

    String airlineName = "Fake Airline";
    int flightNumber = 123;
    int flightNumber2 = 456;
    int flightNumber3 = 789;
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

    String flightNumberAsString2 = String.valueOf(flightNumber2);

    HttpServletRequest request2 = mock(HttpServletRequest.class);
    when(request2.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request2.getParameter(AirlineServlet.FLIGHT_NUMBER_PARAMETER)).thenReturn(flightNumberAsString2);
    when(request2.getParameter(AirlineServlet.SRC_PARAMETER)).thenReturn("HND");
    when(request2.getParameter(AirlineServlet.DEPART_PARAMETER)).thenReturn(depart);
    when(request2.getParameter(AirlineServlet.DEST_PARAMETER)).thenReturn("TPE");
    when(request2.getParameter(AirlineServlet.ARRIVE_PARAMETER)).thenReturn(arrive);

    HttpServletResponse response2 = mock(HttpServletResponse.class);

    servlet.doPost(request2, response2);
    verify(response2).setStatus(HttpServletResponse.SC_OK);

    String flightNumberAsString3 = String.valueOf(flightNumber3);

    HttpServletRequest request3 = mock(HttpServletRequest.class);
    when(request3.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);
    when(request3.getParameter(AirlineServlet.FLIGHT_NUMBER_PARAMETER)).thenReturn(flightNumberAsString3);
    when(request3.getParameter(AirlineServlet.SRC_PARAMETER)).thenReturn(src);
    when(request3.getParameter(AirlineServlet.DEPART_PARAMETER)).thenReturn(depart);
    when(request3.getParameter(AirlineServlet.DEST_PARAMETER)).thenReturn(dest);
    when(request3.getParameter(AirlineServlet.ARRIVE_PARAMETER)).thenReturn(arrive);

    HttpServletResponse response3 = mock(HttpServletResponse.class);

    servlet.doPost(request3, response3);
    verify(response3).setStatus(HttpServletResponse.SC_OK);


    Flight f1 = new Flight(123, src, "3/7/2023 10:36 AM", dest, "3/7/2023 11:39 AM");
    Flight f2 = new Flight(456, "HND", "3/8/2023 2:18 PM", "TPE", "3/8/2023 3:29 PM");
    Flight f3 = new Flight(789, src, "3/8/2023 2:18 PM", dest, "3/8/2023 3:29 PM");
    Airline airline = new Airline(airlineName);
    airline.addFlight(f1);
    airline.addFlight(f2);
    airline.addFlight(f3);


    HttpServletRequest request4 = mock(HttpServletRequest.class);
    when(request4.getParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)).thenReturn(airlineName);

    HttpServletResponse response4 = mock(HttpServletResponse.class);

    StringWriter stringWriter = new StringWriter();
    PrintWriter pw = new PrintWriter(stringWriter, true);
    when(response4.getWriter()).thenReturn(pw);

    servlet.doGet(request4, response4);

    ArgumentCaptor<Integer> statusCode = ArgumentCaptor.forClass(Integer.class);
    verify(response4).setStatus(statusCode.capture());
    assertThat(statusCode.getValue(), equalTo(HttpServletResponse.SC_OK));

    String xml = stringWriter.toString();
    assertThat(xml, containsString("123"));
    assertThat(xml, containsString("456"));
    assertThat(xml, containsString("789"));
  }

}
