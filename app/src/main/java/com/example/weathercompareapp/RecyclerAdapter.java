package com.example.weathercompareapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static com.example.weathercompareapp.MainActivity.blue;
import static com.example.weathercompareapp.MainActivity.red;

//RecyclerView 연결 Adapter 클래스
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private List<WeatherData> mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_row_time;
        private ImageView imageView_row_icon;
        private TextView textView_row_temp;
        private TextView textView_row_compare;

        public MyViewHolder(View v) {
            super(v);
            textView_row_time = v.findViewById(R.id.textView_row_time);
            imageView_row_icon = v.findViewById(R.id.imageView_row_icon);
            textView_row_temp = v.findViewById(R.id.textView_row_temp);
            textView_row_compare = v.findViewById(R.id.textView_row_compare);
        }
    }

    public RecyclerAdapter(List<WeatherData> myDataset) {                     //생성자 매서드
        mDataset = myDataset;
    }

    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_weather, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        WeatherData weathers = mDataset.get(position);
        holder.textView_row_time.setText(weathers.getTime());
        holder.imageView_row_icon.setImageBitmap(weathers.getHourIcon());
        holder.textView_row_temp.setText(weathers.getHourTemp());

        if (weathers.getCompTemp() >= 0 ) {
            holder.textView_row_compare.setTextColor(red);
        } else {
            holder.textView_row_compare.setTextColor(blue);
        }
        holder.textView_row_compare.setText((Integer.toString(weathers.getCompTemp())));

    }

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

}
