package edu.pdx.cs410J.chlin;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TextDumperParserTest {

  @Test
  void airlineWithOneFlightCanBeDumpedAndParsed() throws ParserException {
    String airlineName = "Airline";
    int flightNumberInt = 123;
    String flightNumber = "123";
    String src = "SEA";
    String depart = "3/7/2023 10:36 AM";
    String dest = "PDX";
    String arrive = "3/7/2023 11:39 AM";
    Airline airline = new Airline(airlineName);
    airline.addFlight(new Flight(flightNumberInt, src, depart, dest, arrive));

    Airline read = dumpAndParse(airline);
    assertThat(read.getName(), equalTo(airlineName));
    assertThat(read.getFlights().iterator().next().getNumber(), equalTo(flightNumberInt));
  }

  private Airline dumpAndParse(Airline airline) throws ParserException {
    StringWriter sw = new StringWriter();
    TextDumper dumper = new TextDumper(sw);
    dumper.dump(airline);

    String text = sw.toString();

    TextParser parser = new TextParser(new StringReader(text));
    return parser.parse();
  }

//  @Test
//  void dumpedTextCanBeParsed() throws ParserException {
//    Map<String, String> map = Map.of("one", "1", "two", "2");
//    Map<String, String> read = dumpAndParse(map);
//    assertThat(read, equalTo(map));
//  }
}
