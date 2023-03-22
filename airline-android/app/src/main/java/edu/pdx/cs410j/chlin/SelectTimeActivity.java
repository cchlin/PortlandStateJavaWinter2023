package edu.pdx.cs410j.chlin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TimePicker;

public class SelectTimeActivity extends AppCompatActivity {

    private int selectedYear;
    private int selectedMonth;
    private int selectedDay;
    private int selectedHour;
    private int selectedMinute;
    private String formattedDayTimeString;
    static final String DATE_TIME_VALUE = "DATE_TIME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);

        CalendarView calendarView = findViewById(R.id.select_time_date);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedYear = year;
            selectedMonth = month + 1;
            selectedDay = dayOfMonth;
        });

        TimePicker timePicker = findViewById(R.id.time_picker);
        timePicker.setIs24HourView(false);
        timePicker.setOnTimeChangedListener((view, hour, minute) -> {
            selectedHour = hour;
            selectedMinute = minute;
        });
    }

    public void confirmTime(View view) {
        formatDateTime();
        Intent data = new Intent();
        data.putExtra(DATE_TIME_VALUE, this.formattedDayTimeString);
        setResult(RESULT_OK, data);
        finish();
    }

    private void formatDateTime() {
        String amPm = "";
        if (selectedHour >= 12) {
            amPm = "PM";
            if (selectedHour > 12) {
                selectedHour = selectedHour - 12;
            }
        } else {
            amPm = "AM";
            if (selectedHour == 0) {
                selectedHour = 12;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(selectedMonth).append("/")
                .append(selectedDay).append("/")
                .append(selectedYear).append(" ")
                .append(selectedHour).append(":")
                .append(selectedMinute).append(" ")
                .append(amPm);

        formattedDayTimeString = sb.toString().trim();
    }
}