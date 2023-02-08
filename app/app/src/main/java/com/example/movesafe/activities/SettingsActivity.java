package com.example.movesafe.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.movesafe.R;
import com.example.movesafe.states.UserDevicesState;
import com.example.movesafe.states.UserState;
import com.example.movesafe.utils.EnvironmentVariables;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    Spinner videoResolutionSpinner;
    Button deleteDeviceButton;
    EditText deviceNameEditText;
    Button saveDeviceNameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Settings");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        videoResolutionSpinner = findViewById(R.id.settingsViewVideoResolutionSpinner);
        String[] videoResolutionSpinnerItems = new String[]{"Auto", "1080p", "720p", "480p", "360p", "240p", "144p"};
        videoResolutionSpinner.setAdapter(new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, videoResolutionSpinnerItems));

        deleteDeviceButton = findViewById(R.id.settingsViewDeleteDeviceButton);
        deleteDeviceButton.setOnClickListener(this::handleClickDeleteDeviceButton);

        deviceNameEditText = findViewById(R.id.settingsViewDeviceNameEditText);

        saveDeviceNameButton = findViewById(R.id.settingsViewSaveDeviceNameButton);
        saveDeviceNameButton.setOnClickListener(this::handleClickSaveDeviceNameButton);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void handleClickDeleteDeviceButton(View view) {
        String deviceId = getIntent().getStringExtra("deviceId");
        String url = EnvironmentVariables.BASE_URL + "devices/remove/" + deviceId;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null, response -> {
            Toast.makeText(this, "Device deleted successfully!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }, error -> {
            Toast.makeText(this, "Error, please try again later!", Toast.LENGTH_SHORT).show();
            error.printStackTrace();
        }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = UserState.getInstance().getUser().optString("token");
                headers.put("Authorization", token);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }

    private void handleClickSaveDeviceNameButton(View view) {
        if (deviceNameEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter a device name!", Toast.LENGTH_SHORT).show();
            return;
        }

        String deviceId = getIntent().getStringExtra("deviceId");
        String url = EnvironmentVariables.BASE_URL + "devices/" + deviceId;
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", deviceNameEditText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, response -> {
            Toast.makeText(this, "Device name changed successfully!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }, error -> {
            Toast.makeText(this, "Error, please try again later!", Toast.LENGTH_SHORT).show();
            error.printStackTrace();
        }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = UserState.getInstance().getUser().optString("token");
                headers.put("Authorization", token);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }
}