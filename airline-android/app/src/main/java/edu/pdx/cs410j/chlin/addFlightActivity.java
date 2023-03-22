package edu.pdx.cs410j.chlin;

import static edu.pdx.cs410j.chlin.SelectTimeActivity.DATE_TIME_VALUE;

import edu.pdx.cs410J.AirportNames;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class addFlightActivity extends AppCompatActivity {

    public static final int SELECT_DEPART_TIME = 42;
    public static final int SELECT_ARRIVE_TIME = 43;

    private String airlineName;
    private String flightNumber;
    private String src;
    private String depart;
    private String dest;
    private String arrive;

    static final String FLIGHT = "FLIGHT";
    static final String AIRLINE = "AIRLINE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flight);
    }

    public void setSelectDepartTime(View view) {
        startActivityForResult(new Intent(this, SelectTimeActivity.class), SELECT_DEPART_TIME);
    }

    public void setSelectArriveTime(View view) {
        startActivityForResult(new Intent(this, SelectTimeActivity.class), SELECT_ARRIVE_TIME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_DEPART_TIME) {
                if (data != null) {
                    String departTime = data.getStringExtra(DATE_TIME_VALUE);
                    EditText departTimeEditText = findViewById(R.id.add_flight_depart_row_input);
                    departTimeEditText.setText(departTime);
                }
            }
            if (requestCode == SELECT_ARRIVE_TIME) {
                String arriveTime = data.getStringExtra(DATE_TIME_VALUE);
                EditText arriveTimeEditText = findViewById(R.id.add_flight_arrive_row_input);
                arriveTimeEditText.setText(arriveTime);
            }
        }
    }

    public void addFlightButtonOnClick(View view) {
        EditText airlineEditText = findViewById(R.id.add_flight_airline_row_input);
        EditText flightNumberEditText = findViewById(R.id.add_flight_flightNum_row_input);
        EditText srcEditText = findViewById(R.id.add_flight_src_row_input);
        EditText departEditText = findViewById(R.id.add_flight_depart_row_input);
        EditText destEditText = findViewById(R.id.add_flight_dest_row_input);
        EditText arriveEditText = findViewById(R.id.add_flight_arrive_row_input);

        airlineName = airlineEditText.getText().toString();
        flightNumber = flightNumberEditText.getText().toString();
        src = srcEditText.getText().toString();
        depart = departEditText.getText().toString();
        dest = destEditText.getText().toString();
        arrive = arriveEditText.getText().toString();

        // check if input is valid
        if (!checkInputNotNull(airlineName, "Airline")) {
            return;
        }
        if (!checkInputNotNull(flightNumber, "Flight Number")) {
            return;
        }
        if (!checkInputNotNull(src, "Departure Airport Code")) {
            return;
        }
        if (!checkInputNotNull(depart, "Departure Time")) {
            return;
        }
        if (!checkInputNotNull(dest, "Destination Airport Code")) {
            return;
        }
        if (!checkInputNotNull(arrive, "Arrival Time")) {
            return;
        }
        if (!checkIfAirportCodeValid(src, "Departure")) {
            return;
        }
        if (!checkIfAirportCodeValid(dest, "Destination")) {
            return;
        }

        Flight flight = new Flight(Integer.parseInt(flightNumber), src, depart, dest, arrive);

        if (!flight.departureTimeIsEarlier()) {
            Toast.makeText(this, "Arrival time is earlier than departure time", Toast.LENGTH_LONG).show();
            return;
        }

        airlineEditText.setText("");
        flightNumberEditText.setText("");
        srcEditText.setText("");
        departEditText.setText("");
        destEditText.setText("");
        arriveEditText.setText("");

        Intent data = new Intent();
        data.putExtra(FLIGHT, flight);
        data.putExtra(AIRLINE, airlineName);
        setResult(RESULT_OK, data);

        finish();
    }

    private boolean checkInputNotNull(String input, String fieldName) {
        if (input == null || input.trim().isEmpty()) {
            Toast.makeText(this, "Empty " + fieldName, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean checkIfAirportCodeValid(String input, String srcOrDest) {
        if (input.length() <= 2) {
            Toast.makeText(this, srcOrDest + " Airport code too short: " + input, Toast.LENGTH_LONG).show();
            return false;
        }
        if (input.length() >= 4) {
            Toast.makeText(this, srcOrDest + " Airport code too long: " + input, Toast.LENGTH_LONG).show();
            return false;
        }
        if (input.matches(".*\\d+.")) {
            Toast.makeText(this, srcOrDest + " Airport code should not contain numbers: " + input, Toast.LENGTH_LONG).show();
            return false;
        }
        if (!input.matches("[a-zA-Z]+")) {
            Toast.makeText(this, srcOrDest + " Airport code should be only letters: " + input, Toast.LENGTH_LONG).show();
            return false;
        }
        if (AirportNames.getName(input) == null) {
            Toast.makeText(this, srcOrDest + " airport code does not exist: " + input, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}