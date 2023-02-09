package edu.pdx.cs410J.chlin;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class PrettyPrinterTest {

    @Test
    void testPrettyFormat() {
        Airline airline = new Airline("Alaska Airline");
        Flight flight1 = new Flight(41, "pdx", "3/15/2023 10:36 PM", "SEA", "3/15/2023 11:39 PM");
        Flight flight2 = new Flight(42, "pdx", "3/15/2023 7:36 PM", "SEA", "3/15/2023 11:39 PM");
        Flight flight3 = new Flight(43, "PDX", "3/15/2023 8:36 PM", "SEA", "3/15/2023 11:39 PM");
        Flight flight4 = new Flight(44, "pDx", "3/14/2023 10:36 PM", "SEA", "3/15/2023 11:36 PM");
        Flight flight5 = new Flight(45, "pdx", "3/12/2023 10:36 PM", "SEA", "3/15/2023 11:39 PM");
        airline.addFlight(flight1);
        airline.addFlight(flight2);
        airline.addFlight(flight3);
        airline.addFlight(flight4);
        airline.addFlight(flight5);

        PrettyPrinter pp = new PrettyPrinter();
        ArrayList<String> output = pp.formatOutput(airline);
        for (String line : output) {
            System.out.println(line);
        }

    }
}
