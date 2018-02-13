package com.quagnitia.studentattendance.initials;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.quagnitia.studentattendance.DashboardActivity;
import com.quagnitia.studentattendance.R;
import com.quagnitia.studentattendance.Services.AppConstants;
import com.quagnitia.studentattendance.Services.OnTaskCompleted;
import com.quagnitia.studentattendance.Services.WebService;
import com.quagnitia.studentattendance.utils.PreferencesManager;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,OnTaskCompleted{

    private Button btn_login;
    private Intent intent;
    private Context mContext;
    private TextView tv_forgotpass,tv_title;
    private EditText edt_emailid,edt_password;
    private int role=0;
    private PreferencesManager preferencesManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferencesManager=PreferencesManager.getInstance(this);
        mContext=LoginActivity.this;
        initUI();
        initListener();
        if(getIntent()!=null)
        {
            if(getIntent().getIntExtra("user_role",0)==1)
            {
                role=1;
                tv_title.setText("Admin Login");
            }
            else if(getIntent().getIntExtra("user_role",0)==2)
            {
                role=2;
                tv_title.setText("Teacher Login");
            }
            else if(getIntent().getIntExtra("user_role",0)==3)
            {
                role=3;
                tv_title.setText("Student Login");
            }
        }
    }

    public void initUI()
    {
        tv_forgotpass=(TextView)findViewById(R.id.tv_forgotpass);
        btn_login=(Button)findViewById(R.id.btn_login);
        tv_title=(TextView)findViewById(R.id.tv_title);
        edt_emailid=(EditText)findViewById(R.id.edt_emailid);
        edt_password=(EditText)findViewById(R.id.edt_password);
    }


    public void callLoginWS()
    {
        HashMap hashMap=new HashMap();
        hashMap.put("Username",edt_emailid.getText().toString());
        hashMap.put("UserPassword",edt_password.getText().toString());
        if(role==1)
        {
            hashMap.put("Role","admin");
        }
        else if(role==2)
        {
            hashMap.put("Role","teacher");
        }
        else if(role==3)
        {
            hashMap.put("Role","student");
        }


        new WebService(this,this,hashMap,"LoginWS").execute(AppConstants.BASE_URL+AppConstants.LOGIN);
    }



    public void initListener()
    {
        tv_forgotpass.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.tv_forgotpass:
                intent=new Intent(mContext,ForgotPassword.class);
                startActivity(intent);
                break;
            case R.id.btn_login:
                callLoginWS();
//                intent=new Intent(mContext,DashboardActivity.class);
//                startActivity(intent);
//                finish();
                break;
        }
    }

    @Override
    public void onTaskCompleted(JSONObject jsonObject, String result, String TAG) throws Exception {

        if(jsonObject.optBoolean("success"))
        {
            switch(TAG)
            {
                case "LoginWS":
                    preferencesManager.setUserId(jsonObject.optString("user_id"));
                    preferencesManager.setUserName(edt_emailid.getText().toString());
                    preferencesManager.setRole(role+"");
                    intent=new Intent(mContext,DashboardActivity.class);
                    intent.putExtra("Role",role+"");
                    startActivity(intent);
                    finish();
                    break;
            }
        }
        else
        {
            Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
        }

    }
}
