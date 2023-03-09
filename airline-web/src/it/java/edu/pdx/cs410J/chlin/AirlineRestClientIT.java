package edu.pdx.cs410J.chlin;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Integration test that tests the REST calls made by {@link AirlineRestClient}
 */
@TestMethodOrder(MethodName.class)
class AirlineRestClientIT {
  private static final String HOSTNAME = "localhost";
  private static final String PORT = System.getProperty("http.port", "8080");

  private AirlineRestClient newAirlineRestClient() {
    int port = Integer.parseInt(PORT);
    return new AirlineRestClient(HOSTNAME, port);
  }

  @Test
  void test0RemoveAllAirlines() throws IOException {
    AirlineRestClient client = newAirlineRestClient();
    client.removeAllAirlines();
  }

//  @Test
//  void test1EmptyServerContainsNoAirlines() throws IOException, ParserException {
//    AirlineRestClient client = newAirlineRestClient();
//    Airline airline = client.getAllAirlines();
//    assertThat(airline.size(), equalTo(0));
//  }

  @Test
  void test2CreateFirstFlight() throws IOException, ParserException {
    AirlineRestClient client = newAirlineRestClient();
    String airlineName = "Airline";
    int flightNumberInt = 123;
    String flightNumber = "123";
    String src = "SEA";
    String depart = "3/7/2023 10:36 AM";
    String dest = "PDX";
    String arrive = "3/7/2023 11:39 AM";
    client.addFlight(airlineName, flightNumber, src, depart, dest, arrive);

    Airline airline = client.getAirline(airlineName);
    assertThat(airline.getName(), equalTo(airlineName));
    assertThat(airline.getFlights().iterator().next().getNumber(), equalTo(flightNumberInt));
  }

  @Test
  void test4EmptyWordThrowsException() {
    AirlineRestClient client = newAirlineRestClient();
    String emptyString = "";

    HttpRequestHelper.RestException ex =
      assertThrows(HttpRequestHelper.RestException.class, () -> client.addFlight(emptyString, emptyString, emptyString, emptyString, emptyString, emptyString));
    assertThat(ex.getHttpStatusCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
    assertThat(ex.getMessage(), equalTo(Messages.missingRequiredParameter(AirlineServlet.AIRLINE_NAME_PARAMETER)));
  }

  @Test
  void test5GetFlightsMatchSrcAndDest() throws IOException, ParserException {
    AirlineRestClient client = newAirlineRestClient();
    String airlineName = "Airline";
    int flightNumberInt = 123;
    String flightNumber = "123";
    String src = "SEA";
    String depart = "3/7/2023 10:36 AM";
    String dest = "PDX";
    String arrive = "3/7/2023 11:39 AM";
    client.addFlight(airlineName, flightNumber, src, depart, dest, arrive);
    client.addFlight(airlineName, "456", "NRT", depart, "TPE", arrive);
    client.addFlight(airlineName, "789", src, depart, dest, arrive);

    Airline airline = client.getAirlineWithSrcAndDest(airlineName, src, dest);
    airline.getFlights().forEach(flight -> {
      assertThat(flight.getSource(), equalTo(src));
      assertThat(flight.getDestination(), equalTo(dest));
    });
  }
}
