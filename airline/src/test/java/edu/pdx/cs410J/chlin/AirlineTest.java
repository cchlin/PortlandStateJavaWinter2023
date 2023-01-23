package edu.pdx.cs410J.chlin;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

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
}
