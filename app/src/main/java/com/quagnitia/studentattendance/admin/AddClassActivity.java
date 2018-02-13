package com.quagnitia.studentattendance.admin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class AddClassActivity extends AppCompatActivity implements View.OnClickListener,OnTaskCompleted{

    private Button btn_cancel,btn_save;
    private TextView tv_view_all;
    private ImageView img_back;
    private Intent intent;
    private Context mContext;
    private EditText edt_class_name,edt_class_code;
    private boolean isEdit=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        mContext=AddClassActivity.this;
        initUI();
        initListener();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if(getIntent()!=null && getIntent().hasExtra("ClassName")&& isEdit==false)
        {
            isEdit=true;
            edt_class_name.setText(getIntent().getStringExtra("ClassName"));
            edt_class_code.setText(getIntent().getStringExtra("ClassAbbrevation"));
            btn_save.setText("Update");
        }
        else
        {
            btn_save.setText("Save");
            isEdit=false;
        }


    }


    public void initUI()
    {
        btn_cancel=(Button)findViewById(R.id.btn_cancel);
        btn_save=(Button)findViewById(R.id.btn_save);
        tv_view_all=(TextView)findViewById(R.id.tv_view_all);
        img_back=(ImageView)findViewById(R.id.img_back);
        edt_class_name=(EditText)findViewById(R.id.edt_class_name);
        edt_class_name.clearFocus();
        edt_class_code=(EditText)findViewById(R.id.edt_class_code);
        edt_class_code.clearFocus();
    }

    public void initListener()
    {
        btn_cancel.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        tv_view_all.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.btn_cancel:
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_save:
                if(isEdit)
                {
                    callEditClassWS();
                }
                else
                {
                    callAddClassWS();
                }

                break;
            case R.id.tv_view_all:
                intent=new Intent(mContext,ViewAllClassesActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
        public void callAddClassWS()
        {
            HashMap hashMap=new HashMap();
            hashMap.put("ClassName",edt_class_name.getText().toString());
            hashMap.put("ClassAbbrevation",edt_class_code.getText().toString());
            new WebService(this,this,hashMap,"addClass").execute(AppConstants.BASE_URL+AppConstants.ADD_CLASS);
        }


    public void callEditClassWS()
    {
        HashMap hashMap=new HashMap();
        hashMap.put("PreviousAbbrevation",getIntent().getStringExtra("ClassAbbrevation"));
        hashMap.put("ClassName",edt_class_name.getText().toString());
        hashMap.put("ClassAbbrevation",edt_class_code.getText().toString());
        new WebService(this,this,hashMap,"editClass").execute(AppConstants.BASE_URL+AppConstants.EDIT_CLASS);
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
                    case "addClass":
                        isEdit=false;
                        Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                        edt_class_code.setText("");
                        edt_class_name.setText("");
                        break;

                    case "editClass":
                        isEdit=false;
                        edt_class_code.setText("");
                        edt_class_name.setText("");
                        Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                        break;
                }
                btn_save.setText("Save");
            }
            else
                Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();

        }

}
