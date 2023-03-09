package edu.pdx.cs410J.chlin;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * A unit test for the REST client that demonstrates using mocks and
 * dependency injection
 */
public class AirlineRestClientTest {

  @Test
  void getAllDictionaryEntriesPerformsHttpGetWithNoParameters() throws ParserException, IOException {
    String airlineName = "Airline";
    int flightNumberInt = 123;
    String src = "SEA";
    String depart = "3/7/2023 10:36 AM";
    String dest = "PDX";
    String arrive = "3/7/2023 11:39 AM";
    Airline airline = new Airline(airlineName);
    airline.addFlight(new Flight(flightNumberInt, src, depart, dest, arrive));

    HttpRequestHelper http = mock(HttpRequestHelper.class);
    when(http.get(eq(Map.of(AirlineServlet.AIRLINE_NAME_PARAMETER, airlineName)))).thenReturn(airlineAsText(airline));
    
    AirlineRestClient client = new AirlineRestClient(http);

    Airline read = client.getAirline(airlineName);
    assertThat(read.getName(), equalTo(airlineName));
    assertThat(read.getFlights().iterator().next().getNumber(), equalTo(flightNumberInt));
  }

  private HttpRequestHelper.Response airlineAsText(Airline airline) throws IOException {
    StringWriter writer = new StringWriter();
    XmlDumper dumper = new XmlDumper(writer);
    dumper.dump(airline);

    return new HttpRequestHelper.Response(writer.toString());
  }


}
