package com.codetek.bloodapp.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codetek.bloodapp.Models.Routes;
import com.codetek.bloodapp.Models.Utils;
import com.codetek.bloodapp.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageHospitals extends AppCompatActivity implements Validator.ValidationListener, AdapterView.OnItemSelectedListener{

    private int updateId;
    private ImageView hospitals_back_btn;
    @NotEmpty
    private EditText hospitalName,hospitalDesc,hospitalPhone1,hospitalPhone2,hospitalPhone3,hospitalLng,hospitalLtd,
            hospital_aplus,hospital_aneg,hospital_bplus,hospital_bneg,hospital_oplus,hospital_oneg,hospital_abplus,hospital_abneg;
    private Button hospitalSubmitButton;
    private Validator validator;

    private int district=0;
    private ArrayAdapter spinnerAdapter ;
    private Spinner districtSpinner;
    String [] dataList;

    private ProgressDialog progress;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.codetek.bloodapp.R.layout.activity_manage_hospitals);

        queue = Volley.newRequestQueue(this);
        progress=new ProgressDialog(this);

        validator = new Validator(this);
        validator.setValidationListener(ManageHospitals.this);

        hospitals_back_btn=findViewById(R.id.hospitals_back_btn);
        hospitalName=findViewById(R.id.hospitalName);
        hospitalDesc=findViewById(R.id.hospitalDesc);
        hospitalPhone1=findViewById(R.id.hospitalPhone1);
        hospitalPhone2=findViewById(R.id.hospitalPhone2);
        hospitalPhone3=findViewById(R.id.hospitalPhone3);
        hospitalLng=findViewById(R.id.hospitalLng);
        hospitalLtd=findViewById(R.id.hospitalLtd);
        hospitalSubmitButton=findViewById(R.id.hospitalSubmitButton);

        dataList=Utils.getDistrictNameList();

        hospital_aplus=findViewById(R.id.hospital_aplus);
        hospital_aneg=findViewById(R.id.hospital_aneg);
        hospital_bplus=findViewById(R.id.hospital_bplus);
        hospital_bneg=findViewById(R.id.hospital_bneg);
        hospital_oplus=findViewById(R.id.hospital_oplus);
        hospital_oneg=findViewById(R.id.hospital_oneg);
        hospital_abplus=findViewById(R.id.hospital_abplus);
        hospital_abneg=findViewById(R.id.hospital_abneg);

        districtSpinner=findViewById(R.id.hospitalDistrict);

        hospitals_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        hospitalSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        districtSpinner.setOnItemSelectedListener(ManageHospitals.this);
        spinnerAdapter =new ArrayAdapter<String>(ManageHospitals.this, android.R.layout.simple_spinner_item, dataList );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(spinnerAdapter);

        updateId=getIntent().getIntExtra("id",0);
        if(updateId!=0){
            hospitalName.setText(getIntent().getStringExtra("hospitalName"));
            hospitalDesc.setText(getIntent().getStringExtra("hospitalDesc"));
            hospitalPhone1.setText(getIntent().getStringExtra("hospitalPhone1"));
            hospitalPhone2.setText(getIntent().getStringExtra("hospitalPhone2"));
            hospitalPhone3.setText(getIntent().getStringExtra("hospitalPhone3"));
            hospitalLng.setText(getIntent().getStringExtra("hospitalLng"));
            hospitalLtd.setText(getIntent().getStringExtra("hospitalLtd"));
            hospital_aplus.setText(String.valueOf(getIntent().getDoubleExtra("hospital_aplus",0)));
            hospital_aneg.setText(String.valueOf(getIntent().getDoubleExtra("hospital_aneg",0)));
            hospital_bplus.setText(String.valueOf(getIntent().getDoubleExtra("hospital_bplus",0)));
            hospital_bneg.setText(String.valueOf(getIntent().getDoubleExtra("hospital_bneg",0)));
            hospital_oplus.setText(String.valueOf(getIntent().getDoubleExtra("hospital_oplus",0)));
            hospital_oneg.setText(String.valueOf(getIntent().getDoubleExtra("hospital_oneg",0)));
            hospital_abplus.setText(String.valueOf(getIntent().getDoubleExtra("hospital_abplus",0)));
            hospital_abneg.setText(String.valueOf(getIntent().getDoubleExtra("hospital_abneg",0)));
            district=getIntent().getIntExtra("district",0);
            districtSpinner.setSelection(district-1);
        }
    }
    private void enrollHospitals() {
        Map<String,String> data=new HashMap<>();
        data.put("name",hospitalName.getText().toString());
        data.put("address",hospitalDesc.getText().toString());
        data.put("phone1",hospitalPhone1.getText().toString());
        data.put("phone2",hospitalPhone2.getText().toString());
        data.put("phone3",hospitalPhone3.getText().toString());
        data.put("lng",hospitalLng.getText().toString());
        data.put("ltd",hospitalLtd.getText().toString());
        data.put("aplus",hospital_aplus.getText().toString());
        data.put("aneg",hospital_aneg.getText().toString());
        data.put("bplus",hospital_bplus.getText().toString());
        data.put("bneg",hospital_bneg.getText().toString());
        data.put("oplus",hospital_oplus.getText().toString());
        data.put("oneg",hospital_oneg.getText().toString());
        data.put("abplus",hospital_abplus.getText().toString());
        data.put("abneg",hospital_abneg.getText().toString());
        data.put("district",String.valueOf(district));

        if(updateId!=0){
            data.put("id",String.valueOf(updateId));
        }

        progress.setMessage("Please wait");
        progress.show();

        StringRequest sr = new StringRequest(Request.Method.POST, Routes.ADD_HOSPITAL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.hide();
                Utils.refresPreviousState=true;
                onBackPressed();
                Toast.makeText(ManageHospitals.this,"Process Completed Successfully.", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                progress.hide();
                Toast.makeText(ManageHospitals.this, "Server Error, Please try again", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return data;
            }
        };
        queue.add(sr);
    }

    @Override
    public void onValidationSucceeded() {
        enrollHospitals();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        district=Utils.getDistrictIdList()[i];
        System.out.println(district);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}