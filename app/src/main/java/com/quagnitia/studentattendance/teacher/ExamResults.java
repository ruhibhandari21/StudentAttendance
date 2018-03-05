package com.quagnitia.studentattendance.teacher;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.quagnitia.studentattendance.adapters.ExamResultAdapter;
import com.quagnitia.studentattendance.models.GetAllResult;
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
    private ExamResultAdapter examResultAdapter;
    private List<GetAllStudentByClassName> studentList = new ArrayList<>();
    private List<String> studentList1 = new ArrayList<>();
    private List<GetAllResult> listgetAllResult=new ArrayList<>();
    private PreferencesManager preferencesManager;
    private RecyclerView recycler_view;

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
        recycler_view=(RecyclerView)findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());

    }

    public void initListener() {
        img_back.setOnClickListener(this);
        sp_student.setOnItemSelectedListener(this);

    }

    public void callGetAllStudentWS() {
        new WebService(this, this, null, "getAllStudentByClassname").execute(AppConstants.BASE_URL + AppConstants.GET_STUDENT_CLASSNAME+"?classname="+preferencesManager.getClassname());
    }

    public void callGetAllStudentMarksWS(int pos) {
        new WebService(this, this, null, "getAllStudentMarks").execute(AppConstants.BASE_URL + AppConstants.GET_STUDENTS_MARKS+"?Classname="+preferencesManager.getClassname()+"&StudentUserId="+studentList.get(pos).getUserid());
    }


    String studentUserId = "";

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        studentUserId = studentList.get(position).getUserid();
        listgetAllResult.clear();
        callGetAllStudentMarksWS(position);
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
                                if(studentList!=null)
                                    studentList.clear();
                                    studentList1.clear();
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

                case "getAllStudentMarks":
                    try {
                        JSONArray jsonArray = new JSONArray(jsonObject.optString("result"));
                        if (jsonArray.length() != 0) {
                            if(listgetAllResult!=null)
                                listgetAllResult.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                                GetAllResult getAllResult = new GetAllResult();
                                getAllResult.setTeacherUserId(jsonObject1.optString("TeacherUserId"));
                                getAllResult.setStudentUserId(jsonObject1.optString("StudentUserId"));
                                getAllResult.setStudentName(jsonObject1.optString("StudentName"));
                                getAllResult.setClassname(jsonObject1.optString("Classname"));
                                getAllResult.setSubjectName(jsonObject1.optString("SubjectName"));
                                getAllResult.setExamDate(jsonObject1.optString("ExamDate"));
                                getAllResult.setExamType(jsonObject1.optString("ExamType"));
                                getAllResult.setMaxMarks(jsonObject1.optString("MaxMarks"));
                                getAllResult.setMarksObtained(jsonObject1.optString("MarksObtained"));
                                getAllResult.setMinMarks(jsonObject1.optString("MinMarks"));

                                float marksObtained=Float.parseFloat(getAllResult.getMarksObtained());
                                float totalmarks=Float.parseFloat(getAllResult.getMaxMarks());
                                float minmarks=Float.parseFloat(getAllResult.getMinMarks());

                                if(marksObtained<minmarks)
                                {
                                    getAllResult.setRemark("Fail");
                                }
                                else
                                {
                                    getAllResult.setRemark("Pass");
                                }


                                listgetAllResult.add(getAllResult);
                            }

                            examResultAdapter=new ExamResultAdapter(mContext,listgetAllResult);
                            recycler_view.setAdapter(examResultAdapter);


                        } else {
                            examResultAdapter=new ExamResultAdapter(mContext,listgetAllResult);
                            recycler_view.setAdapter(examResultAdapter);
                            Toast.makeText(mContext, "No Records Found", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    break;



            }
        } else {
            if(TAG.equals("getAllStudentMarks"))
            {
                examResultAdapter=new ExamResultAdapter(mContext,listgetAllResult);
                recycler_view.setAdapter(examResultAdapter);
            }
            Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
        }


    }

}
