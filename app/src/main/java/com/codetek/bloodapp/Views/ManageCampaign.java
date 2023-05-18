package com.codetek.bloodapp.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageCampaign extends AppCompatActivity implements Validator.ValidationListener{

    private int updateId;

    ImageView manage_campaigns_back_btn;
    private Button campaignSubmit;
    private Validator validator;
    DatePicker campaignDate;
    @NotEmpty
    private EditText campaignName,campaignLng,campaignLtd;
    private EditText campaignInfo;
    private String selectedDate;
    private Date now;

    private ProgressDialog progress;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.codetek.bloodapp.R.layout.activity_manage_campaign);

        campaignName=findViewById(R.id.campaignName);
        campaignInfo=findViewById(R.id.campaignInfo);
        campaignLng=findViewById(R.id.campaignLng);
        campaignLtd=findViewById(R.id.campaignLtd);

        now=new Date();

        validator = new Validator(this);
        validator.setValidationListener(ManageCampaign.this);

        manage_campaigns_back_btn=findViewById(R.id.manage_campaigns_back_btn);
        manage_campaigns_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        campaignSubmit=findViewById(R.id.campaignSubmit);
        campaignSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        campaignDate=findViewById(R.id.campaignDate);
        campaignDate.setMinDate(new Date().getTime());

        updateId=getIntent().getIntExtra("id",0);
        if(updateId!=0){
            String str[] =getIntent().getStringExtra("date").split("-");
            campaignDate.init(Integer.parseInt(str[0]), Integer.parseInt(str[1])-1, Integer.parseInt(str[2]),null);
            selectedDate=getIntent().getStringExtra("date");
            campaignName.setText(getIntent().getStringExtra("name"));
            campaignInfo.setText(getIntent().getStringExtra("info"));
            campaignLng.setText(String.valueOf(getIntent().getDoubleExtra("lng",0)));
            campaignLtd.setText(String.valueOf(getIntent().getDoubleExtra("ltd",0)));
        }

        campaignDate.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                month++;
                selectedDate=year+"/"+String.format("%02d", month)+"/"+day;
                System.out.println(selectedDate);
            }
        });

        queue = Volley.newRequestQueue(this);
        progress=new ProgressDialog(this);
    }

    @Override
    public void onValidationSucceeded() {

        progress.setMessage("Please wait");
        progress.show();

        Map<String,String> data=new HashMap<>();

        data.put("name", campaignName.getText().toString());
        data.put("info", campaignInfo.getText().toString());
        data.put("lng", campaignLng.getText().toString());
        data.put("ltd", campaignLtd.getText().toString());
        data.put("date", selectedDate);

        if(updateId!=0){
            data.put("id",String.valueOf(updateId));
        }

        StringRequest sr = new StringRequest(Request.Method.POST, Routes.ADD_CAMPAIGN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.hide();
                Utils.refresPreviousState=true;
                clearFields();
                onBackPressed();
                Toast.makeText(ManageCampaign.this,"Process Completed Successfully.", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                progress.hide();
                Toast.makeText(ManageCampaign.this, "Server Error, Please try again", Toast.LENGTH_SHORT).show();
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

    private void clearFields(){
        campaignName.setText("");
        campaignInfo.setText("");
        campaignLng.setText("");
        campaignLtd.setText("");
        campaignDate.setMinDate(new Date().getTime());
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
}