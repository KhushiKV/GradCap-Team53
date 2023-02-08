package com.example.movesafe.fragments.dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.movesafe.R;
import com.example.movesafe.activities.DeviceActivity;
import com.example.movesafe.adapters.DashboardListViewAdapter;
import com.example.movesafe.states.UserDevicesState;
import com.example.movesafe.states.UserState;
import com.example.movesafe.utils.EnvironmentVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivityDashboardFragment extends Fragment {

    public MainActivityDashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDevices();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_activity_dashboard, container, false);
    }

    ListView devicesListView;
    TextView titleTextView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        devicesListView = requireView().findViewById(R.id.dashboardViewDevicesListView);
        devicesListView.setAdapter(new DashboardListViewAdapter(requireActivity(), UserDevicesState.getInstance().getDevices()));
        devicesListView.setOnItemClickListener((parent, v, position, id) -> {
            try {
                String deviceId = UserDevicesState.getInstance().getDevices().get(position).getString("id");
                startActivity(new Intent(requireActivity(), DeviceActivity.class).putExtra("deviceId", deviceId));
            } catch (Exception e) {
                Toast.makeText(requireActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        updateListView(UserDevicesState.getInstance().getDevices());

        titleTextView = requireView().findViewById(R.id.dashboardViewTitleTextView);
        titleTextView.setVisibility(View.INVISIBLE);
    }

    void getDevices() {
        ProgressDialog progress = new ProgressDialog(requireContext());
        progress.setTitle("Loading");
        progress.setMessage("Please wait...");
        progress.setCancelable(false);

        progress.show();

        String url = EnvironmentVariables.BASE_URL + "/devices";
        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        String token = UserState.getInstance().getUser().optString("token");
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        UserDevicesState.getInstance().setDevices(response);
                        updateListView(UserDevicesState.getInstance().getDevices());
                    } catch (Exception e) {
                        Toast.makeText(requireActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    progress.dismiss();
                    if (titleTextView != null) titleTextView.setVisibility(View.VISIBLE);
                },
                error -> {
                    Log.d("error", error.toString());
                    Toast.makeText(requireActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                    progress.dismiss();
                    if (titleTextView != null) titleTextView.setVisibility(View.VISIBLE);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", token);
                return params;
            }
        };

        queue.add(jsonObjectRequest);
    }

    void updateListView(ArrayList<JSONObject> devices) {
        String[] deviceIds = new String[devices.size()];
        for (int i = 0; i < devices.size(); i++) {
            try {
                JSONObject device = devices.get(i);
                deviceIds[i] = device.getString("id");
            } catch (Exception e) {
                Toast.makeText(requireActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        devicesListView.setAdapter(new DashboardListViewAdapter(requireActivity(), devices));
    }
}