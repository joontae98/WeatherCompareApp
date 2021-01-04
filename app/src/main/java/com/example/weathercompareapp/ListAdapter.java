package com.example.weathercompareapp;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import static com.example.weathercompareapp.MainActivity.StringToBitmap;
import static com.example.weathercompareapp.MainActivity.blue;
import static com.example.weathercompareapp.MainActivity.red;

public class ListAdapter extends ArrayAdapter {

    public ListAdapter(@NonNull Context context, int resource, @NonNull List objects) {

        super(context, resource, objects);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_weather, parent, false);

        }

        TextView row_time = convertView.findViewById(R.id.textView_row_time);
        ImageView row_icon = convertView.findViewById(R.id.imageView_row_icon);
        TextView row_temp = convertView.findViewById(R.id.textView_row_temp);
        TextView row_compare = convertView.findViewById(R.id.textView_row_compare);

        WeatherData list_view_item = (WeatherData) getItem(position);

        row_time.setText(list_view_item.getTime());
        row_icon.setImageBitmap(StringToBitmap(list_view_item.getIcon()));
        row_temp.setText(list_view_item.getHourTemp());
        if (list_view_item.getCompTemp() >= 0) {
            row_compare.setTextColor(red);
        } else {
            row_compare.setTextColor(blue);
        }
        row_compare.setText(Integer.toString(list_view_item.getCompTemp()));

        return convertView;

    }

}