package com.example.movesafe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.movesafe.R;

import org.json.JSONObject;

import java.util.ArrayList;

public class DashboardListViewAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<JSONObject> list;

    public DashboardListViewAdapter(Context context, ArrayList<JSONObject> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.dashboard_view_list_view_item, viewGroup, false);
        }

        JSONObject item = list.get(i);

        TextView nameTextView = view.findViewById(R.id.dashboardViewNameTextView);
        nameTextView.setText(item.optString("name", "placeholder_device_naFme"));

        TextView idTextView = view.findViewById(R.id.dashboardViewIdTextView);
        idTextView.setText(item.optString("unique_id", "placeholder_device_id"));

        return view;
    }
}
