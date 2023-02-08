package com.example.movesafe.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.movesafe.R;
import com.example.movesafe.adapters.DeviceHistoryListViewAdapter;
import com.example.movesafe.states.UserDevicesState;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DeviceHistoryActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_history);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Reward history");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        JSONObject device = UserDevicesState.getInstance().getDevice(getIntent().getStringExtra("deviceId"));
        ArrayList<String> deviceHistory = new ArrayList<>();
        deviceHistory.add("overspeeding -10");
        deviceHistory.add("cruise +20");
        deviceHistory.add("rash-driving -10");

        listView = findViewById(R.id.deviceActivityHistoryCardView);
        listView.setAdapter(new DeviceHistoryListViewAdapter(this, deviceHistory));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}