package edu.pdx.cs410j.chlin;

import androidx.appcompat.app.AppCompatActivity;

import edu.pdx.cs410J.AirportNames;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.pdx.cs410J.ParserException;

public class searchAirline extends AppCompatActivity {

    private ArrayAdapter<String> flights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_airline);
    }

    private File getAirlineFile(String airlineName) {
       File dataDir = this.getDataDir();
       File airlineFile = new File(dataDir, airlineName + ".txt");

       if (!airlineFile.exists()) {
           return null;
       }
       return airlineFile;
    }

    public void searchAirline(View view) {
        EditText airlineEditText = findViewById(R.id.search_airline_row_input);
        String airlineName = airlineEditText.getText().toString();
        if (airlineName.trim().isEmpty()) {
            Toast.makeText(this, "Empty airline name", Toast.LENGTH_LONG).show();
            return;
        }
        File airlineFile = getAirlineFile(airlineName.toLowerCase());
        ListView listOfFlights = findViewById(R.id.airline_search_result);
        List<String> flightList = new ArrayList<>();
        if (airlineFile == null || !airlineFile.exists()) {
            flightList.add("No airline found");
            flights = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, flightList);
        } else {
            try {
                Reader reader = new FileReader(airlineFile);
                TextParser parser = new TextParser(reader);
                Airline airline = parser.parse();
                prettyPrintFlights(flightList, airline);
            } catch (IOException e) {
                Toast.makeText(this, "Problem occurred while reading from file: " + e, Toast.LENGTH_LONG).show();
                return;
            } catch (ParserException e) {
                Toast.makeText(this, "Problem occurred while parsing from file" + e, Toast.LENGTH_LONG).show();
                return;
            }
            flights = new ArrayAdapter<>(this, R.layout.list_item_layout, flightList);
        }
        listOfFlights.setAdapter(flights);

    }

    private void prettyPrintFlights(List<String> flightList, Airline airline) {
        Collection<Flight> flights = airline.getFlights();
        String format = "%10s %30s %30s";
        String head = String.format(format, "Flight", "From", "To");
        flightList.add(head);

        if (!flights.isEmpty()) {
           for (Flight flight : flights)  {
               int flightNumber = flight.getNumber();
               String source = AirportNames.getName(flight.getSource());
               String departTime = flight.getDepartureString();
               String dest = AirportNames.getName(flight.getDestination());
               String arrivalTime = flight.getArrivalString();

               long duration = flight.getArrival().getTime() - flight.getDeparture().getTime();
               String minute = String.valueOf(duration / (60 * 1000));
               StringBuilder travelTime = new StringBuilder();
               travelTime.append("(").append(minute).append("m)");

               String flightFormatted = String.format(format, flightNumber, source, dest);
               String flightTimeFormatted = String.format(format, travelTime.toString().trim(), departTime, arrivalTime);
               flightList.add((flightFormatted + System.lineSeparator() + flightTimeFormatted).trim());
           }
        } else {
            flightList.add("No flights!");
        }
    }
}