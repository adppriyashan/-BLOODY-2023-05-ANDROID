package com.codetek.bloodapp.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codetek.bloodapp.Adapters.HospitalListAdapter;
import com.codetek.bloodapp.Models.DB.Hospital;
import com.codetek.bloodapp.Models.DB.User;
import com.codetek.bloodapp.Models.Routes;
import com.codetek.bloodapp.Models.Utils;
import com.codetek.bloodapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

public class Hospitals extends AppCompatActivity {

    ImageView hospitals_list_back_btn,hospitals_list_add;
    RecyclerView hospitalsList;
    private ProgressDialog progress;
    RequestQueue queue;

    ArrayList<Hospital> dataList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.codetek.bloodapp.R.layout.activity_hospitals);

        queue = Volley.newRequestQueue(this);
        progress=new ProgressDialog(this);

        hospitals_list_back_btn=findViewById(R.id.hospitals_list_back_btn);
        hospitals_list_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        hospitalsList=findViewById(R.id.hospitals_list_list);
        hospitals_list_add=findViewById(R.id.hospitals_list_add);
        if(Utils.getUser().getUsertype()==1 || Utils.getUser().getUsertype()==2){

            hospitals_list_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Hospitals.this,ManageHospitals.class));
                }
            });
        }else{
            hospitals_list_add.setVisibility(View.GONE);
        }

        loadContents();
    }

    private void loadContents() {
        progress.setMessage("Please wait");
        progress.show();
        dataList.clear();
        StringRequest sr = new StringRequest(Request.Method.POST, Routes.LIST_HOSPITAL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                progress.hide();
                try {
                    JSONArray dataArr=new JSONArray(response);
                    for (int x=0;x<dataArr.length();x++){

                        JSONObject arr=dataArr.getJSONObject(x);

                        dataList.add(new Hospital(arr.getInt("id"),
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
                        System.out.println(dataList.get(0).getAplus());
                    }

                    initializeRecyclerView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return null;
            }
        };
        queue.add(sr);
    }

    private void initializeRecyclerView() {
        System.out.println(dataList.size());
        HospitalListAdapter adapter = new HospitalListAdapter(Hospitals.this,dataList);
        hospitalsList.setHasFixedSize(true);
        hospitalsList.setLayoutManager(new LinearLayoutManager(Hospitals.this));
        hospitalsList.setAdapter(adapter);
    }

    @Override
    public void onRestart()
    {
        super.onRestart();

        if (Utils.refresPreviousState){
            Utils.refresPreviousState=false;
            Toast.makeText(this, "Refreshing", Toast.LENGTH_SHORT).show();
            loadContents();
        }
    }
}