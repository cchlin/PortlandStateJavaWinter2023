package edu.pdx.cs410J.chlin;

import edu.pdx.cs410J.ParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TextDumperTest {

  @Test
  void airlineNameIsDumpedInTextFormat() throws IOException {
    String airlineName = "Test Airline";
    Airline airline = new Airline(airlineName);
    
    Flight flight = getFlight();
    airline.addFlight(flight);

    StringWriter sw = new StringWriter();
    TextDumper dumper = new TextDumper(sw);
    dumper.dump(airline);

    String text = sw.toString();
    assertThat(text, containsString(airlineName));
  }

  private static Flight getFlight() {
    return new Flight(123, "PDX", "3/13/2023 10:39 PM", "SEA", "3/13/2023 11:39 PM");
  }

  @Test
  void canParseTextWrittenByTextDumper(@TempDir File tempDir) throws IOException, ParserException {
    String airlineName = "Test Airline";
    Airline airline = new Airline(airlineName);

    Flight flight = getFlight();
    airline.addFlight(flight);

    File textFile = new File(tempDir, "airline.txt");
    TextDumper dumper = new TextDumper(new FileWriter(textFile));
    dumper.dump(airline);

    TextParser parser = new TextParser(new FileReader(textFile));
    Airline read = parser.parse();
    assertThat(read.getName(), equalTo(airlineName));
  }

  @Test
  void emptyFlightThrownException(@TempDir File tempDir) throws IOException {
    String airlineName = "Test Airline";
    Airline airline = new Airline(airlineName);

    File textFile = new File(tempDir, "airline.txt");
    TextDumper dumper = new TextDumper(new FileWriter(textFile));
    assertThrows(IOException.class, () -> dumper.dump(airline));
  }
}
