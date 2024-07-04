package com.example.autism;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class adminlogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);

        Button button = findViewById(R.id.button17);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Move these lines inside the onClick method
                TextView textView = findViewById(R.id.EmailAddress);
                TextView textView1 = findViewById(R.id.Password);

                String email = textView.getText().toString();
                String password = textView1.getText().toString();

                if (email.equals("sai@gmail.com") && password.equals("7777")) {
                    Intent intent = new Intent(adminlogin.this, videopage.class);
                    startActivity(intent);
                }
            }
        });
    }
}
