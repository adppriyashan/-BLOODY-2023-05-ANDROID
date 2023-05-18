package com.codetek.bloodapp.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codetek.bloodapp.Adapters.CampaignListAdapter;
import com.codetek.bloodapp.Adapters.HospitalListAdapter;
import com.codetek.bloodapp.Models.DB.Campaign;
import com.codetek.bloodapp.Models.DB.Hospital;
import com.codetek.bloodapp.Models.Routes;
import com.codetek.bloodapp.Models.Utils;
import com.codetek.bloodapp.R;
import com.mobsandgeeks.saripaar.Validator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class Campaigns extends AppCompatActivity {

    ImageView campaigns_back_btn,campaigns_add_btn;
    RecyclerView listView;
    ArrayList<Campaign> dataList;
    private ProgressDialog progress;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.codetek.bloodapp.R.layout.activity_campaigns);

        campaigns_back_btn=findViewById(R.id.campaigns_back_btn);
        campaigns_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        queue = Volley.newRequestQueue(this);
        progress=new ProgressDialog(this);

        listView=findViewById(R.id.campaigns_list);
        dataList=new ArrayList<>();

        campaigns_add_btn=findViewById(R.id.campaigns_add_btn);
        if(Utils.getUser().getUsertype()==3){
            campaigns_add_btn.setVisibility(View.GONE);
        }else{
            campaigns_add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Campaigns.this, ManageCampaign.class));
                }
            });
        }

        loadContents();
    }

    private void loadContents() {
        progress.setMessage("Please wait");
        progress.show();
        dataList.clear();

        StringRequest sr = new StringRequest(Request.Method.POST, Routes.LIST_CAMPAIGN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                progress.hide();
                try {
                    JSONArray dataArr=new JSONArray(response);
                    for (int x=0;x<dataArr.length();x++){

                        JSONObject arr=dataArr.getJSONObject(x);

                        System.out.println(arr.toString());

                        dataList.add(new Campaign(arr.getInt("id"),
                                arr.getString("name"),
                                arr.getString("info"),
                                arr.getString("date"),
                                arr.getDouble("lng"),
                                arr.getDouble("ltd"),
                                arr.getInt("status")
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
        sr.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }

    private void initializeRecyclerView() {
        CampaignListAdapter adapter = new CampaignListAdapter(Campaigns.this,dataList);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(Campaigns.this));
        listView.setAdapter(adapter);
    }

    @Override
    public void onRestart()
    {
        super.onRestart();

        if (Utils.refresPreviousState){
            Utils.refresPreviousState=false;
            Toast.makeText(this, "Refreshing", Toast.LENGTH_SHORT).show();
//            loadContents();
        }
    }
}