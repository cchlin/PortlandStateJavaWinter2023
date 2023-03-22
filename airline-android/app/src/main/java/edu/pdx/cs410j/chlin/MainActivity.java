package edu.pdx.cs410j.chlin;

import static edu.pdx.cs410j.chlin.addFlightActivity.AIRLINE;
import static edu.pdx.cs410j.chlin.addFlightActivity.FLIGHT;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    static final int ADD_FLIGHT_VALUE = 41;

    private final Map<String, Airline> airlines = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchAddFlight(View view) {
        startActivityForResult(new Intent(this, addFlightActivity.class), ADD_FLIGHT_VALUE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_FLIGHT_VALUE) {
                if (data != null) {
                    Flight flight = data.getSerializableExtra(FLIGHT, Flight.class);
                    String airlineName = data.getStringExtra(AIRLINE);
                    Airline airline = this.airlines.get(airlineName);
                    if (airline == null) {
                        airline = new Airline(airlineName);
                        this.airlines.put(airlineName, airline);
                    }
                    airline.addFlight(flight);
                    try {
                        writeAirlineToStorage(airlineName, airline);
                    } catch (IOException e) {
                        Toast.makeText(this, "Problem occurred while writing to file: " + e, Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    private void writeAirlineToStorage(String airlineName, Airline airline) throws IOException {
        File airlineFile = getAirlineFile(airlineName.toLowerCase());

        Writer writer = new FileWriter(airlineFile);

        try (TextDumper dumper = new TextDumper(writer)) {
            dumper.dump(airline);
        }
    }

    private File getAirlineFile(String airlineName) {
        File dataDir = this.getDataDir();
        return new File(dataDir, airlineName + ".txt");
    }

    public void searchAirlineOnClick(View view) {
        startActivity(new Intent(this, searchAirline.class));
    }

    public void searchFlightsOnClick(View view) {
        startActivity(new Intent(this, searchFlightsActivity.class));
    }

    public void launchReadMe(View view) {
        startActivity(new Intent(this, readMeActivity.class));
    }
}