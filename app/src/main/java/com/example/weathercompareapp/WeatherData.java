package com.example.weathercompareapp;

import android.graphics.Bitmap;

import java.io.Serializable;

public class WeatherData implements Serializable {

    private String time;
    private String hourTemp;
    private Bitmap hourIcon;
    private String icon;
    private int compTemp;

    public int getCompTemp() {
        return compTemp;
    }

    public void setCompTemp(int compTemp) {
        this.compTemp = compTemp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

}
