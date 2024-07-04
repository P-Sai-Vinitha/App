package com.example.autism;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class playskills extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playskills);

        String email2="";
        Intent intent = getIntent();
        if (intent.hasExtra("emailKey")) {
            // Retrieve the email value from the intent
            String email = intent.getStringExtra("emailKey");

            email2=email;

            // Log the actual email value (for debugging purposes)
            Log.d("EmailValue", "Email: " + email);
        }

        Button button=findViewById(R.id.button10);
        Button button1=findViewById(R.id.button11);
        Button button2=findViewById(R.id.button12);
        Button button3=findViewById(R.id.button13);
        Button button4=findViewById(R.id.button14);
        String finalEmail=email2;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(playskills.this,ps.class);
                String email = finalEmail;

                // Pass email as a parameter to the next activity
                intent.putExtra("emailKey", email);
                startActivity(intent);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(playskills.this,playingtogether.class);
                String email = finalEmail;

                // Pass email as a parameter to the next activity
                intent.putExtra("emailKey", email);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(playskills.this,newplayskills.class);
                String email = finalEmail;

                // Pass email as a parameter to the next activity
                intent.putExtra("emailKey", email);
                startActivity(intent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(playskills.this,practiseplay.class);
                String email = finalEmail;

                // Pass email as a parameter to the next activity
                intent.putExtra("emailKey", email);
                startActivity(intent);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(playskills.this,reinforcement.class);
                String email = finalEmail;

                // Pass email as a parameter to the next activity
                intent.putExtra("emailKey", email);
                startActivity(intent);
            }
        });
    }
}