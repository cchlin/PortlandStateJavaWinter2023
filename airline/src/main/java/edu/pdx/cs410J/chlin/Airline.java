package edu.pdx.cs410J.chlin;

import edu.pdx.cs410J.AbstractAirline;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Airline class that stores its name and a list of flights
 */
public class Airline extends AbstractAirline<Flight> {

  /**
   * The name of the airline
   */
  private final String name;
  /**
   * A list of stored flights
   */
  private Collection<Flight> flights = new ArrayList<>();

  /**
   * Airline constructor that creates an airline
   * @param name the name of the airline
   */
  public Airline(String name) {
    this.name = name;
  }

  /**
   * get the name of the airline
   * @return name of the airline
   */
  @Override
  public String getName() {
    return this.name;
  }

  /**
   * Add one flight into the flight list
   * @param flight A flight objecct
   */
  @Override
  public void addFlight(Flight flight) {
    List<Flight> lFlights = (List<Flight>) flights;
    // use as the index
    int index = 0;
    int size = lFlights.size();
    for (int i = 0; i < size; i++) {
      if (flight.compareTo(lFlights.get(i)) > 0) {
        index = i + 1;
      }
    }
    lFlights.add(index, flight);
    this.flights =  lFlights;
  }

  /**
   * Return the elements in the flight list
   * @return returns all flights in the list
   */
  @Override
  public Collection<Flight> getFlights() {
    return flights;
  }
}
