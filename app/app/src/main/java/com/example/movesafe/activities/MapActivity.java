package com.example.movesafe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.movesafe.R;
import com.example.movesafe.states.UserDevicesState;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    ArrayList<String> location = new ArrayList<>();
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Live location");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        JSONObject device = UserDevicesState.getInstance().getDevice(getIntent().getStringExtra("deviceId"));
        if (device != null) {
            try {
                location.clear();
//                location.add(device.getJSONArray("location").get(0).toString());
//                location.add(device.getJSONArray("location").get(1).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragmentContainerView);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Toast.makeText(this, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (location.isEmpty()) {
            location.add("26");
            location.add("80");
        }

        LatLng latLng = new LatLng(Double.parseDouble(location.get(0)), Double.parseDouble(location.get(1)));

        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 1));
    }
}