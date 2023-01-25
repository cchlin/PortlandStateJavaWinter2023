package edu.pdx.cs410J.chlin;

import edu.pdx.cs410J.AbstractFlight;

/**
 * Flight class that has flight number, departure airport code, departure date and time
 * destination airport code, and arrival date and time
 */
public class Flight extends AbstractFlight {

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
  private String departureTime;
  /**
   * Three-letter destination airport code
   */
  private String destination;
  /**
   * arrival date and time
   * MM/DD/YYYY hh:mm
   */
  private String arrivalTime;

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
    this.source = source;
    this.departureTime = departureTime;
    this.destination = destination;
    this.arrivalTime = arrivalTime;
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
    return this.departureTime;
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
    return this.arrivalTime;
  }
}
