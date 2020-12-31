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
    private int compTemp;

    public int getCompTemp() {
        int curTemp;
        int lastTemp;
        int resultTemp;
        curTemp = parsing(this.hourTemp);
        lastTemp = parsing(this.lastTemp);
        resultTemp = curTemp - lastTemp;
        this.compTemp = resultTemp;
        return compTemp;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        // Unix 시간 -> UTC 시간 변환
        long l = Long.parseLong(time);
        Date date = new Date(l * 1000);
        this.time = date.toString().substring(0, 16);
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

    public void setLastTemp(String lastTemp) {
        this.lastTemp = lastTemp;
    }

    public int parsing(String str) {
//        Log.d("들어온 값", str);
        int parAfter = (int) Double.parseDouble(str);
//        System.out.println("파싱 후 " + parAfter);
        return parAfter;
    }
}
