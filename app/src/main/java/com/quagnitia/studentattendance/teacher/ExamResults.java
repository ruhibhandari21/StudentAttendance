package com.quagnitia.studentattendance.teacher;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.quagnitia.studentattendance.R;
import com.quagnitia.studentattendance.Services.AppConstants;
import com.quagnitia.studentattendance.Services.OnTaskCompleted;
import com.quagnitia.studentattendance.Services.WebService;
import com.quagnitia.studentattendance.models.GetAllStudentByClassName;
import com.quagnitia.studentattendance.utils.PreferencesManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ExamResults extends AppCompatActivity implements OnTaskCompleted, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private ImageView img_back;
    private Context mContext;
    private Spinner sp_student;
    private List<GetAllStudentByClassName> studentList = new ArrayList<>();
    private List<String> studentList1 = new ArrayList<>();
    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_results);
        preferencesManager = PreferencesManager.getInstance(this);
        mContext = ExamResults.this;
        initUI();
        initListener();
        callGetAllStudentWS();
    }

    public void initUI() {
        img_back = (ImageView) findViewById(R.id.img_back);
        sp_student = (Spinner) findViewById(R.id.sp_student);


    }

    public void initListener() {
        img_back.setOnClickListener(this);
        sp_student.setOnItemSelectedListener(this);

    }

    public void callGetAllStudentWS() {
        new WebService(this, this, null, "getAllStudentByClassname").execute(AppConstants.BASE_URL + AppConstants.GET_ALL_STUDENT);
    }

    String studentUserId = "";

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        studentUserId = studentList.get(position).getUserid();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;

        }
    }

    @Override
    public void onTaskCompleted(JSONObject jsonObject, String result, String TAG) throws Exception {
        if (result.equals("")) {
            Toast.makeText(mContext, "No Response From Server", Toast.LENGTH_SHORT).show();
            return;
        }
        if (jsonObject.optBoolean("success")) {
            switch (TAG) {
                case "getAllStudentByClassname":
                    try {
                        JSONArray jsonArray = new JSONArray(jsonObject.optString("students"));
                        if (jsonArray.length() != 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                                GetAllStudentByClassName getAllStudents = new GetAllStudentByClassName();
                                getAllStudents.setUserid(jsonObject1.optString("UserId"));
                                getAllStudents.setFullname(jsonObject1.optString("StudentFullname"));
                                getAllStudents.setAdmission_no(jsonObject1.optString("AdmissionNo"));
                                getAllStudents.setContactno(jsonObject1.optString("MobileNo"));
                                getAllStudents.setEmailid(jsonObject1.optString("EmailId"));
                                getAllStudents.setFatherfullname(jsonObject1.optString("FatherFullname"));
                                getAllStudents.setMotherfullname(jsonObject1.optString("MotherFullname"));
                                getAllStudents.setDateofregistration(jsonObject1.optString("DateOfReg"));
                                getAllStudents.setFeeeffectivefrom(jsonObject1.optString("FeeEffectiveFrom"));
                                getAllStudents.setGender(jsonObject1.optString("Gender"));
                                getAllStudents.setClassname(jsonObject1.optString("Classname"));
                                getAllStudents.setUsername(jsonObject1.optString("Username"));
                                getAllStudents.setPassword(jsonObject1.optString("Password"));
                                getAllStudents.setPresent(jsonObject1.optString("Present"));
                                getAllStudents.setAbsent(jsonObject1.optString("Absent"));
                                studentList.add(getAllStudents);
                                studentList1.add(jsonObject1.optString("StudentFullname"));
                            }
                            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, studentList1);
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sp_student.setAdapter(aa);

                        } else {
                            Toast.makeText(mContext, "No Records Found", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;


            }
        } else {
            Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
        }


    }

}
