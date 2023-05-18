package com.codetek.bloodapp.Models.DB;

public class Campaign {
    private int id;
    private String name,info,date;
    private double lng,ltd;
    private int status;

    public Campaign(int id, String name, String info, String date, double lng, double ltd, int status) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.date = date;
        this.lng = lng;
        this.ltd = ltd;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public String getDate() {
        return date;
    }

    public double getLng() {
        return lng;
    }

    public double getLtd() {
        return ltd;
    }

    public int getStatus() {
        return status;
    }
}
