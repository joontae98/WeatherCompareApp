package com.example.weathercompareapp;

import android.graphics.Bitmap;

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
        l += 32400;                             //32400 = Unix 시간으로 9시간
        Date date = new Date(l*1000);
        this.time = date.toString().substring(0,16);
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
