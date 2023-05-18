package com.codetek.bloodapp.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.codetek.bloodapp.Models.DB.Hospital;
import com.codetek.bloodapp.Models.Utils;
import com.codetek.bloodapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BloodStock extends AppCompatActivity implements  AdapterView.OnItemSelectedListener{

    ImageView stock_list_back_btn2;
    Spinner stock_hospital_spinner;
    Button stock_get_data_btn;
    Hospital selectedHospital;
    private ArrayAdapter spinnerAdapter ;
    LinearLayout stock_bloods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.codetek.bloodapp.R.layout.activity_blood_stock);

        stock_bloods=findViewById(R.id.stock_bloods);

        stock_list_back_btn2=findViewById(R.id.stock_list_back_btn2);
        stock_list_back_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        stock_get_data_btn=findViewById(R.id.stock_get_data_btn);

        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        stock_get_data_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stock_bloods.removeAllViews();

                if(selectedHospital!=null){
                    Map<String,String> bloodList= new HashMap<>();
                    bloodList.put("A RhD positive (A+)", String.valueOf(selectedHospital.getAplus())+"L" );
                    bloodList.put("A RhD negative (A-)", String.valueOf(selectedHospital.getAneg())+"L" );
                    bloodList.put("B RhD positive (B+)", String.valueOf(selectedHospital.getBplus())+"L" );
                    bloodList.put("B RhD negative (B-)", String.valueOf(selectedHospital.getBneg())+"L" );
                    bloodList.put("O RhD positive (O+)", String.valueOf(selectedHospital.getOplus())+"L" );
                    bloodList.put("O RhD negative (O-)", String.valueOf(selectedHospital.getOneg())+"L" );
                    bloodList.put("AB RhD positive (AB+)", String.valueOf(selectedHospital.getAbplus())+"L" );
                    bloodList.put("AB RhD negative (AB-)", String.valueOf(selectedHospital.getAbneg())+"L" );

                    for (Map.Entry<String, String> entry : bloodList.entrySet()) {
                        LayoutInflater inflater=(LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View viewRec=inflater.inflate(R.layout.activity_stock_record, null);
                        TextView title=viewRec.findViewById(R.id.stock_record_title);
                        TextView qty=viewRec.findViewById(R.id.stock_record_qty);

                        title.setText( entry.getKey());
                        qty.setText(entry.getValue() );

                        stock_bloods.addView(viewRec);
                    }
                }
            }
        });

        stock_hospital_spinner=findViewById(R.id.stock_hospital_spinner);
        stock_hospital_spinner.setOnItemSelectedListener(BloodStock.this);
        spinnerAdapter =new ArrayAdapter<Hospital>(BloodStock.this, android.R.layout.simple_spinner_item, Utils.hospitals );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stock_hospital_spinner.setAdapter(spinnerAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedHospital= Utils.hospitals.get(i);
        System.out.println(selectedHospital.getName());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}