package com.codetek.bloodapp.Controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codetek.bloodapp.Models.DB.Hospital;
import com.codetek.bloodapp.Models.DB.User;
import com.codetek.bloodapp.Models.Utils;
import com.codetek.bloodapp.Views.Dashboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class AuthController {
    private ProgressDialog progress;
    private String url;
    public Context context;
    RequestQueue queue;

    public AuthController(Context context, String url){
        this.url=url;
        this.context=context;
        queue = Volley.newRequestQueue(this.context);
        progress=new ProgressDialog(context);
    }

    public void doRegister(Map<String,String> data) {
        progress.setMessage("Please wait");
        progress.show();
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                progress.hide();
                try {
                    JSONObject responseObject=new JSONObject(response);
                    Toast.makeText(context, responseObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                progress.hide();
                Toast.makeText(context, "Server Error, Please try again", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return data;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(sr);
    }

    public void doLogin(Map<String,String> data) {

        progress.setMessage("Please wait");
        progress.show();
        System.out.println(url);
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                progress.hide();
                try {
                    JSONObject resp=new JSONObject(response);
                    JSONObject responseObject=resp.getJSONObject("user");
                    JSONArray hospitals=resp.getJSONArray("hospitals");
                    Utils.hospitals.clear();
                    for (int x=0;x<hospitals.length();x++){
                        JSONObject arr=hospitals.getJSONObject(x);
                        Utils.hospitals.add(new Hospital(arr.getInt("id"),
                                arr.getString("name"),
                                arr.getString("address"),
                                arr.getString("phone1"),
                                arr.getString("phone2"),
                                arr.getString("phone3"),
                                arr.getString("lng"),
                                arr.getString("ltd"),
                                arr.getInt("status"),
                                arr.getDouble("aplus"),
                                arr.getDouble("aneg"),
                                arr.getDouble("bplus"),
                                arr.getDouble("bneg"),
                                arr.getDouble("oplus"),
                                arr.getDouble("oneg"),
                                arr.getDouble("abplus"),
                                arr.getDouble("abneg"),
                                arr.getInt("district")
                        ));
                    }

                    Utils.setUser(new User(responseObject.getInt("id"),responseObject.getString("name"),responseObject.getString("email"),responseObject.getString("tel"), responseObject.getInt("usertype"), responseObject.getInt("district"), responseObject.getInt("bloodtype"),responseObject.getInt("points"),responseObject.getInt("rank")));
                    System.out.println("Logged user - "+Utils.getUser().getEmail());
                    context.startActivity(new Intent(context, Dashboard.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                String body;
                if(error.networkResponse!=null && error.networkResponse.data!=null) {
                    try {
                        body = new String(error.networkResponse.data,"UTF-8");
                        System.out.println(body);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                progress.hide();
                if(error.networkResponse!=null && error.networkResponse.statusCode!=422){
                    Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Server Error, Please try again", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return data;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }
}
