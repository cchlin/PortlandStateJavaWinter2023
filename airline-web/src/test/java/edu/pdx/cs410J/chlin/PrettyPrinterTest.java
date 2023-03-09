package edu.pdx.cs410J.chlin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @Test
    void testPrettyNotNull() {
        Writer w = new StringWriter();
        PrettyPrinter pp = new PrettyPrinter(w);

        assertNotNull(pp);
    }

    @Test
    void testPrettyDump(@TempDir File tempDir) throws Exception {
       File textFile = new File(tempDir,"test.txt") ;
        PrintWriter writer = new PrintWriter(textFile);
        PrettyPrinter pp = new PrettyPrinter(writer);

        Airline airline = new Airline("Alaska Airline");
        Flight flight1 = new Flight(41, "pdx", "3/15/2023 10:36 PM", "SEA", "3/15/2023 11:39 PM");
        airline.addFlight(flight1);

        pp.dump(airline);

        String format = "%10s %30s %30s";
        String line8 = "------------";
        String line30 = "------------------------------";
        StringBuilder horizontalLine = new StringBuilder();
        horizontalLine.append(line8).append(line30).append(line30);
        String line0 = "Alaska Airline with 1 flights";
        String head = String.format(format, "Flight", "From", "To");
        String hori = horizontalLine.toString();
        String line1 = String.format(format, 41, "Portland, OR", "Seattle, WA");
        String line2 = String.format(format, "(63m)", "3/15/23 10:36 PM", "3/15/23 11:39 PM");

        BufferedReader br = new BufferedReader(new FileReader(textFile));
        String[] lines = new String[5];
        lines[0] = br.readLine();
        lines[1] = br.readLine();
        lines[2] = br.readLine();
        lines[3] = br.readLine();
        lines[4] = br.readLine();

        assertEquals(line0, lines[0]);
        assertEquals(head, lines[1]);
        assertEquals(hori, lines[2]);
        assertEquals(line1, lines[3]);
        assertEquals(line2, lines[4]);

    }

}
