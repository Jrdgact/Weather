package com.example.weather.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class DayWeatherBean implements Serializable {


    @SerializedName("date")
    private String date;


    @SerializedName("wea")
    private String wea;

    @SerializedName("wea_img")
    private String weaImg;

    @SerializedName("tem_day")
    private String tem_day;

    @SerializedName("tem_night")
    private String tem_night;

    @SerializedName("win")
    private String win;

    @SerializedName("win_speed")
    private String winSpeed;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWea() {
        return wea;
    }

    public void setWea(String wea) {
        this.wea = wea;
    }

    public String getWeaImg() {
        return weaImg;
    }

    public void setWeaImg(String weaImg) {
        this.weaImg = weaImg;
    }

    public String getTem_day() {
        return tem_day;
    }

    public void setTem_day(String tem_day) {
        this.tem_day = tem_day;
    }

    public String getTem_night() {
        return tem_night;
    }

    public void setTem_night(String tem_night) {
        this.tem_night = tem_night;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public String getWinSpeed() {
        return winSpeed;
    }

    public void setWinSpeed(String winSpeed) {
        this.winSpeed = winSpeed;
    }

    @Override
    public String toString() {
        return "DayWeatherBean{" +
                "date='" + date + '\'' +
                ", wea='" + wea + '\'' +
                ", weaImg='" + weaImg + '\'' +
                ", tem_day='" + tem_day + '\'' +
                ", tem_night='" + tem_night + '\'' +
                ", win='" + win + '\'' +
                ", winSpeed='" + winSpeed + '\'' +
                '}';
    }
}
