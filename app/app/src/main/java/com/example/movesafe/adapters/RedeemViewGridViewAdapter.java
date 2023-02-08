package com.example.movesafe.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movesafe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class RedeemViewGridViewAdapter extends BaseAdapter {
    private final Context context;
    private final List<JSONObject> list;
    ImageView imageView;

    public RedeemViewGridViewAdapter(Context context, List<JSONObject> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.redeem_view_grid_view_item, parent, false);
        }

        JSONObject coupon = list.get(position);

        TextView titleTextView = view.findViewById(R.id.redeemViewGridViewItemTitleTextView);
        try {
            titleTextView.setText(coupon.getString("title"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageView = view.findViewById(R.id.redeemViewGridViewItemImageView);
        String imageUrl = null;
        try {
            imageUrl = coupon.getString("imageUrl");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Glide.with(view.getContext()).load(imageUrl).into(imageView);

        return view;
    }
}