package com.example.weathercompareapp;

import android.util.Log;
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
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<WeatherData> mDataset;
    int curTemp;
    int lastTemp;
    int compTemp;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewrowtime;
        public ImageView imageviewrowicon;
        public TextView textviewrowtemp;
        public TextView textviewrowcompare;

        public MyViewHolder(View v) {
            super(v);
            textviewrowtime = v.findViewById(R.id.textview_row_time);
            imageviewrowicon = v.findViewById(R.id.imageview_row_icon);
            textviewrowtemp = v.findViewById(R.id.textview_row_temp);
            textviewrowcompare = v.findViewById(R.id.textview_row_compare);
        }
    }

    public MyAdapter(List<WeatherData> myDataset) {                     //생성자 매서드
        mDataset = myDataset;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_weather, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        WeatherData weathers = mDataset.get(position);
        Log.d("시간",weathers.getTime()+"  / "+position);
        holder.textviewrowtime.setText(weathers.getTime());
        holder.imageviewrowicon.setImageBitmap(weathers.getHourIcon());
        holder.textviewrowtemp.setText(weathers.getHourTemp());

        curTemp = parsing(weathers.getHourTemp());
        lastTemp = parsing(weathers.getLastTemp());
        compTemp = (curTemp) - (lastTemp);
        if (compTemp >= 0 ) {
            holder.textviewrowcompare.setTextColor(red);
        } else {
            holder.textviewrowcompare.setTextColor(blue);
        }
        holder.textviewrowcompare.setText((Integer.toString(compTemp)));

    }

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

    public int parsing(String str) {
//        Log.d("들어온 값", str);
        int parAfter = (int) Double.parseDouble(str);
//        System.out.println("파싱 후 " + parAfter);
        return parAfter;
    }
}
