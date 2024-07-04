package com.example.autism;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class ss extends AppCompatActivity {
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ss);

        String email2="";
        Intent intent = getIntent();
        if (intent.hasExtra("emailKey")) {
            // Retrieve the email value from the intent
            String email = intent.getStringExtra("emailKey");

            email2=email;

            // Log the actual email value (for debugging purposes)
            Log.d("EmailValue", "Email: " + email);
        }

        Button button=findViewById(R.id.button18);
        String finalEmail=email2;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ss.this,eyecontactskill.class);
                String email = finalEmail;

                // Pass email as a parameter to the next activity
                intent.putExtra("emailKey", email);
                startActivity(intent);
            }
        });
        videoView = findViewById(R.id.videoView);

        // Replace 'your_server_url' with the URL where your PHP script is hosted

        fetchData("sai");


    }

    private void fetchData(String username) {
        // Replace "http://192.168.156.100:80/login/prof.php" with your actual API endpoint
        String apiUrl = ip.ipn +"vdi1.php";

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



