package com.example.movesafe.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.movesafe.R;
import com.example.movesafe.states.UserState;
import com.example.movesafe.utils.EnvironmentVariables;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddDeviceActivity extends AppCompatActivity {
    EditText deviceIdEditText;
    Button addDeviceButton;
    ImageView qrCodeImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add Device");
        }

        deviceIdEditText = findViewById(R.id.deviceIdEditText);

        addDeviceButton = findViewById(R.id.addDeviceViewAddDeviceButton);
        addDeviceButton.setOnClickListener(this::handleClickAddDeviceButton);

        qrCodeImageView = findViewById(R.id.addDeviceViewQRCodeImageView);
        qrCodeImageView.setOnClickListener(this::handleClickQrCodeImageView);
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
        return true;
    }

    void addDevice(String deviceId) {
        String url = EnvironmentVariables.BASE_URL + "devices/add/" + deviceId;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                null,
                response -> {
                    Toast.makeText(this, "Device registered successfully!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, MainActivity.class));
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Device registration failed!", Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                String token = UserState.getInstance().getUser().optString("token");
                params.put("Authorization", token);
                return params;
            }
        };

        queue.add(jsonObjectRequest);
    }

    void handleClickAddDeviceButton(View v) {
        String deviceId = deviceIdEditText.getText().toString();
        if (deviceId.isEmpty()) {
            deviceIdEditText.setError("Device ID is required");
            return;
        }

        addDevice(deviceId);
    }

    void handleClickQrCodeImageView(View v) {
        ScanOptions options = new ScanOptions();
        options.setOrientationLocked(true);
        options.setCaptureActivity(QRScannerActivity.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            String deviceId = result.getContents();
            addDevice(deviceId);
        }
    });
}