package com.example.movesafe.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.movesafe.R;
import com.example.movesafe.states.UserDevicesState;
import com.example.movesafe.states.UserState;

import org.json.JSONObject;

import java.util.ArrayList;

public class DeviceActivity extends AppCompatActivity {
    String deviceId = "";

    CardView liveLocationCardView;
    CardView redeemCardView;
    CardView sosContactsCardView;
    CardView historyCardView;
    CardView settingsCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        deviceId = getIntent().getStringExtra("deviceId");
        JSONObject device = UserDevicesState.getInstance().getDevice(deviceId);
        if (device != null) {
            setTitle(device.optString("name", "placeholder_device_name"));
        } else {
            setTitle("placeholder_device_name");
        }

        liveLocationCardView = findViewById(R.id.deviceActivityLiveLocationCardView);
        liveLocationCardView.setOnClickListener(this::handleClickLiveLocationCardView);

        redeemCardView = findViewById(R.id.deviceActivityRedeemCardView);
        redeemCardView.setOnClickListener(this::handleClickRedeemCardView);

        sosContactsCardView = findViewById(R.id.deviceActivitySOSContactsCardView);
        sosContactsCardView.setOnClickListener(this::handleClickSosContactsCardView);

        historyCardView = findViewById(R.id.deviceActivityHistoryCardView);
        historyCardView.setOnClickListener(this::handleClickHistoryCardView);

        settingsCardView = findViewById(R.id.deviceActivitySettingsCardView);
        settingsCardView.setOnClickListener(this::handleClickSettingsCardView);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void handleClickLiveLocationCardView(View view) {
        if (deviceId.length() == 0) {
            Toast.makeText(this, "Device not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        startActivity(new Intent(this, MapActivity.class).putExtra("deviceId", deviceId));
    }

    private void handleClickRedeemCardView(View view) {
        startActivity(new Intent(this, RedeemActivity.class));
    }

    private void handleClickSosContactsCardView(View view) {
        startActivity(new Intent(this, SOSContactsActivity.class));
    }

    private void handleClickHistoryCardView(View view) {
        if (deviceId.length() == 0) {
            Toast.makeText(this, "Device not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        startActivity(new Intent(this, DeviceHistoryActivity.class).putExtra("deviceId", deviceId));
    }

    private void handleClickSettingsCardView(View view) {
        if (deviceId.length() == 0) {
            Toast.makeText(this, "Device not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        startActivity(new Intent(this, SettingsActivity.class).putExtra("deviceId", deviceId));
    }
}