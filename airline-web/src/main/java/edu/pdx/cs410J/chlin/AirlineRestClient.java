package edu.pdx.cs410J.chlin;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import static edu.pdx.cs410J.web.HttpRequestHelper.Response;
import static edu.pdx.cs410J.web.HttpRequestHelper.RestException;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * A helper class for accessing the rest client.  Note that this class provides
 * an example of how to make gets and posts to a URL.  You'll need to change it
 * to do something other than just send dictionary entries.
 */
public class AirlineRestClient
{
    private static final String WEB_APP = "airline";
    private static final String SERVLET = "flights";

    private final HttpRequestHelper http;


    /**
     * Creates a client to the airline REST service running on the given host and port
     * @param hostName The name of the host
     * @param port The port
     */
    public AirlineRestClient( String hostName, int port )
    {
        this(new HttpRequestHelper(String.format("http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET)));
    }

    @VisibleForTesting
    AirlineRestClient(HttpRequestHelper http) {
      this.http = http;
    }

//  /**
//   * Returns all dictionary entries from the server
//   */
//  public Map<String, String> getAllAirlines() throws IOException, ParserException {
//    Response response = http.get(Map.of());
//    throwExceptionIfNotOkayHttpStatus(response);
//
//    TextParser parser = new TextParser(new StringReader(response.getContent()));
//    return parser.parse();
//  }

  /**
   * Returns the definition for the given word
   */
  public Airline getAirline(String airlineName) throws IOException, ParserException {
    Response response = http.get(Map.of(AirlineServlet.AIRLINE_NAME_PARAMETER, airlineName));
    throwExceptionIfNotOkayHttpStatus(response);
    String content = response.getContent();

    TextParser parser = new TextParser(new StringReader(content));
    return parser.parse();
  }

  public void addFlight(String airlineName, String flightNumber, String src, String depart, String dest, String arrive) throws IOException {
    Response response = http.post(Map.of(AirlineServlet.AIRLINE_NAME_PARAMETER, airlineName,
            AirlineServlet.FLIGHT_NUMBER_PARAMETER, flightNumber,
            AirlineServlet.SRC_PARAMETER, src,
            AirlineServlet.DEPART_PARAMETER, depart,
            AirlineServlet.DEST_PARAMETER, dest,
            AirlineServlet.ARRIVE_PARAMETER, arrive));
    throwExceptionIfNotOkayHttpStatus(response);
  }

  public void removeAllAirlines() throws IOException {
    Response response = http.delete(Map.of());
    throwExceptionIfNotOkayHttpStatus(response);
  }

  private void throwExceptionIfNotOkayHttpStatus(Response response) {
    int code = response.getHttpStatusCode();
    if (code != HTTP_OK) {
      String message = response.getContent();
      throw new RestException(code, message);
    }
  }

}
