package com.codetek.bloodapp.Views;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codetek.bloodapp.Controllers.AuthController;
import com.codetek.bloodapp.Models.Routes;
import com.codetek.bloodapp.Models.Utils;
import com.codetek.bloodapp.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Register extends AppCompatActivity  implements Validator.ValidationListener, AdapterView.OnItemSelectedListener {

    private TextView register_back_login;
    @NotEmpty
    private EditText register_name,register_tel;
    @NotEmpty
    @Email
    private EditText register_email;
    @Password(min = 6, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS)
    private EditText register_password;
    @ConfirmPassword
    private EditText register_retype_password;

    private Spinner register_district;

    private Spinner register_blood_type;

    private int blood_type=0;
    private int district=0;

    private ArrayAdapter spinnerAdapter1 ;
    private ArrayAdapter spinnerAdapter2 ;

    Button register_button;

    private Validator validator;

    private AuthController authController;

    String [] dataList1;
    String [] dataList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        validator = new Validator(this);
        validator.setValidationListener(Register.this);
        initState();
    }

    private void initState() {
        dataList1=Utils.getBloodTypeNameList();
        dataList2=Utils.getDistrictNameList();

        register_name=findViewById(R.id.register_name);
        register_email=findViewById(R.id.register_email);
        register_password=findViewById(R.id.register_password);
        register_retype_password=findViewById(R.id.register_retype_password);
        register_district=findViewById(R.id.register_district);
        register_blood_type=findViewById(R.id.register_blood_type);
        register_tel=findViewById(R.id.register_tel);

        register_blood_type.setOnItemSelectedListener(Register.this);
        spinnerAdapter1 =new ArrayAdapter<String>(Register.this, android.R.layout.simple_spinner_item, dataList1 );
        spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        register_blood_type.setAdapter(spinnerAdapter1);

        register_district.setOnItemSelectedListener(Register.this);
        spinnerAdapter2 =new ArrayAdapter<String>(Register.this, android.R.layout.simple_spinner_item, dataList2 );
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        register_district.setAdapter(spinnerAdapter2);

        register_button=findViewById(R.id.register_button);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        register_back_login=findViewById(R.id.register_back_login);
        register_back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        authController=new AuthController(Register.this, Routes.REGISTER);
    }

    @Override
    public void onValidationSucceeded() {
        try {
            if(blood_type!=0 && district!=0){
                register_button.setFocusable(true);
                Map<String,String> registerForm= new HashMap<String, String>();
                registerForm.put("name",register_name.getText().toString());
                registerForm.put("email",register_email.getText().toString());
                registerForm.put("tel",register_tel.getText().toString());
                registerForm.put("password",register_password.getText().toString());
                registerForm.put("district",String.valueOf(district));
                registerForm.put("bloodtype",String.valueOf(blood_type));

                authController.doRegister(registerForm);
                register_name.setText("");
                register_tel.setText("");
                register_email.setText("");
                register_password.setText("");
                register_retype_password.setText("");
            }else{
                Toast.makeText(this, "Please select all fields", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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
        if(adapterView.getId()==register_blood_type.getId()){
            blood_type=Utils.getBloodTypeIdList()[i];
        }else{
            district=Utils.getDistrictIdList()[i];
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}