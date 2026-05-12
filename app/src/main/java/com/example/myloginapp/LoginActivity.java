package com.example.myloginapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myloginapp.api.ApiResponse;
import com.example.myloginapp.api.RetrofitClient;
import com.example.myloginapp.database.HashUtils;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    // UI Elements
    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Link UI elements to variables
        etEmail    = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin   = findViewById(R.id.btnLogin);
        tvSignUp   = findViewById(R.id.tvSignUp);

        // LOGIN BUTTON CLICK
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        // SIGN UP LINK CLICK
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to SignupActivity
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void handleLogin() {
        // Get text from fields
        String email    = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // --- VALIDATION ---
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        // --- CHECK AGAINST XAMPP (MySQL Backend) ---
        String hashedPassword = HashUtils.hashPassword(password);

        RetrofitClient.getApiService().login("login", email, hashedPassword)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ApiResponse res = response.body();
                            if ("success".equals(res.getStatus())) {
                                Toast.makeText(LoginActivity.this, "Login Successful via XAMPP!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                                intent.putExtra("userName", res.getName());
                                intent.putExtra("userEmail", res.getEmail());
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, res.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
