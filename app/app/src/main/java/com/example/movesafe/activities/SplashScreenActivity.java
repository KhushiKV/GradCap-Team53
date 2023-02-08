package com.example.movesafe.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.movesafe.R;
import com.example.movesafe.states.UserState;
import com.example.movesafe.utils.EnvironmentVariables;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        handleNetworkState();

        handleUserState();
    }

    void handleNetworkState() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isConnected = cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();

        if (!isConnected) {
            new AlertDialog.Builder(this)
                    .setTitle("No Internet Connection")
                    .setMessage("Please connect to the internet and try again.")
                    .setPositiveButton("OK", (dialog, which) -> finish())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    void handleUserState() {
        SharedPreferences sharedPreferences = getSharedPreferences("movesafe", MODE_PRIVATE);
        if (sharedPreferences.contains("user")) {
            JSONObject user = null;
            try {
                user = new JSONObject(sharedPreferences.getString("user", ""));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (user != null && user.length() > 0) {
                // verify user, and if they are still logged in, redirect them to dashboard
                String url = EnvironmentVariables.BASE_URL + "/user";
                RequestQueue queue = Volley.newRequestQueue(this);

                JSONObject finalUser = user;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
                    UserState.getInstance().setUser(finalUser);
                    navigateTo(MainActivity.class);
                }, error -> {
                    error.printStackTrace();
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<>();
                        String token = null;
                        try {
                            token = finalUser.getString("token");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        params.put("Authorization", token);
                        return params;
                    }
                };
                queue.add(jsonObjectRequest);
            } else {
                navigateTo(LoginActivity.class);
            }
        } else {
            navigateTo(LoginActivity.class);
        }
    }


    void navigateTo(Class c) {
        startActivity(new Intent(this, c));
        finish();
    }
}