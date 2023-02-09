package edu.pdx.cs410J.chlin;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
    assertThat(flight.getArrivalString(), equalTo("3/15/23 11:39 PM"));
  }

  /**
   * This unit test will need to be modified (likely deleted) as you implement
   * your project.
   */
  @Test
  void initiallyAllFlightsHaveTheSameNumber() {
    Flight flight = getFlight();
    assertThat(flight.getNumber(), equalTo(42));
  }

  @Test
  void testGetFlightSource() {
    Flight flight = getFlight();
    String source = flight.getSource();
    assertThat(source, equalTo("PDX"));
  }

  private static Flight getFlight() {
    return new Flight(42, "PDX", "3/15/2023 10:36 PM", "SEA", "3/15/2023 11:39 PM");
  }

//  @Test
//  void forProject1ItIsOkayIfGetDepartureTimeReturnsNull() {
//    Flight flight = getFlight();
//    assertThat(flight.getDeparture(), is(nullValue()));
//  }

  @Test
  void testGetDepartureString() {
    Flight flight = getFlight();
    String departureString = flight.getDepartureString();
    assertThat(departureString, equalTo("3/15/23 10:36 PM"));
  }

  @Test
  void testGetFlightDestination() {
    Flight flight = getFlight();
    String destination = flight.getDestination();
    assertThat(destination, equalTo("SEA"));
  }


  @Test
  void testGetArrival() {
    String stringDate = "3/15/2023 11:39 PM";
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy h:mm a");
    Date date = null;
    try {
      date = df.parse(stringDate);
    } catch (ParseException ex) {
      System.err.println("** Bad date: " + stringDate);
    }

    Flight flight = getFlight();
    Date flightDate = flight.getArrival();

    assertThat(date, equalTo(flightDate));
  }

  @Test
  void testGetDeparture() {
    String stringDate = "3/15/2023 10:36 PM";
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy h:mm a");
    Date date = null;
    try {
      date = df.parse(stringDate);
    } catch (ParseException ex) {
      System.err.println("** Bad date: " + stringDate);
    }

    Flight flight = getFlight();
    Date flightDate = flight.getDeparture();

    assertThat(date, equalTo(flightDate));
  }

  @Test
  void testFlightCompareToSameSource() {
    Flight flight1 = getFlight();
    Flight flight2 = new Flight(42, "pdx", "3/15/2023 10:36 PM", "SEA", "3/15/2023 11:39 PM");
    int result = flight1.compareTo(flight2);
    assertTrue(result == 0);
  }
  @Test
  void testFlightCompareToDifferentSource() {
    Flight flight1 = getFlight();
    Flight flight2 = new Flight(42, "pex", "3/15/2023 10:36 PM", "SEA", "3/15/2023 11:39 PM");
    int result = flight1.compareTo(flight2);
    assertTrue(result < 0);
  }
  @Test
  void testFlightCompareToSameSourceDifferentDepartureTime() {
    Flight flight1 = getFlight();
    Flight flight2 = new Flight(42, "PDX", "3/15/2023 5:36 PM", "SEA", "3/15/2023 11:39 PM");
    int result = flight1.compareTo(flight2);
    assertTrue(result > 0);
  }

  @Test
  void testFlightCompareToSameSourceSameDepartureTime() {
    Flight flight1 = getFlight();
    Flight flight2 = new Flight(42, "PDX", "3/15/2023 10:36 PM", "SEA", "3/15/2023 11:39 PM");
    int result = flight1.compareTo(flight2);
    assertTrue(result == 0);
  }

}
