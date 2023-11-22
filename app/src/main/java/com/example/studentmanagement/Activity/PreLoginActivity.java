package com.example.studentmanagement.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.studentmanagement.R;

public class PreLoginActivity extends AppCompatActivity {

    private Button btGetStarted;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_login);

        btGetStarted = findViewById(R.id.btGetStared);

        btGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(PreLoginActivity.this, LoginActivity.class);
                startActivity(myIntent);
            }
        });
    }
}