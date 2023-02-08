package com.example.movesafe.states;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

// create a singleton class to store the user's devices
public class UserDevicesState {
    private static UserDevicesState instance = null;
    private ArrayList<JSONObject> devices;

    private UserDevicesState() {
        devices = new ArrayList<>();
    }

    public static UserDevicesState getInstance() {
        if (instance == null) {
            instance = new UserDevicesState();
        }
        return instance;
    }

    public ArrayList<JSONObject> getDevices() {
        return devices;
    }

    public JSONObject getDevice(String deviceId) {
        for (int i = 0; i < devices.size(); i++) {
            JSONObject device = devices.get(i);
            if (device.optString("id").equals(deviceId)) {
                return device;
            }
        }
        return null;
    }

    public void setDevices(ArrayList<JSONObject> devices) {
        this.devices = devices;
    }

    public void setDevices(JSONArray devices) {
        this.devices = new ArrayList<>();
        for (int i = 0; i < devices.length(); i++) {
            try {
                this.devices.add(devices.getJSONObject(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}