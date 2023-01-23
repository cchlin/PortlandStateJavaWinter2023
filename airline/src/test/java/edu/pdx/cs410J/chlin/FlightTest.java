package edu.pdx.cs410J.chlin;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link Flight} class.
 *
 * You'll need to update these unit tests as you build out you program.
 */
public class FlightTest {

  /**
   * This unit test will need to be modified (likely deleted) as you implement
   * your project.
   */
  @Test
  void getArrivalStringNeedsToBeImplemented() {
    Flight flight = getFlight();
    assertThrows(UnsupportedOperationException.class, flight::getArrivalString);
  }

  /**
   * This unit test will need to be modified (likely deleted) as you implement
   * your project.
   */
  @Test
  void initiallyAllFlightsHaveTheSameNumber() {
    Flight flight = getFlight();
    assertThat(flight.getArrivalString(), equalTo("3/15/2023 11:39"));
  }

  @Test
  void testGetFlightSource() {
    Flight flight = getFlight();
    String source = flight.getSource();
    assertThat(source, equalTo("PDX"));
  }

  private static Flight getFlight() {
    return new Flight(42, "PDX", "3/15/2023 10:39", "SEA", "3/15/2023 11:39");
  }

  @Test
  void forProject1ItIsOkayIfGetDepartureTimeReturnsNull() {
    Flight flight = getFlight();
    assertThat(flight.getDeparture(), is(nullValue()));
  }

  @Test
  void testGetDepartureString() {
    Flight flight = getFlight();
    String departureString = flight.getDepartureString();
    assertThat(departureString, equalTo("3/15/2023 10:39"));
  }

  @Test
  void testGetFlightDestination() {
    Flight flight = getFlight();
    String destination = flight.getDestination();
    assertThat(destination, equalTo("SEA"));
  }

}
