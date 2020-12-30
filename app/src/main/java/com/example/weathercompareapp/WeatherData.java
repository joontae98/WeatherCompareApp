package com.example.weathercompareapp;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.Serializable;
import java.util.Date;

public class WeatherData implements Serializable {

    private String time;
    private String hourTemp;
    private Bitmap hourIcon;
    private String lastTemp;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        // Unix 시간 -> UTC 시간 변환
        long l = Long.parseLong(time);
        Date date = new Date(l * 1000);
        this.time = date.toString().substring(0, 16);
        Log.d("set시간", this.time);
    }

    public String getHourTemp() {
        return hourTemp;
    }

    public void setHourTemp(String hourTemp) {
        this.hourTemp = hourTemp;
    }

    public Bitmap getHourIcon() {
        return hourIcon;
    }

    public void setHourIcon(Bitmap hourIcon) {
        this.hourIcon = hourIcon;
    }

    public String getLastTemp() {
        return lastTemp;
    }

    public void setLastTemp(String lastTemp) {
        this.lastTemp = lastTemp;
    }
}
