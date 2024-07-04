package com.example.autism;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.autism.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class details extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 1;

    private ImageView imageView;
    private FloatingActionButton button4;
    private Uri selectedImageUri;
    private String emailcopy ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        String email = "";
        if (intent.hasExtra("emailKey")) {
            // Retrieve the email value from the intent
            email = intent.getStringExtra("emailKey");
            emailcopy=email;
        }


        Button button = findViewById(R.id.button17);
        TextView parentnameVal = findViewById(R.id.parentname);
        TextView parentageVal = findViewById(R.id.parentage);
        TextView dateVal = findViewById(R.id.date);
        TextView childnameVal = findViewById(R.id.childname);
        TextView childageVal = findViewById(R.id.childage);
        TextView childdiagnosisVal = findViewById(R.id.childdiagnosis);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(getColor(R.color.black)));
        }

        imageView = findViewById(R.id.imageView16);
        button4 = findViewById(R.id.floatingActionButton);

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });


        String finalEmail = email;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String parentname = parentnameVal.getText().toString();
                String parentage = parentageVal.getText().toString();
                String date = dateVal.getText().toString();
                String childname = childnameVal.getText().toString();
                String childage = childageVal.getText().toString();
                String childdiagnosis = childdiagnosisVal.getText().toString();

                // Get the image URI as a string
                String imageUriString = "";
                if (imageView.getDrawable() != null && selectedImageUri != null) {
                    imageUriString = selectedImageUri.toString();
                }


                String base64Image = "";
                if (imageView.getDrawable() != null && selectedImageUri != null) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    // Convert the bitmap to JPEG format
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                    // Now, encode the JPEG image as Base64
                    byte[] imageBytes = baos.toByteArray();
                    base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                }

                // Execute AsyncTask to send data to the server
                new SendDataToServer().execute(parentname, parentage, date, childname, childage, childdiagnosis, imageUriString, finalEmail,base64Image);
            }
        });
    }



    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);
        }
    }

    private class SendDataToServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                String parentname = params[0];
                String parentage = params[1];
                String date = params[2];
                String childname = params[3];
                String childage = params[4];
                String childdiagnosis = params[5];
                String imageUriString = params[6];
                String email = params[7];
                String base64Image=params[8];

                // Create a JSON object
                JSONObject jsonParams = new JSONObject();
                jsonParams.put("parentname", parentname);
                jsonParams.put("parentage", parentage);
                jsonParams.put("date", date);
                jsonParams.put("childname", childname);
                jsonParams.put("childage", childage);
                jsonParams.put("childdiagnosis", childdiagnosis);
                jsonParams.put("email", email);
                jsonParams.put("profileimage", imageUriString);
                jsonParams.put("base64image", base64Image);

                // Convert JSON object to string
                String jsonData = jsonParams.toString();

                URL url = new URL(ip.ipn+"details.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                urlConnection.setDoOutput(true);

                // Write JSON data to the server
                try (OutputStream os = urlConnection.getOutputStream()) {
                    byte[] input = jsonData.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Get the response from the server
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                bufferedReader.close();
                inputStream.close();

                // Return the server response
                return stringBuilder.toString();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (result != null && result.equals("Data inserted successfully")) {
                Intent intent = new Intent(details.this, parentlogin.class);
                String email = emailcopy;
                intent.putExtra("emailKey", email);
                startActivity(intent);

            }
        }
    }
}
