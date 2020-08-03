package com.example.newapplication.model;

public class History {
    String time;
    Float suhu;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Float getSuhu() {
        return suhu;
    }

    public void setSuhu(Float suhu) {
        this.suhu = suhu;
    }

    public History(String time, Float suhu) {
        this.time = time;
        this.suhu = suhu;
    }



}
