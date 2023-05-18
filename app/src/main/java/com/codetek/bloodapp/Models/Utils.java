package com.codetek.bloodapp.Models;

import com.codetek.bloodapp.Models.DB.Hospital;
import com.codetek.bloodapp.Models.DB.User;

import java.util.ArrayList;
import java.util.Locale;

import okhttp3.MediaType;

public class Utils {
    private static String baseUrl="http://192.168.1.170:8001/";
    private static String apiUrl=baseUrl+"api/";
    private static User user;

    public static ArrayList<Hospital> hospitals=new ArrayList<>();

//    private static ArrayList<Country> countriesList;
    private static String allCountries[];

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static String getApiUrl() {
        return apiUrl;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Utils.user = user;
    }

    public static String[] getBloodTypeNameList(){
        String[] dataList = {"A RhD positive (A+)","A RhD negative (A-)","B RhD positive (B+)","B RhD negative (B-)","O RhD positive (O+)","O RhD negative (O-)","AB RhD positive (AB+)","AB RhD negative (AB-)"};
        return dataList;
    }

    public static int[] getBloodTypeIdList(){
        int[] dataList = {1,2,3,4,5,6,7,8};
        return dataList;
    }

    public static String[] getDistrictNameList(){
        String[] dataList = {"Colombo","Gampaha","Kalutara","Kandy"};
        return dataList;
    }

    public static int[] getDistrictIdList(){
        int[] dataList = {1,2,3,4};
        return dataList;
    }

    public static boolean refresPreviousState=false;
}
