package com.example.autism;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class parentdetails extends AppCompatActivity {
    private TextView parentname, parentage, registereddate, childname, childage, childdiagnosis;
    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parentdetails);

        String username = getIntent().getStringExtra("username");
        System.out.println("111  " + username);

        parentname = findViewById(R.id.parentNameText);
        parentage = findViewById(R.id.parentageText);
        registereddate = findViewById(R.id.registeredonText);
        childname = findViewById(R.id.childnameText);
        childage = findViewById(R.id.childageText);
        childdiagnosis = findViewById(R.id.childdiagnosisText);
        profileImage = findViewById(R.id.imageView15);

        fetchData(username);
    }

    private void fetchData(String username) {
        // Replace "http://192.168.156.100:80/login/prof.php" with your actual API endpoint
        String apiUrl = ip.ipn + "parentdetails.php";

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
                handleError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Send the username as a POST parameter
                Map<String, String> data = new HashMap<>();
                data.put("id", username);

                // Log the parameters for debugging
                Log.d("Volley Request", "Params: " + data.toString());

                return data;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void handleResponse(String response) {
        Log.d("JSON Response", response);

        try {
            // Parse the JSON response into a JSONObject
            JSONObject jsonResponse = new JSONObject(response);

            // Display the values in the corresponding TextViews
            parentname.setText(jsonResponse.getString("parentname"));
            parentage.setText(jsonResponse.getString("parentage"));
            registereddate.setText(jsonResponse.getString("registereddate"));
            childname.setText(jsonResponse.getString("childname"));
            childage.setText(jsonResponse.getString("childage"));
            childdiagnosis.setText(jsonResponse.getString("childdiagnosis"));

            // Decode the base64 profile image and set it in the ImageView
            String base64Image = jsonResponse.getString("profileimage");
            if (base64Image != null && !base64Image.isEmpty()) {
                byte[] decodedImageBytes = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedImageBytes, 0, decodedImageBytes.length);
                profileImage.setImageBitmap(decodedBitmap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleError(VolleyError error) {
        System.out.println("boooooo");
    }
}
