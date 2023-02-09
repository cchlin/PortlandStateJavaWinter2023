package edu.pdx.cs410J.chlin;

import edu.pdx.cs410J.AbstractFlight;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DateFormat;

/**
 * Flight class that has flight number, departure airport code, departure date and time
 * destination airport code, and arrival date and time
 */
public class Flight extends AbstractFlight implements Comparable<Flight> {

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
      System.err.println("** Bad time: " + departureTime);
      System.err.println(ex);
    }
    this.departureTime = departDate;
    this.destination = destination.toUpperCase();
    Date arriveDate = null;
    try {
      arriveDate = df.parse(arrivalTime);
    } catch (ParseException ex) {
      System.err.println("** Bad time: " + arrivalTime);
      System.err.println(ex);
    }
    this.arrivalTime = arriveDate;
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

  @Override
  public Date getArrival() {
    return this.arrivalTime;
  }

  @Override
  public Date getDeparture() {
    return this.departureTime;
  }

  @Override
  public int compareTo(Flight other) {
    int result = this.source.compareToIgnoreCase(other.getSource());
    if (result != 0) {
      return result;
    } else {
      return this.departureTime.compareTo(other.getDeparture());
    }
  }

  private String getDate(Date date) {
    int f = DateFormat.SHORT;
    DateFormat df = DateFormat.getDateInstance(f);
    return df.format(date);
  }

  private String getTime(Date date) {
    int f = DateFormat.SHORT;
    DateFormat df = DateFormat.getTimeInstance(f);
    return df.format(date);
  }
}
