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

import com.example.autism.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class parentlogin extends AppCompatActivity {

    private TextView emailVal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parentlogin);

        Button button = findViewById(R.id.button17);
        TextView text = findViewById(R.id.textView23);
        emailVal = findViewById(R.id.editTextTextEmailAddress);
        TextView passVal = findViewById(R.id.editTextTextPassword);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailVal.getText().toString();
                String pass = passVal.getText().toString();

                // Log or print the entered text
                Log.d("EnteredEmail", email);

                // Send the values to the PHP script
                new SendDataToServer().execute(email, pass);
            }
        });

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parentlogin.this, signup.class);
                startActivity(intent);
            }
        });
    }

    private class SendDataToServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String pass = params[1];

            try {
                // URL of your PHP script
                URL url = new URL(ip.ipn+"reg.php");

                // Open a connection
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");

                // Set the parameters to be sent to the server
                String postData = "email=" + URLEncoder.encode(email, "UTF-8") +
                        "&password=" + URLEncoder.encode(pass, "UTF-8");

                // Enable input/output streams
                connection.setDoOutput(true);
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
            // Check the response from the server
            if (result != null) {
                if (result.equals("Login successful")) {
                    // Login successful, move to the graph page
                    Intent intent = new Intent(parentlogin.this, graph.class);
                    String email = emailVal.getText().toString();

                    // Pass email as a parameter to the next activity
                    intent.putExtra("emailKey", email);
                    startActivity(intent);
                } else if (result.equals("Not registered")) {
                    // Not registered, go to the signup page
                    Intent intent = new Intent(parentlogin.this, signup.class);
                    startActivity(intent);
                } else if(result.equals("Incorrect password")){
                    Toast.makeText(parentlogin.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
