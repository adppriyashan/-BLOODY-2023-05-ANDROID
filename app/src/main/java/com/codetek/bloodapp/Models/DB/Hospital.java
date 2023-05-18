package com.codetek.bloodapp.Models.DB;

public class Hospital {
    private int id;
    private String name,address,phone1,phone2,phone3,lng,ltd;
    private int status;
    private double aplus,aneg,bplus,bneg,oplus,oneg,abplus,abneg;
    private int district;

    public Hospital(int id, String name, String address, String phone1, String phone2, String phone3, String lng, String ltd, int status, double aplus, double aneg, double bplus, double bneg, double oplus, double oneg, double abplus, double abneg, int district) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.phone3 = phone3;
        this.lng = lng;
        this.ltd = ltd;
        this.status = status;
        this.aplus = aplus;
        this.aneg = aneg;
        this.bplus = bplus;
        this.bneg = bneg;
        this.oplus = oplus;
        this.oneg = oneg;
        this.abplus = abplus;
        this.abneg = abneg;
        this.district = district;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone1() {
        return phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public String getPhone3() {
        return phone3;
    }

    public String getLng() {
        return lng;
    }

    public String getLtd() {
        return ltd;
    }

    public int getStatus() {
        return status;
    }

    public double getAplus() {
        return aplus;
    }

    public double getAneg() {
        return aneg;
    }

    public double getBplus() {
        return bplus;
    }

    public double getBneg() {
        return bneg;
    }

    public double getOplus() {
        return oplus;
    }

    public double getOneg() {
        return oneg;
    }

    public double getAbplus() {
        return abplus;
    }

    public double getAbneg() {
        return abneg;
    }

    public int getDistrict() {
        return district;
    }

    @Override
    public String toString() {
        return name;
    }
}
