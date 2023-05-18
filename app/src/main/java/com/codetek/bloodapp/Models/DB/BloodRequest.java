package com.codetek.bloodapp.Models.DB;

public class BloodRequest {
    private int id,hospital,user,bloodtype;
    private String tel, name;

    public BloodRequest(int id, int hospital, int user, int bloodtype, String tel, String name) {
        this.id = id;
        this.hospital = hospital;
        this.user = user;
        this.bloodtype = bloodtype;
        this.tel = tel;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getHospital() {
        return hospital;
    }

    public int getUser() {
        return user;
    }

    public int getBloodtype() {
        return bloodtype;
    }

    public String getTel() {
        return tel;
    }

    public String getName() {
        return name;
    }
}
