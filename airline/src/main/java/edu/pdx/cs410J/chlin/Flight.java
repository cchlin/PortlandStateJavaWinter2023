package edu.pdx.cs410J.chlin;

import edu.pdx.cs410J.AbstractFlight;

public class Flight extends AbstractFlight {
  
  private int flightNumber;
  private String source;
  private String departureTime;
  private String destination;
  private String arrivalTime;

  public Flight(int flightNumber, String source, String departureTime, String destination, String arrivalTime) {
    this.flightNumber = flightNumber;
    this.source = source;
    this.departureTime = departureTime;
    this.destination = destination;
    this.arrivalTime = arrivalTime;
  }

  @Override
  public int getNumber() {
    return this.flightNumber;
  }

  @Override
  public String getSource() {
    return this.source;
  }

  @Override
  public String getDepartureString() {
    return this.departureTime;
  }

  @Override
  public String getDestination() {
    return this.destination;
  }

  @Override
  public String getArrivalString() {
    return this.arrivalTime;
  }
}
