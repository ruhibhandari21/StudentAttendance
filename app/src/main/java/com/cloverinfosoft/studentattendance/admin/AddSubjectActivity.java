package com.cloverinfosoft.studentattendance.admin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cloverinfosoft.studentattendance.R;
import com.cloverinfosoft.studentattendance.Services.AppConstants;
import com.cloverinfosoft.studentattendance.Services.OnTaskCompleted;
import com.cloverinfosoft.studentattendance.Services.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddSubjectActivity extends AppCompatActivity implements OnTaskCompleted, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Button btn_cancel,btn_save;
    private TextView tv_view_all;
    private ImageView img_back;
    private EditText edt_subject_name,edt_subject_code;
    private boolean isEdit=false;
    private Context mContext;
    private Intent intent;
    private LinearLayout ll_sp_class;
    private Spinner sp_class;
    private List<String> list = new ArrayList<>();

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
            ll_sp_class.setVisibility(View.GONE);

            btn_save.setText("Update");
            edt_subject_name.setText(getIntent().getStringExtra("SubjectName"));
            edt_subject_code.setText(getIntent().getStringExtra("SubjectAbbrevation"));
        }
        else
        {
            ll_sp_class.setVisibility(View.VISIBLE);
            btn_save.setText("Save");
            isEdit=false;
        }
        callGetAllClassWS();

    }

    public void callGetAllClassWS() {
        new WebService(mContext, this, null, "getAllClass").execute(AppConstants.BASE_URL + AppConstants.GET_ALL_CLASS);
    }
    public void initUI()
    {
        ll_sp_class=(LinearLayout)findViewById(R.id.ll_sp_class);
        sp_class = (Spinner) findViewById(R.id.sp_class);
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
        hashMap.put("Classname",sp_class.getSelectedItem().toString());
        new WebService(this,this,hashMap,"addSubject").execute(AppConstants.BASE_URL+AppConstants.ADD_SUBJECT);
    }


    public void callEditSubjectWS()
    {
        HashMap hashMap=new HashMap();
        hashMap.put("PreviousAbbrevation",getIntent().getStringExtra("SubjectAbbrevation"));
        hashMap.put("SubjectName",edt_subject_name.getText().toString());
        hashMap.put("SubjectAbbrevation",edt_subject_code.getText().toString());
        new WebService(this,this,hashMap,"editSubject").execute(AppConstants.BASE_URL+AppConstants.EDIT_SUBJECT);
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
                case "getAllClass":
                    try {
                        JSONArray jsonArray = new JSONArray(jsonObject.optString("classes"));
                        if (jsonArray.length() != 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                                list.add(jsonObject1.optString("ClassAbbrevation"));
                            }
                            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sp_class.setAdapter(aa);
                        } else {
                            Toast.makeText(mContext, "No Records Found", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case "addSubject":
                    isEdit=false;
                    Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    edt_subject_name.setText("");
                    edt_subject_code.setText("");
                    btn_save.setText("Save");
                    break;

                case "editSubject":
                    isEdit=false;
                    edt_subject_name.setText("");
                    edt_subject_code.setText("");
                    btn_save.setText("Save");
                    Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    break;
            }

        }
        else
            Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
