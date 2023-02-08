package com.example.movesafe.states;

import org.json.JSONObject;

public class UserState {
    private static UserState instance = null;
    private JSONObject user;

    private UserState() {
        user = new JSONObject();
    }

    public static UserState getInstance() {
        if (instance == null) {
            instance = new UserState();
        }
        return instance;
    }

    public JSONObject getUser() {
        return user;
    }

    public void setUser(JSONObject user) {
        this.user = user;
    }
}
