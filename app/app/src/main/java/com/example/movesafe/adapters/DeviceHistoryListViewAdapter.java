package com.example.movesafe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.movesafe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DeviceHistoryListViewAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<String> list;

    public DeviceHistoryListViewAdapter(Context context, ArrayList<String> list) {
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.device_history_view_list_view_item, viewGroup, false);
        }

        view.setEnabled(false);
        view.setOnClickListener(null);

        String item = list.get(position);
        String[] itemArray = item.split(" ");
        String title, value;
        value = itemArray[itemArray.length - 1];
        title = item.substring(0, item.length() - value.length() - 1);

        TextView titleTextView = view.findViewById(R.id.deviceHistoryViewTitleTextView);
        titleTextView.setText(title.toUpperCase());

        TextView valueTextView = view.findViewById(R.id.deviceHistoryViewValueTextView);
        valueTextView.setText(value);
        if(value.charAt(0) == '-') {
            valueTextView.setTextColor(context.getResources().getColor(R.color.red));
        }

        return view;
    }
}
