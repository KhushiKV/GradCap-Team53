package com.example.movesafe.fragments.feedback;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.movesafe.R;
import com.example.movesafe.states.UserState;
import com.example.movesafe.utils.EnvironmentVariables;
import com.google.android.material.slider.RangeSlider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivityFeedbackFragment extends Fragment {

    public MainActivityFeedbackFragment() {
        // Required empty public constructor
    }

    EditText feedbackEditText;
    Button submitButton;
    RangeSlider ratingSlider;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_activity_feedback, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        feedbackEditText = requireView().findViewById(R.id.feedbackViewFeedbackEditText);

        submitButton = requireView().findViewById(R.id.feedbackViewSubmitButton);
        submitButton.setOnClickListener(this::handleClickSubmitButton);

        ratingSlider = requireView().findViewById(R.id.feedbackViewRatingSlider);
    }

    void handleClickSubmitButton(View v) {
        String feedback = feedbackEditText.getText().toString();
        String ratingValue = ratingSlider.getValues().get(0).toString();
        if (!ratingValue.isEmpty()) {
            feedback += " " + ratingValue;
        }

        if (feedback.isEmpty()) {
            feedbackEditText.setError("Please enter your feedback!");
            return;
        }

        String url = EnvironmentVariables.BASE_URL + "feedbacks";
        RequestQueue queue = Volley.newRequestQueue(requireActivity());

        JSONObject body = new JSONObject();
        try {
            body.put("feedback", feedback);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                response -> {
                    Toast.makeText(requireActivity(), "Feedback submitted!", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Toast.makeText(requireActivity(), "Feedback submission failed!", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = null;
                try {
                    token = UserState.getInstance().getUser().getString("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                headers.put("Authorization", token);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }

    void handleClickCancelButton(View v) {
        requireActivity().onBackPressed();
    }
}