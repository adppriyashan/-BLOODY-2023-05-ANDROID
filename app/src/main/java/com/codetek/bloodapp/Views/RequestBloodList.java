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
import com.codetek.bloodapp.Adapters.BloodRequestAdapter;
import com.codetek.bloodapp.Adapters.HospitalListAdapter;
import com.codetek.bloodapp.Models.DB.BloodRequest;
import com.codetek.bloodapp.Models.DB.Hospital;
import com.codetek.bloodapp.Models.Routes;
import com.codetek.bloodapp.Models.Utils;
import com.codetek.bloodapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class RequestBloodList extends AppCompatActivity {

    ImageView hospitals_list_back_btn,bloodrequest_list_add2;
    RecyclerView blood_list_view;

    private ProgressDialog progress;
    RequestQueue queue;

    ArrayList<BloodRequest> dataList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.codetek.bloodapp.R.layout.activity_request_blood_list);

        queue = Volley.newRequestQueue(this);
        progress=new ProgressDialog(this);

        hospitals_list_back_btn=findViewById(R.id.hospitals_list_back_btn);
        hospitals_list_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        bloodrequest_list_add2=findViewById(R.id.bloodrequest_list_add2);
        bloodrequest_list_add2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Add Window");
                startActivity(new Intent(RequestBloodList.this,RequestBlood.class));
            }
        });

        blood_list_view=findViewById(R.id.blood_list_view);

        loadContents();

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

    private void loadContents() {
        progress.setMessage("Please wait");
        progress.show();
        dataList.clear();
        StringRequest sr = new StringRequest(Request.Method.POST, Routes.LIST_BR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                progress.hide();
                try {
                    JSONArray dataArr=new JSONArray(response);
                    for (int x=0;x<dataArr.length();x++){

                        JSONObject arr=dataArr.getJSONObject(x);

                        dataList.add(new BloodRequest(arr.getInt("id"),
                                0,
                                arr.getInt("user"),
                                arr.getInt("bloodtype"),
                                arr.getString("tel"),
                                arr.getString("name")
                        ));
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
        BloodRequestAdapter adapter = new BloodRequestAdapter(RequestBloodList.this,dataList);
        blood_list_view.setHasFixedSize(true);
        blood_list_view.setLayoutManager(new LinearLayoutManager(RequestBloodList.this));
        blood_list_view.setAdapter(adapter);
    }
}