package com.quagnitia.studentattendance.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.quagnitia.studentattendance.R;
import com.quagnitia.studentattendance.Services.AppConstants;
import com.quagnitia.studentattendance.Services.OnTaskCompleted;
import com.quagnitia.studentattendance.Services.WebService;

import org.json.JSONObject;

import java.util.HashMap;

public class AddTeacherActivity extends AppCompatActivity implements View.OnClickListener,OnTaskCompleted {

    private ImageView img_back;
    private TextView tv_view_all;
    private Button btn_cancel, btn_register;
    private Intent intent;
    private Context mContext;
    private int pos=-1;
    private String UserId="";
    private EditText edt_username,edt_password,edt_emailid,edt_full_name,edt_mobileno;
    private boolean isEdit=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);
        mContext=AddTeacherActivity.this;
        initUI();
        initListener();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if(getIntent()!=null && getIntent().hasExtra("Fullname")&& isEdit==false)
        {
            isEdit=true;
            btn_register.setText("Update");
            UserId=getIntent().getStringExtra("UserId");
            edt_full_name.setText(getIntent().getStringExtra("Fullname"));
            edt_emailid.setText(getIntent().getStringExtra("EmailId"));
            edt_mobileno.setText(getIntent().getStringExtra("MobileNo"));
            edt_username.setText(getIntent().getStringExtra("Username"));
            edt_password.setText(getIntent().getStringExtra("Password"));
        }
        else
        {
            btn_register.setText("Register");
            UserId="";
            isEdit=false;
        }
    }

    public void initUI() {
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_view_all = (TextView) findViewById(R.id.tv_view_all);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_register = (Button) findViewById(R.id.btn_register);
        edt_username=(EditText) findViewById(R.id.edt_username);
        edt_password=(EditText) findViewById(R.id.edt_password);
        edt_emailid=(EditText) findViewById(R.id.edt_email_id);
        edt_full_name=(EditText) findViewById(R.id.edt_full_name);
        edt_mobileno=(EditText) findViewById(R.id.edt_mobileno);
    }

    public void initListener() {
        img_back.setOnClickListener(this);
        tv_view_all.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }


    public void callAddTeacherWS()
    {
        HashMap hashMap=new HashMap();
        hashMap.put("Fullname",edt_full_name.getText().toString());
        hashMap.put("EmailId",edt_emailid.getText().toString());
        hashMap.put("MobileNo",edt_mobileno.getText().toString());
        hashMap.put("Username",edt_username.getText().toString());
        hashMap.put("Password",edt_password.getText().toString());
        new WebService(this,this,hashMap,"addTeacher").execute(AppConstants.BASE_URL+AppConstants.ADD_TEACHER);
    }


    public void callEditTeacherWS()
    {
        HashMap hashMap=new HashMap();
        hashMap.put("UserId",UserId);
        hashMap.put("Fullname",edt_full_name.getText().toString());
        hashMap.put("EmailId",edt_emailid.getText().toString());
        hashMap.put("MobileNo",edt_mobileno.getText().toString());
        hashMap.put("Username",edt_username.getText().toString());
        hashMap.put("Password",edt_password.getText().toString());
        new WebService(this,this,hashMap,"editTeacher").execute(AppConstants.BASE_URL+AppConstants.EDIT_TEACHER);
    }




    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.btn_cancel:
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_register:
                if(isEdit)
                {
                    callEditTeacherWS();
                }
                else
                {
                    callAddTeacherWS();
                }

                break;
            case R.id.tv_view_all:
                intent=new Intent(mContext,ViewAllTeachersActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }


    @Override
    public void onTaskCompleted(JSONObject jsonObject, String result, String TAG) throws Exception {

        if(result.equals(""))
        {
            Toast.makeText(mContext, "No Response From Server", Toast.LENGTH_SHORT).show();
            return;
        }

        if(jsonObject.optBoolean("success"))
        {
            switch(TAG)
            {
                case "addTeacher":
                    isEdit=false;
                    UserId="";
                    Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    edt_password.setText("");
                    edt_username.setText("");
                    edt_mobileno.setText("");
                    edt_emailid.setText("");
                    edt_full_name.setText("");
                    break;

                case "editTeacher":
                    isEdit=false;
                    UserId="";
                    edt_password.setText("");
                    edt_username.setText("");
                    edt_mobileno.setText("");
                    edt_emailid.setText("");
                    edt_full_name.setText("");
                    Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    break;
            }
            btn_register.setText("Register");
        }
        else
            Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();

    }

}
