package com.example.movesafe.fragments.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.movesafe.R;
import com.example.movesafe.states.UserState;

import org.json.JSONObject;

public class MainActivityProfileFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_activity_profile, container, false);
    }

    TextView nameTextView;
    TextView emailTextView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        JSONObject user = UserState.getInstance().getUser();

        nameTextView = view.findViewById(R.id.profileFragmentNameTextView);
        nameTextView.setText(user.optString("name"));

        emailTextView = view.findViewById(R.id.profileFragmentEmailTextView);
        emailTextView.setText(user.optString("email"));
    }
}