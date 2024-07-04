package com.example.autism;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class graphdoctor extends AppCompatActivity {
    private static TextView textView15;
    private static TextView textView16;
    private static TextView textView17;
    private static TextView textView18;
    private static TextView textView7;
    private ProgressBar button;
    private ProgressBar button1;
    private ProgressBar button2;
    private ProgressBar button3;
    private ProgressBar button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphdoctor);

        String email2 = "";
        String finalEmail = "";
        Intent intent = getIntent();
        if (intent.hasExtra("emailKey")) {
            // Retrieve the email value from the intent
            String email = intent.getStringExtra("emailKey");
            email2 = email;
            finalEmail = email;

            // Log the actual email value (for debugging purposes)
            Log.d("EmailValue", "Email: " + email);
        }

        textView15 = findViewById(R.id.textView15);
        textView16 = findViewById(R.id.textView16);
        textView17 = findViewById(R.id.textView17);
        textView18 = findViewById(R.id.textView18);
        textView7 = findViewById(R.id.textView7);

        button = findViewById(R.id.progressBar2);
        button1 = findViewById(R.id.progressBar3);
        button2 = findViewById(R.id.progressBar4);
        button3 = findViewById(R.id.progressBar5);
        button4 = findViewById(R.id.progressBar);

        int a = getRandomValue();
        int b = getRandomValue();
        int c = getRandomValue();
        int d = getRandomValue();


        textView15.setText(String.valueOf(a));
        textView16.setText(String.valueOf(b));
        textView17.setText(String.valueOf(c));
        textView18.setText(String.valueOf(d));

        button.setProgress(a);
        button1.setProgress(b);
        button2.setProgress(c);
        button3.setProgress(d);
        button4.setProgress(d);


        sendDataToServer(finalEmail);

        String finalEmail1 = finalEmail;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = finalEmail1;

                // Pass email as a parameter to the next activity
                intent.putExtra("emailKey", email);
                startActivity(intent);
            }
        });

    }
    private void sendDataToServer(String finalEmail) {
        new graph.SendDataToServer(button, textView15).execute(finalEmail, "socialSkillsPercentage");
        new graph.SendDataToServer(button1, textView16).execute(finalEmail, "playSkillsPercentage");
        new graph.SendDataToServer(button2, textView17).execute(finalEmail, "selfHelpSkillsPercentage");
        new graph.SendDataToServer(button3, textView18).execute(finalEmail, "behavioralIssuesPercentage");
        new graph.SendDataToServer(button4, textView7).execute(finalEmail, "overallPercentage");
    }

    static class SendDataToServer extends AsyncTask<String, Void, Double> {
        private ProgressBar progressBar;
        private TextView textView;
        private String skillPercentageKey;

        // Constructor to receive the ProgressBar from the activity
        public SendDataToServer(ProgressBar progressBar, TextView textView) {
            this.progressBar = progressBar;
            this.textView = textView;
        }

        @Override
        protected Double doInBackground(String... params) {
            String email = params[0];
            skillPercentageKey = params[1];

            try {
                // Create a JSON object with the email value
                JSONObject json = new JSONObject();
                json.put("email", email);

                // Convert the JSON object to a string
                String jsonData = json.toString();

                // Replace "your_php_script_url" with the actual URL of your PHP script
                URL url = new URL(ip.ipn+"graph.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                // Set the request method to POST
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);

                // Write the data to the output stream
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(jsonData);
                out.flush();

                // Get the response from the server
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String response = convertInputStreamToString(in);

                // Parse the response JSON
                JSONObject responseObject = new JSONObject(response);
                String status = responseObject.optString("status");
                double percentage = responseObject.optDouble(skillPercentageKey, -1);

                // Close the connection
                urlConnection.disconnect();

                if ("success".equals(status) && percentage >= 0 && percentage <= 100) {
                    return percentage;
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return -1.0; // Return -1 in case of an error
        }

        @Override
        protected void onPostExecute(Double percentage) {
            super.onPostExecute(percentage);

            if (percentage >= 0) {
                // Update ProgressBar and TextView with the percentage value
                int progressValue = (int) Math.round(percentage);
                progressBar.setProgress(progressValue);
                textView.setText(String.valueOf(progressValue));
            } else {
                // Handle error or provide a default behavior
            }
        }

        // Helper method to convert InputStream to String
        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
            return stringBuilder.toString();
        }
    }

    private int getRandomValue() {
        Random random = new Random();
        return random.nextInt(41); // Adjust the range as needed
    }
}