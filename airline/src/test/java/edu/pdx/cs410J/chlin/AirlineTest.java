package edu.pdx.cs410J.chlin;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Unit tests for the {@link Airline} class.
 */

public class AirlineTest {
    private static Airline getAirline() {
        return new Airline("Alaska Airline");
    }

    @Test
    void testAirlineGetName() {
        Airline airline = getAirline();
        assertThat(airline.getName(), equalTo("Alaska Airline"));
    }


    @Test
    void testAddFlightAndGetFlights() {
        Airline airline = getAirline();
        Flight flight = new Flight(42, "PDX", "3/15/2023 10:39", "SEA", "3/15/2023 11:39");
        airline.addFlight(flight);
        Collection<Flight> c = new ArrayList<>();
        c.add(flight);
        assertEquals(c, airline.getFlights());
    }
}
