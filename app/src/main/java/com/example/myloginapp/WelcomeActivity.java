package com.example.myloginapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    private TextView tvWelcomeName, tvWelcomeEmail;
    private Button btnLogout;

    private static final String PREFS_NAME = "UserPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Link UI elements
        tvWelcomeName  = findViewById(R.id.tvWelcomeName);
        tvWelcomeEmail = findViewById(R.id.tvWelcomeEmail);
        btnLogout      = findViewById(R.id.btnLogout);

        // Get data passed from LoginActivity
        String userName  = getIntent().getStringExtra("userName");
        String userEmail = getIntent().getStringExtra("userEmail");

        // Display personalized welcome message
        if (userName != null && !userName.isEmpty()) {
            tvWelcomeName.setText("Hello, " + userName + "! 👋");
        } else {
            tvWelcomeName.setText("You are now logged in.");
        }

        if (userEmail != null && !userEmail.isEmpty()) {
            tvWelcomeEmail.setText(userEmail);
        }

        // LOGOUT BUTTON
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout() {
        // Navigate back to login screen
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // Prevent going back to login with back button after login
    @Override
    public void onBackPressed() {
        // Do nothing — user must use logout button
    }
}
