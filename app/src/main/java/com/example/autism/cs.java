package com.example.autism;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class cs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cs);

        String email2 = "";
        String finalEmail = "";
        Intent intent = getIntent();
        if (intent.hasExtra("emailKey")) {
            // Retrieve the email value from the intent
            String email = intent.getStringExtra("emailKey");

            finalEmail = email;

            // Log the actual email value (for debugging purposes)
            Log.d("EmailValue", "Email: " + email);
        }

        Button button=findViewById(R.id.button7);



        String finalEmail1 = finalEmail;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(cs.this,playskills.class);
                String email = finalEmail1;
                    Log.d("cs ",email);
                // Pass email as a parameter to the next activity
                intent.putExtra("emailKey", email);
                startActivity(intent);
            }
        });
    }
}