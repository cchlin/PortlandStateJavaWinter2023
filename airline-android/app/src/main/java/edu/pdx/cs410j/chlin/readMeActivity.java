package edu.pdx.cs410j.chlin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class readMeActivity extends AppCompatActivity {

    private final String FILE_NAME = "README.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_me);

        String readme = readReadme();
        TextView textView = findViewById(R.id.help_row_text);
        textView.setText(readme);
    }

    private String readReadme() {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream inputream = getAssets().open(FILE_NAME);
            InputStreamReader inputStreamReader = new InputStreamReader(inputream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = reader.readLine()) != null ) {
               sb.append(line).append("\n");
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error while opening readme file", Toast.LENGTH_SHORT).show();
        }

        return sb.toString().trim();

    }

}