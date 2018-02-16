package com.quagnitia.studentattendance.teacher;

import android.app.DatePickerDialog;
import android.content.Context;
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
import com.quagnitia.studentattendance.models.GetAllStudentByClassName;
import com.quagnitia.studentattendance.utils.PreferencesManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExamMarksEntry extends AppCompatActivity implements OnTaskCompleted, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Button btn_cancel, btn_register;
    private TextView tv_view_all, tv_class, tv_subject, tv_examtype, tv_examdate, tv_max_marks;
    private ImageView img_back;
    private EditText edt_marks,tv_min_marks;
    private Context mContext;
    private Spinner sp_student;
    private List<GetAllStudentByClassName> studentList = new ArrayList<>();
    private List<String> studentList1 = new ArrayList<>();
    private DatePickerDialog.OnDateSetListener date;
    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_marks_entry);
        preferencesManager = PreferencesManager.getInstance(this);
        mContext = ExamMarksEntry.this;
        initUI();
        initListener();
        callGetAllStudentWS(getIntent().getStringExtra("Classname"));
    }

    public void initUI() {
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_register = (Button) findViewById(R.id.btn_register);
        img_back = (ImageView) findViewById(R.id.img_back);
        edt_marks = (EditText) findViewById(R.id.edt_marks_obtained);
        tv_view_all = (TextView) findViewById(R.id.tv_view_all);
        sp_student = (Spinner) findViewById(R.id.sp_student);
        tv_class = (TextView) findViewById(R.id.tv_class);
        tv_subject = (TextView) findViewById(R.id.tv_subject_name);
        tv_examtype = (TextView) findViewById(R.id.tv_exam_type);
        tv_examdate = (TextView) findViewById(R.id.tv_exam_date);
        tv_max_marks = (TextView) findViewById(R.id.tv_max_marks);

        if (getIntent() != null) {
            tv_class.setText(getIntent().getStringExtra("Classname"));
            tv_subject.setText(getIntent().getStringExtra("Subject"));
            tv_examdate.setText(getIntent().getStringExtra("ExamDate"));
            tv_examtype.setText(getIntent().getStringExtra("ExamType"));
            tv_max_marks.setText(getIntent().getStringExtra("MaxMarks"));
        }


    }

    public void initListener() {
        btn_register.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        img_back.setOnClickListener(this);
        tv_view_all.setOnClickListener(this);
        sp_student.setOnItemSelectedListener(this);

    }

    public void callGetAllStudentWS(String classabbrevation) {
        HashMap hashMap = new HashMap();
        hashMap.put("Classname", classabbrevation);
        new WebService(this, this, hashMap, "getAllStudentByClassname").execute(AppConstants.BASE_URL + AppConstants.GET_ALL_STUDENT_BY_CLASSNAME);
    }

    public void callAddStudentMarksWS() {
        if (edt_marks.getText().toString().equals("")) {
            Toast.makeText(mContext, "Please enter the marks", Toast.LENGTH_SHORT).show();
        } else if (Float.parseFloat(edt_marks.getText().toString()) > Float.parseFloat(tv_max_marks.getText().toString())) {
            Toast.makeText(mContext, "Obtained marks cannot be greater than max marks", Toast.LENGTH_SHORT).show();
        } else {
            HashMap hashMap = new HashMap();
            hashMap.put("TeacherUserId", preferencesManager.getUserId());
            hashMap.put("StudentUserId", studentUserId);
            hashMap.put("StudentName", sp_student.getSelectedItem().toString());
            hashMap.put("Classname", tv_class.getText().toString());
            hashMap.put("SubjectName", tv_subject.getText().toString());
            hashMap.put("ExamType", tv_examtype.getText().toString());
            hashMap.put("ExamDate", tv_examdate.getText().toString());
            hashMap.put("MaxMarks", tv_max_marks.getText().toString());
            hashMap.put("MinMarks", getIntent().getStringExtra("MinMarks"));
            hashMap.put("MarksObtained", edt_marks.getText().toString());

            new WebService(this, this, hashMap, "addStudentsMarks").execute(AppConstants.BASE_URL + AppConstants.ADD_STUDENTS_MARKS);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_register:
                callAddStudentMarksWS();
                break;
        }
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

                case "addStudentsMarks":
                    edt_marks.setText("");
                    Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    break;

            }
        } else {
            Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
        }


    }

}
