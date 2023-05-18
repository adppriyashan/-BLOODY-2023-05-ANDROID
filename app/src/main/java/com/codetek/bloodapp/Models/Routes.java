package com.codetek.bloodapp.Models;

public class Routes {
    public static String LOGIN=Utils.getApiUrl()+"auth/login";
    public static String REGISTER=Utils.getApiUrl()+"auth/register";
    public static String ADD_HOSPITAL=Utils.getApiUrl()+"hospital/add";
    public static String DELETE_HOSPITAL=Utils.getApiUrl()+"hospital/delete";
    public static String LIST_HOSPITAL=Utils.getApiUrl()+"hospital/list";
    public static String ADD_BR=Utils.getApiUrl()+"bloodrequests/add";
    public static String DELETE_BR=Utils.getApiUrl()+"bloodrequests/delete";
    public static String LIST_BR=Utils.getApiUrl()+"bloodrequests/list";
    public static String ADD_CAMPAIGN=Utils.getApiUrl()+"campaign/add";
    public static String DELETE_CAMPAIGN=Utils.getApiUrl()+"campaign/delete";
    public static String LIST_CAMPAIGN=Utils.getApiUrl()+"campaign/list";
}
