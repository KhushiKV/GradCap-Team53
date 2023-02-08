package com.example.movesafe.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.movesafe.R;
import com.example.movesafe.states.CouponsState;

import org.json.JSONException;
import org.json.JSONObject;

public class CouponActivity extends AppCompatActivity {

    ImageView couponImageView;
    TextView titleTextView;
    TextView descriptionTextView;
    Button redeemButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Coupon");
        }

        int position = getIntent().getIntExtra("couponPosition", -1);
        JSONObject coupon = CouponsState.getInstance().getCoupons().get(position);

        couponImageView = findViewById(R.id.couponViewImageView);
        try {
            Glide.with(this).load(coupon.getString("imageUrl")).into(couponImageView);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        titleTextView = findViewById(R.id.couponViewTitleTextView);
        titleTextView.setText(coupon.optString("title"));

        descriptionTextView = findViewById(R.id.couponViewDescriptionTextView);
        descriptionTextView.setText(coupon.optString("description"));

        redeemButton = findViewById(R.id.couponViewRedeemButton);
        redeemButton.setOnClickListener(this::handleClickRedeemButton);

        cancelButton = findViewById(R.id.couponViewCancelButton);
        cancelButton.setOnClickListener(this::handleClickCancelButton);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    void handleClickRedeemButton(View view) {
        finish();
    }

    void handleClickCancelButton(View view) {
        finish();
    }
}