package com.example.autism;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.autism.R;
import com.example.autism.details;
import com.example.autism.parentlogin;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        Intent intent = getIntent();
        if (intent.hasExtra("emailKey")) {
            String email = intent.getStringExtra("emailKey");


            Log.d("EmailValue", "Email: " + email);
        }

        Button button = findViewById(R.id.button17);
        TextView text = findViewById(R.id.textView23);
        TextView nameVal = findViewById(R.id.fullname);
        TextView emailVal = findViewById(R.id.email);
        TextView passwordVal = findViewById(R.id.Password);
        TextView confirmPasswordVal = findViewById(R.id.confrimPassword);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameVal.getText().toString();
                String email = emailVal.getText().toString();
                String password = passwordVal.getText().toString();
                String confirmPassword = confirmPasswordVal.getText().toString();

                Log.d("EnteredName", name);
                Log.d("EnteredEmail", email);
                String finalEmail = email;
                new SendDataToServer(finalEmail).execute(name, email, password, confirmPassword);
            }
        });

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signup.this, parentlogin.class);
                startActivity(intent);
            }
        });
    }

    private class SendDataToServer extends AsyncTask<String, Void, Boolean> {
        private final String finalEmail;

        public SendDataToServer(String finalEmail) {
            this.finalEmail = finalEmail;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String name = params[0];
            String email = params[1];
            String password = params[2];
            String confirmPassword = params[3];

            try {
                URL url = new URL(ip.ipn+"signup.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setDoOutput(true);

                String postData = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&" +
                        URLEncoder.encode("confirmPassword", "UTF-8") + "=" + URLEncoder.encode(confirmPassword, "UTF-8");

                OutputStream os = connection.getOutputStream();
                OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
                writer.write(postData);
                writer.flush();
                writer.close();
                os.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return true;
                }

                connection.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            if (isSuccess) {
                Intent intent = new Intent(signup.this, details.class);
                String email = finalEmail;
                intent.putExtra("emailKey", email);
                startActivity(intent);
                finish();
            } else {
                // Handle the case where the insertion failed
                // You might want to show an error message or take other actions
            }
        }
    }
}
