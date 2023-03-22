package edu.pdx.cs410j.chlin;

import edu.pdx.cs410J.AbstractFlight;
import edu.pdx.cs410J.AirportNames;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Flight class that has flight number, departure airport code, departure date and time
 * destination airport code, and arrival date and time
 */
public class Flight extends AbstractFlight implements Comparable<Flight>, Serializable {

  /**
   * Flight number of this flight object
   */
  private int flightNumber;
  /**
   * The three-letter departure airport code
   */
  private String source;
  /**
   * departure date and time
   * MM/DD/YYYY hh:mm
   */
  private Date departureTime;
  /**
   * Three-letter destination airport code
   */
  private String destination;
  /**
   * arrival date and time
   * MM/DD/YYYY hh:mm
   */
  private Date arrivalTime;

  /**
   * flight constructor
   * @param flightNumber flight number
   * @param source departure airport code
   * @param departureTime departure date and time
   * @param destination destination ariport code
   * @param arrivalTime arrival date and time
   */
  public Flight(int flightNumber, String source, String departureTime, String destination, String arrivalTime) {
    this.flightNumber = flightNumber;
    this.source = source.toUpperCase();
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy h:mm a");
    Date departDate = null;
    try {
      departDate = df.parse(departureTime);
    } catch (ParseException ex) {
      System.err.println("** Bad departure time: " + departureTime);
    }
    this.departureTime = departDate;
    this.destination = destination.toUpperCase();
    Date arriveDate = null;
    try {
      arriveDate = df.parse(arrivalTime);
    } catch (ParseException ex) {
      System.err.println("** Bad arrival time: " + arrivalTime);
    }
    this.arrivalTime = arriveDate;
  }

  public Flight(Flight flight) {
    this.flightNumber = flight.getNumber();
    this.source = flight.getSource();
    this.departureTime = flight.getDeparture();
    this.destination = flight.getDestination();
    this.arrivalTime = flight.getArrival();
  }

  /**
   * get the flight number
   * @return flight number
   */
  @Override
  public int getNumber() {
    return this.flightNumber;
  }

  /**
   * get the departure airport
   * @return airport code of the departure airport
   */
  @Override
  public String getSource() {
    return this.source;
  }

  /**
   * get the departure date and time
   * @return departure date and time
   */
  @Override
  public String getDepartureString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getDate(departureTime)).append(" ").append(getTime(departureTime));

    return sb.toString();
  }

  /**
   * get the destination airport code
   * @return destination aiport code
   */
  @Override
  public String getDestination() {
    return this.destination;
  }

  /**
   * get the arrival date and time
   * @return arrival date and time
   */
  @Override
  public String getArrivalString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getDate(arrivalTime)).append(" ").append(getTime(arrivalTime));

    return sb.toString();
  }

  /**
   * get the Date of the arrival time
   * @return Date object of the arrival time
   */
  @Override
  public Date getArrival() {
    return this.arrivalTime;
  }

  /**
   * get the Date of the departure time
   * @return Date object of the departure time
   */
  @Override
  public Date getDeparture() {
    return this.departureTime;
  }

  /**
   * compare to another Flight object first compare their source airport code
   * if their source airport code are the same, then compare their departure time
   * @param other another Flight object to be compared.
   * @return return 0 if the source airport code are the same and the departure tiems
   * are also the same. return &lt; 0 if the source airport code is &lt; them the arugment
   * airport code or the source airport code is the same but the departure time is
   * earlier than the argument Flight object's departure time. Otherwise return
   * > 0.
   */
  @Override
  public int compareTo(Flight other) {
    int result = this.source.compareToIgnoreCase(other.getSource());
    if (result != 0) {
      return result;
    } else {
      return this.departureTime.compareTo(other.getDeparture());
    }
  }

  /**
   * get only the date part of the Date object
   * @param date Date object. Departure or arrival
   * @return string representation of the date part of the Date
   */
  private String getDate(Date date) {
    int f = DateFormat.SHORT;
    DateFormat df = DateFormat.getDateInstance(f);
    return df.format(date);
  }

  /**
   * get only the time part of the Date object
   * @param date Date object. Departure or arrival
   * @return string representation of the time part of the Date
   */
  private String getTime(Date date) {
    int f = DateFormat.SHORT;
    DateFormat df = DateFormat.getTimeInstance(f);
    return df.format(date);
  }

  /**
   * check if the airport code is in the list of AirportName
   * @param airportCode the airport code, string
   * @return return true if the airport code is found in AirportName, return
   * false otherwise.
   */
  public boolean airportCodeIsLegit(String airportCode) {
    if (AirportNames.getName(airportCode) != null) {
      return true;
    }
    return false;
  }

  /**
   * check if the arrival time is not earlier than the depatrure time
   * @return return true if the flight's depature is earlier, false otherwise
   */
  public boolean departureTimeIsEarlier() {
    return this.departureTime.before(this.arrivalTime);
  }
}
