package com.quagnitia.studentattendance.teacher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.quagnitia.studentattendance.R;
import com.quagnitia.studentattendance.Services.AppConstants;
import com.quagnitia.studentattendance.Services.OnTaskCompleted;
import com.quagnitia.studentattendance.Services.WebService;
import com.quagnitia.studentattendance.admin.ViewAllStudentActivity;
import com.quagnitia.studentattendance.models.GetAllSubjects;
import com.quagnitia.studentattendance.utils.PreferencesManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddHomeworkActivity extends AppCompatActivity implements View.OnClickListener, OnTaskCompleted, AdapterView.OnItemSelectedListener {

    private Spinner sp_class, sp_subject;
    private EditText edt_description;
    private Context mContext;
    private Button btn_cancel, btn_register;
    private TextView tv_view_all;
    private ImageView img_back;
    private Intent intent;
    private List<String> list = new ArrayList<>();
    private PreferencesManager preferencesManager;
    private List<GetAllSubjects> listAllSubjects = new ArrayList<>();
    private List<String> listAllSubjects1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        preferencesManager=PreferencesManager.getInstance(this);
        setContentView(R.layout.activity_add_homework);
        initUI();
        initListener();
        callGetAllClassWS();
    }


    public void initUI() {
        sp_class = (Spinner) findViewById(R.id.sp_class);
        sp_subject = (Spinner) findViewById(R.id.sp_subject);
        edt_description = (EditText) findViewById(R.id.edt_description);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_register = (Button) findViewById(R.id.btn_register);
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_view_all = (TextView) findViewById(R.id.tv_view_all);
    }


    public void callGetAllClassWS() {
        new WebService(mContext, this, null, "getAllClass").execute(AppConstants.BASE_URL + AppConstants.GET_ALL_CLASS);
    }

    public void callGetAllSubjectsWS() {
        new WebService(this, this, null, "getAllSubjects").execute(AppConstants.BASE_URL + AppConstants.GET_ALL_SUBJECTS);
    }

    public void callAddHomeWorkWS()
    {
        if(edt_description.getText().toString().equals("")||
                sp_class.getSelectedItem().toString().equals("")||
                sp_subject.getSelectedItem().toString().equals("")
                )
        {
            Toast.makeText(mContext, "please fill in the details", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String,String> hashMap=new HashMap<>();
            hashMap.put("TeacherUserId",preferencesManager.getUserId());
            hashMap.put("Classname",sp_class.getSelectedItem().toString());
            hashMap.put("SubjectName",sp_subject.getSelectedItem().toString());
            hashMap.put("Description",edt_description.getText().toString());
            new WebService(this, this, hashMap, "addHomework").execute(AppConstants.BASE_URL + AppConstants.ADD_HOMEWORK);
        }

    }

    public void initListener() {
        sp_class.setOnItemSelectedListener(this);
        sp_subject.setOnItemSelectedListener(this);
        btn_register.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        img_back.setOnClickListener(this);
        tv_view_all.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_register:
                callAddHomeWorkWS();
                break;
            case R.id.tv_view_all:
                intent = new Intent(mContext, ViewAllHomeworkActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onTaskCompleted(JSONObject jsonObject, String result, String TAG) throws Exception {
        if (result.equals("")) {
            Toast.makeText(mContext, "No Response From Server", Toast.LENGTH_SHORT).show();
            return;
        }
        if (jsonObject.optBoolean("success")) {
            switch (TAG) {
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
                            callGetAllSubjectsWS();
                        } else {
                            Toast.makeText(mContext, "No Records Found", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;


                case "getAllSubjects":
                    try {
                        JSONArray jsonArray = new JSONArray(jsonObject.optString("subjects"));
                        if (jsonArray.length() != 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                                GetAllSubjects getAllSubjects = new GetAllSubjects();
                                getAllSubjects.setSubjectcode(jsonObject1.optString("SubjectAbbrevation"));
                                getAllSubjects.setSubjectname(jsonObject1.optString("SubjectName"));
                                listAllSubjects.add(getAllSubjects);
                                listAllSubjects1.add(jsonObject1.optString("SubjectName"));
                            }
                            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listAllSubjects1);
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sp_subject.setAdapter(aa);

                        } else {
                            Toast.makeText(mContext, "No Records Found", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case "addHomework":
                    edt_description.setText("");
                    Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    break;


            }
        } else {
            Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
        }


    }

}
