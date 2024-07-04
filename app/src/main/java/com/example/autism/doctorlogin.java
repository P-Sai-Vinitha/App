package com.example.autism;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class doctorlogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctorlogin);

        Button button = findViewById(R.id.button17);
        TextView emailVal = findViewById(R.id.EmailAddress);
        TextView passVal = findViewById(R.id.Password);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailVal.getText().toString();
                String pass = passVal.getText().toString();

                // Send the values to the PHP script
                new SendDataToServer().execute(email, pass);
            }
        });
    }

    private class SendDataToServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String password = params[1];

            try {
                // URL of your PHP script
                URL url = new URL(ip.ipn+"doctorlogin.php");

                // Open a connection
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");

                // Set the content type to application/x-www-form-urlencoded
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                // Enable input/output streams
                connection.setDoOutput(true);

                // Set the parameters to be sent to the server
                String postData = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

                // Write the form data to the output stream
                OutputStream os = connection.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();

                // Get the response from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // Close the connection
                connection.disconnect();

                // Return the response from the server
                return response.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // Check the response from the server and display toasts accordingly
            if (result != null) {
                switch (result) {
                    case "Login successful":
                        // Move to the doctoroptions page
                        Intent intent = new Intent(doctorlogin.this, doctoroptions.class);
                        startActivity(intent);
                        break;
                    case "Incorrect password":
                        // Display toast for incorrect password
                        Toast.makeText(doctorlogin.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                        break;
                    case "Not registered":
                        // Display toast for not registered
                        Toast.makeText(doctorlogin.this, "Not registered", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        // Handle other responses or errors
                        break;
                }
            }
        }
    }
}
