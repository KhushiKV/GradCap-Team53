package com.example.movesafe.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.movesafe.R;
import com.example.movesafe.utils.EnvironmentVariables;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    EditText nameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText confirmPasswordEditText;
    Button registerButton;
    TextView alreadyHaveAccountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // remove action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        nameEditText = findViewById(R.id.registerViewNameEditText);
        emailEditText = findViewById(R.id.registerViewEmailEditText);
        passwordEditText = findViewById(R.id.registerViewPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.registerViewConfirmPasswordEditText);
        registerButton = findViewById(R.id.registerViewRegisterButton);
        alreadyHaveAccountText = findViewById(R.id.registerViewAlreadyHaveAccountText);

        registerButton.setOnClickListener(this::handleClickRegisterButton);
        alreadyHaveAccountText.setOnClickListener(this::handleClickAlreadyHaveAccountButton);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");
        builder.setMessage("Do you want to exit the app?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            finishAffinity();
            System.exit(0);
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    void removeErrors() {
        nameEditText.setError(null);
        emailEditText.setError(null);
        passwordEditText.setError(null);
        confirmPasswordEditText.setError(null);
    }

    void handleClickRegisterButton(View v) {
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        // remove errors from all fields and validate input
        removeErrors();

        if (name.isEmpty()) {
            nameEditText.setError("Name is required!");
            nameEditText.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            emailEditText.setError("Email is required!");
            emailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please provide a valid email!");
            emailEditText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required!");
            passwordEditText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters long!");
            passwordEditText.requestFocus();
            return;
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError("Confirm password is required!");
            confirmPasswordEditText.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match!");
            confirmPasswordEditText.requestFocus();
            return;
        }

        // now, send the data to the url for registration by sending a post request using volley, with json body
        String url = EnvironmentVariables.BASE_URL + "/auth/register";
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", name);
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, response -> {
            Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        }, error -> {
            Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
            error.printStackTrace();
        }
        );

        queue.add(jsonObjectRequest);
    }

    void handleClickAlreadyHaveAccountButton(View v) {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }
}