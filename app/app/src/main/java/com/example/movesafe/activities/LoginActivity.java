package com.example.movesafe.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.movesafe.R;
import com.example.movesafe.states.UserState;
import com.example.movesafe.utils.EnvironmentVariables;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText emailEditText;
    EditText passwordEditText;
    Button loginButton;
    TextView loginViewDontHaveAccountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        remove action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        emailEditText = findViewById(R.id.loginViewEmailEditText);
        passwordEditText = findViewById(R.id.loginViewPasswordEditText);
        loginButton = findViewById(R.id.loginViewLoginButton);
        loginButton.setOnClickListener(this::handleClickLoginButton);

        loginViewDontHaveAccountText = findViewById(R.id.loginViewDontHaveAccountText);
        loginViewDontHaveAccountText.setOnClickListener(this::handleClickDontHaveAccountButton);
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
        emailEditText.setError(null);
        passwordEditText.setError(null);
    }

    void handleClickLoginButton(View v) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // remove errors from all fields and validate input
        removeErrors();

        if (email.isEmpty()) {
            emailEditText.setError("Email is required!");
            emailEditText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required!");
            passwordEditText.requestFocus();
            return;
        }

        String url = EnvironmentVariables.BASE_URL + "auth/login";
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, response -> {
            try {
                JSONObject user = response.getJSONObject("user");
                user.put("token", response.getString("token"));
                UserState.getInstance().setUser(user);

                SharedPreferences sharedPreferences = getSharedPreferences("movesafe", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user", user.toString());
                editor.apply();

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
        });

        queue.add(jsonObjectRequest);
    }

    void handleClickDontHaveAccountButton(View v) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }
}