package com.quagnitia.studentattendance.admin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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

public class AddSubjectActivity extends AppCompatActivity implements View.OnClickListener,OnTaskCompleted{

    private RecyclerView recyclerView;
    private Button btn_cancel,btn_save;
    private TextView tv_view_all;
    private ImageView img_back;
    private EditText edt_subject_name,edt_subject_code;
    private boolean isEdit=false;
    private Context mContext;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        mContext=this;
        initUI();
        initListener();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if(getIntent()!=null && getIntent().hasExtra("SubjectName")&& isEdit==false)
        {
            isEdit=true;
            btn_save.setText("Update");
            edt_subject_name.setText(getIntent().getStringExtra("SubjectName"));
            edt_subject_code.setText(getIntent().getStringExtra("SubjectAbbrevation"));
        }
        else
        {
            btn_save.setText("Save");
            isEdit=false;
        }

    }


    public void initUI()
    {
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        img_back=(ImageView)findViewById(R.id.img_back);
        btn_cancel=(Button)findViewById(R.id.btn_cancel);
        btn_save=(Button)findViewById(R.id.btn_save);
        tv_view_all=(TextView)findViewById(R.id.tv_view_all);
        edt_subject_name=(EditText)findViewById(R.id.edt_subject_name);
        edt_subject_code=(EditText)findViewById(R.id.edt_subject_code);
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
                    callEditSubjectWS();
                }
                else
                {
                    callAddSubjectWS();
                }

                break;
            case R.id.tv_view_all:
                intent=new Intent(mContext,ViewAllSubjectsActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void callAddSubjectWS()
    {
        HashMap hashMap=new HashMap();
        hashMap.put("SubjectName",edt_subject_name.getText().toString());
        hashMap.put("SubjectAbbrevation",edt_subject_code.getText().toString());
        new WebService(this,this,hashMap,"addSubject").execute(AppConstants.BASE_URL+AppConstants.ADD_SUBJECT);
    }


    public void callEditSubjectWS()
    {
        HashMap hashMap=new HashMap();
        hashMap.put("PreviousAbbrevation",getIntent().getStringExtra("SubjectAbbrevation"));
        hashMap.put("SubjectName",edt_subject_name.getText().toString());
        hashMap.put("SubjectAbbrevation",edt_subject_code.getText().toString());
        new WebService(this,this,hashMap,"addSubject").execute(AppConstants.BASE_URL+AppConstants.EDIT_SUBJECT);
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
                case "addSubject":
                    isEdit=false;
                    Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    edt_subject_name.setText("");
                    edt_subject_code.setText("");
                    break;

                case "editSubject":
                    isEdit=false;
                    edt_subject_name.setText("");
                    edt_subject_code.setText("");
                    Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    break;
            }
            btn_save.setText("Save");
        }
        else
            Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();

    }



}
