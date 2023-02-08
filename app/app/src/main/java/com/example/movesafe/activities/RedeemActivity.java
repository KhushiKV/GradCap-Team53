package com.example.movesafe.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.example.movesafe.R;
import com.example.movesafe.adapters.RedeemViewGridViewAdapter;
import com.example.movesafe.states.CouponsState;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RedeemActivity extends AppCompatActivity {

    EditText searchEditText;
    ArrayList<JSONObject> filteredCoupons;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Redeem");
        }

        searchEditText = findViewById(R.id.redeemViewSearchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                handle search drawable
                if (editable.length() > 0) {
                    searchEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                } else {
                    searchEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_search_24, 0, 0, 0);
                }

                handleFilterCoupons(editable.toString());
            }
        });

        gridView = findViewById(R.id.redeemViewGridView);
        filteredCoupons = new ArrayList<>(CouponsState.getInstance().getCoupons());
        gridView.setAdapter(new RedeemViewGridViewAdapter(this, filteredCoupons));
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            startActivity(new Intent(this, CouponActivity.class).putExtra("couponPosition", position));
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void handleFilterCoupons(String query) {
        filteredCoupons.clear();
        if (query.length() > 0) {
            List<JSONObject> coupons = CouponsState.getInstance().getCoupons();
            for (JSONObject coupon : coupons) {
                try {
                    String title = coupon.getString("title");
                    if (title.contains(query)) {
                        filteredCoupons.add(coupon);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            filteredCoupons = new ArrayList<>(CouponsState.getInstance().getCoupons());
        }
        gridView.setAdapter(new RedeemViewGridViewAdapter(this, filteredCoupons));
    }
}