package com.codetek.bloodapp.Models.DB;

public class User {
    private int id;
    private String name,email,tel;
    private int usertype, district,bloodtype,points,rank;

    public User(int id, String name, String email, String tel, int usertype, int district, int bloodtype, int points, int rank) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.tel = tel;
        this.usertype = usertype;
        this.district = district;
        this.bloodtype = bloodtype;
        this.points = points;
        this.rank = rank;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getTel() {
        return tel;
    }

    public int getUsertype() {
        return usertype;
    }

    public int getDistrict() {
        return district;
    }

    public int getBloodtype() {
        return bloodtype;
    }

    public int getPoints() {
        return points;
    }

    public int getRank() {
        return rank;
    }
}
