package com.example.myloginapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myloginapp.api.ApiResponse;
import com.example.myloginapp.api.RetrofitClient;
import com.example.myloginapp.database.HashUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    // UI Elements
    private TextInputEditText etName;
    private TextInputEditText etEmail;
    private TextInputEditText etDob;
    private TextInputEditText etPassword;
    private TextInputEditText etConfirmPassword;

    private Button btnSignUp;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // LINK UI ELEMENTS
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etDob = findViewById(R.id.etDob);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnSignUp = findViewById(R.id.btnSignUp);
        tvLogin = findViewById(R.id.tvLogin);

        // DATE PICKER
        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        // SIGNUP BUTTON
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignUp();
            }
        });

        // GO BACK TO LOGIN
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // DATE PICKER METHOD
    private void showDatePicker() {

        final Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                SignupActivity.this,

                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view,
                                          int selectedYear,
                                          int selectedMonth,
                                          int selectedDay) {

                        String selectedDate =
                                String.format(
                                        "%02d/%02d/%04d",
                                        selectedDay,
                                        selectedMonth + 1,
                                        selectedYear
                                );

                        etDob.setText(selectedDate);
                    }
                },

                year,
                month,
                day
        );

        // PREVENT FUTURE DATES
        datePickerDialog.getDatePicker()
                .setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();
    }

    // SIGNUP METHOD
    private void handleSignUp() {

        // GET VALUES
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword =
                etConfirmPassword.getText().toString().trim();

        // VALIDATION

        if (TextUtils.isEmpty(name)) {

            etName.setError("Full name is required");
            etName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {

            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()) {

            etEmail.setError("Enter a valid email");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(dob)) {

            etDob.setError("Date of birth is required");
            etDob.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {

            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {

            etPassword.setError(
                    "Password must be at least 6 characters"
            );

            etPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {

            etConfirmPassword.setError(
                    "Confirm your password"
            );

            etConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {

            etConfirmPassword.setError(
                    "Passwords do not match"
            );

            etConfirmPassword.requestFocus();
            return;
        }

        // HASH PASSWORD
        String hashedPassword =
                HashUtils.hashPassword(password);

        // SEND DATA TO PHP BACKEND
        RetrofitClient.getApiService()
                .signup(
                        "signup",
                        name,
                        email,
                        dob,
                        hashedPassword
                )

                .enqueue(new Callback<ApiResponse>() {

                    @Override
                    public void onResponse(
                            Call<ApiResponse> call,
                            Response<ApiResponse> response
                    ) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            ApiResponse apiResponse =
                                    response.body();

                            if ("success".equals(
                                    apiResponse.getStatus()
                            )) {

                                Toast.makeText(
                                        SignupActivity.this,
                                        "Account created successfully!",
                                        Toast.LENGTH_LONG
                                ).show();

                                finish();

                            } else {

                                Toast.makeText(
                                        SignupActivity.this,
                                        apiResponse.getMessage(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }

                        } else {

                            Toast.makeText(
                                    SignupActivity.this,
                                    "Server Error",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<ApiResponse> call,
                            Throwable t
                    ) {

                        Toast.makeText(
                                SignupActivity.this,
                                "Network Error: " + t.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}