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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codetek.bloodapp.Models.DB.Hospital;
import com.codetek.bloodapp.Models.Routes;
import com.codetek.bloodapp.Models.Utils;
import com.codetek.bloodapp.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestBlood extends AppCompatActivity implements AdapterView.OnItemSelectedListener, Validator.ValidationListener{

    private ImageView hospitals_list_back_btn2;
    private Spinner rb_blood_type,rb_hospitals;
    @NotEmpty
    private EditText rb_tel;
    private int bloodType, hospital;
    private ArrayAdapter spinnerAdapter1,spinnerAdapter2 ;
    private Button rb_submit;
    String [] dataList1;
    private Validator validator;
    private int updateId;

    TextView rb_tel_label,rb_blood_hospital_label;

    private ProgressDialog progress;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_blood);

        queue = Volley.newRequestQueue(this);
        progress=new ProgressDialog(this);
        progress.setMessage("Please wait");

        hospitals_list_back_btn2=findViewById(R.id.hospitals_list_back_btn2);
        hospitals_list_back_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        rb_tel_label=findViewById(R.id.rb_tel_label);
        rb_blood_hospital_label=findViewById(R.id.rb_blood_hospital_label);

        rb_blood_type=findViewById(R.id.rb_blood_type);
        rb_hospitals=findViewById(R.id.rb_hospitals);
        rb_tel=findViewById(R.id.rb_tel);

        dataList1= Utils.getBloodTypeNameList();

        rb_blood_type.setOnItemSelectedListener(RequestBlood.this);
        spinnerAdapter1 =new ArrayAdapter<String>(RequestBlood.this, android.R.layout.simple_spinner_item, dataList1 );
        spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rb_blood_type.setAdapter(spinnerAdapter1);

        rb_hospitals.setOnItemSelectedListener(RequestBlood.this);
        spinnerAdapter2 =new ArrayAdapter<Hospital>(RequestBlood.this, android.R.layout.simple_spinner_item, Utils.hospitals );
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rb_hospitals.setAdapter(spinnerAdapter2);

        if(Utils.getUser().getUsertype()==3){
            rb_hospitals.setVisibility(View.GONE);
            rb_blood_hospital_label.setVisibility(View.GONE);
        }else{
            rb_tel.setVisibility(View.GONE);
            rb_tel_label.setVisibility(View.GONE);
        }

        validator = new Validator(this);
        validator.setValidationListener(RequestBlood.this);

        rb_submit=findViewById(R.id.rb_submit);
        rb_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        updateId=getIntent().getIntExtra("id",0);
        if(updateId!=0){
            rb_tel.setText(getIntent().getStringExtra("tel"));
            rb_blood_type.setSelection(bloodType-1);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId()==rb_blood_type.getId()){
            bloodType=Utils.getBloodTypeIdList()[i];
        }else{
            hospital=Utils.hospitals.get(i).getId();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onValidationSucceeded() {
        Map<String,String> data= new HashMap<String, String>();
        data.put("bloodtype",String.valueOf(bloodType));
        data.put("user",String.valueOf(Utils.getUser().getId()));

        if(Utils.getUser().getUsertype()!=3){
            data.put("hospital",String.valueOf(Utils.hospitals.get(hospital-1).getId()));
        }else{
            data.put("tel",rb_tel.getText().toString());
        }

        enrollProcess(data);
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

    private void enrollProcess(Map<String,String> data){
        progress.show();
        StringRequest sr = new StringRequest(Request.Method.POST, Routes.ADD_BR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.hide();
                Utils.refresPreviousState=true;
                onBackPressed();
                Toast.makeText(RequestBlood.this,"Process Completed Successfully.", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                progress.hide();
                Toast.makeText(RequestBlood.this, "Server Error, Please try again", Toast.LENGTH_SHORT).show();
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