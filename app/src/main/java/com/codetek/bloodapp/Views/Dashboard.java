package com.codetek.bloodapp.Views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.codetek.bloodapp.Models.Utils;
import com.codetek.bloodapp.R;
import com.google.android.gms.maps.model.Dash;

public class Dashboard extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;

    ConstraintLayout dashboard_request_blood,dashboard_hospitals,dashboard_campaigns,dashboard_stock;

    private TextView dashboard_name,dashboard_rank,dashboard_points,dashboard_email;
    private ImageView dashboard_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initState();
    }

    private void initState() {
        dashboard_request_blood=findViewById(R.id.dashboard_request_blood);
        dashboard_request_blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this,RequestBloodList.class));
            }
        });

        dashboard_hospitals=findViewById(R.id.dashboard_hospitals);
        dashboard_hospitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this,Hospitals.class));
            }
        });


        dashboard_campaigns=findViewById(R.id.dashboard_campaigns);
        dashboard_campaigns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this,Campaigns.class));
            }
        });

        dashboard_stock=findViewById(R.id.dashboard_stock);
        dashboard_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this,BloodStock.class));
            }
        });

        dashboard_email=findViewById(R.id.dashboard_email);
        dashboard_email.setText(Utils.getUser().getEmail());

        dashboard_name=findViewById(R.id.dashboard_name);
        dashboard_name.setText(Utils.getUser().getName());

        dashboard_rank=findViewById(R.id.dashboard_rank);
        dashboard_rank.setText(String.valueOf(Utils.getUser().getRank()));

        dashboard_points=findViewById(R.id.dashboard_points);
        dashboard_points.setText(String.valueOf(Utils.getUser().getPoints()));

        dashboard_logout=findViewById(R.id.dashboard_logout);
        dashboard_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this,Login.class));
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}