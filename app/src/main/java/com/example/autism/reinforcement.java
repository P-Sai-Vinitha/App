package com.example.autism;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class reinforcement extends AppCompatActivity {
    private VideoView videoView;
    private TextView textView19;
    private SeekBar seekbar3;

    private TextView textView21;
    private SeekBar seekbar;

    private TextView textViewProficiencyValue;
    private SeekBar seekbar1;

    private TextView textView5;
    private SeekBar lastSeekBar;
    int v1 = 0;
    int v2 = 0;
    int v3 = 0;
    int v4 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reinforcement);

        Intent intent = getIntent();
        String email = "";
        if (intent.hasExtra("emailKey")) {
            // Retrieve the email value from the intent
            email = intent.getStringExtra("emailKey");
        }

        textView19 = findViewById(R.id.textView19);
        seekbar3 = findViewById(R.id.seekBar3);

        seekbar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textView19.setText("" + i);
                v1 = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        textView21 = findViewById(R.id.textView21);
        seekbar = findViewById(R.id.seekBar);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textView21.setText("" + i);
                v2 = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        textViewProficiencyValue = findViewById(R.id.textViewProficiencyValue);
        seekbar1 = findViewById(R.id.seekBar1);

        seekbar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textViewProficiencyValue.setText("" + i);
                v3 = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        textView5 = findViewById(R.id.textView5);
        lastSeekBar = findViewById(R.id.lastSeekBar);

        lastSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textView5.setText("" + i);
                v4 = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        Button button=findViewById(R.id.button18);
        String finalEmail = email;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendDataToServer().execute(v1, v2, v3, v4, finalEmail);
                Intent intent=new Intent(reinforcement.this,graph.class);
                String email = finalEmail;

                // Pass email as a parameter to the next activity
                intent.putExtra("emailKey", email);
                startActivity(intent);
            }
        });
        videoView = findViewById(R.id.videoView);
        fetchData("sai");
    }
    static class SendDataToServer extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            int v1 = (int) params[0];
            int v2 = (int) params[1];
            int v3 = (int) params[2];
            int v4 = (int) params[3];
            String email = (String) params[4];

            try {
                // Create a JSON object with v1, v2, v3, v4, and email values
                JSONObject json = new JSONObject();
                json.put("v1", v1);
                json.put("v2", v2);
                json.put("v3", v3);
                json.put("v4", v4);
                json.put("email", email);

                // Convert the JSON object to a string
                String jsonData = json.toString();

                // Replace "your_php_script_url" with the actual URL of your PHP script
                URL url = new URL(ip.ipn+"reinforcement.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                // Set the request method to POST
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);

                // Write the data to the output stream
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(jsonData);
                out.flush();

                // Get the response from the server (if needed)
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                // Read and handle the server response if necessary

                // Close the connection
                urlConnection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
    private void fetchData(String username) {
        // Replace "http://192.168.156.100:80/login/prof.php" with your actual API endpoint
        String apiUrl = ip.ipn +"vdv8.php";

        // Append the username as a parameter to the URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("hhhh");
                handleError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Send the username as a POST parameter
                Map<String, String> data = new HashMap<>();
                data.put("username", username);

                // Log the parameters for debugging
                Log.d("Volley Request", "Params: " + data.toString());

                return data;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);

    }


    private void handleResponse(String response) {
        Log.d("JSON Response", response);
        System.out.println(response);
        String[] values = response.split(":");
        // Handle your JSON response here without assuming a 'status' field
        // You can parse the response and handle success/failure accordingly
        System.out.println(values[1]);
        // Extract the values from the response string
        System.out.println(ip.ipn+"videos/"+values[1].substring(9, values[1].length()-2));
        playVideo(ip.ipn+"videos/"+values[1].substring(9, values[1].length()-2));



        // Display the values in the corresponding TextViews

    }

    private void handleError(VolleyError error) {
        System.out.println("boooooo");
    }

    private void playVideo(String videoPath) {

        CustomMediaController mediaController = new CustomMediaController(this);
        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
        videoView.setVideoPath(videoPath);
        videoView.requestFocus();
        videoView.start();
    }
}
