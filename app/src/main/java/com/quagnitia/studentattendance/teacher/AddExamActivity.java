package com.quagnitia.studentattendance.teacher;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.quagnitia.studentattendance.R;
import com.quagnitia.studentattendance.Services.AppConstants;
import com.quagnitia.studentattendance.Services.OnTaskCompleted;
import com.quagnitia.studentattendance.Services.WebService;
import com.quagnitia.studentattendance.adapters.ViewAllSubjectAdapter;
import com.quagnitia.studentattendance.admin.ViewAllStudentActivity;
import com.quagnitia.studentattendance.models.GetAllSubjects;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddExamActivity extends AppCompatActivity implements OnTaskCompleted, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Button btn_cancel, btn_register;
    private TextView tv_view_all;
    private ImageView img_back;
    private Intent intent;
    private EditText edt_marks,edt_min_marks;
    private Context mContext;
    private Spinner sp_class, sp_exam_type, sp_subject;
    private boolean isEdit = false;
    private List<String> list = new ArrayList<>();
    private List<String> listExamType = new ArrayList<>();
    private List<GetAllSubjects> listAllSubjects=new ArrayList<>();
    private List<String> listAllSubjects1=new ArrayList<>();
    private TextView tv_exam_date;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exam);
        mContext = AddExamActivity.this;
        listExamType.add("UNIT_TEST");
        listExamType.add("HALF_YEARLY_EXAM");
        listExamType.add("FINAL_EXAM");
        initUI();
        initListener();
        callGetAllClassWS();
    }


    public void initUI() {
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_register = (Button) findViewById(R.id.btn_register);
        img_back = (ImageView) findViewById(R.id.img_back);
        edt_marks=(EditText)findViewById(R.id.edt_marks);
        edt_min_marks=(EditText)findViewById(R.id.edt_marks);
        tv_view_all = (TextView) findViewById(R.id.tv_view_all);
        sp_class = (Spinner) findViewById(R.id.sp_class);
        sp_exam_type = (Spinner) findViewById(R.id.sp_exam_type);
        sp_subject = (Spinner) findViewById(R.id.sp_subject);
        tv_exam_date=(TextView)findViewById(R.id.tv_exam_date);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listExamType);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_exam_type.setAdapter(aa);

          myCalendar = Calendar.getInstance();
          date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                tv_exam_date.setText(sdf.format(myCalendar.getTime()));
            }

        };


    }

    public void initListener() {
        btn_register.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        img_back.setOnClickListener(this);
        tv_view_all.setOnClickListener(this);
        sp_class.setOnItemSelectedListener(this);
        sp_exam_type.setOnItemSelectedListener(this);
        sp_subject.setOnItemSelectedListener(this);
        tv_exam_date.setOnClickListener(this);

    }

    public void callGetAllClassWS() {
        new WebService(mContext, this, null, "getAllClass").execute(AppConstants.BASE_URL + AppConstants.GET_ALL_CLASS);
    }

    public void callGetAllSubjectsWS() {
        new WebService(this,this,null,"getAllSubjects").execute(AppConstants.BASE_URL+AppConstants.GET_ALL_SUBJECTS);
    }


    public void callAddExamWS() {

        if(edt_marks.getText().toString().equals("")||
                sp_class.getSelectedItem().toString().equals("")||
                sp_subject.getSelectedItem().toString().equals("")||
                sp_exam_type.getSelectedItem().toString().equals("")||
                tv_exam_date.getText().equals("")||
                edt_min_marks.getText().equals(""))
        {
            Toast.makeText(mContext, "All fields are mandatory", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String,String> hashMap=new HashMap<>();
            hashMap.put("Classname",sp_class.getSelectedItem().toString());
            hashMap.put("Subject",sp_subject.getSelectedItem().toString());
            hashMap.put("ExamType",sp_exam_type.getSelectedItem().toString());
            hashMap.put("ExamDate",tv_exam_date.getText().toString());
            hashMap.put("MaxMarks",edt_marks.getText().toString());
            hashMap.put("MinMarks",edt_min_marks.getText().toString());
            new WebService(mContext, this, hashMap, "addExam").execute(AppConstants.BASE_URL + AppConstants.ADD_EXAM);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_exam_date:
                new DatePickerDialog(mContext, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.btn_cancel:
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_register:
                callAddExamWS();
                break;
            case R.id.tv_view_all:
                intent = new Intent(mContext, ViewAllExamsActivity.class);
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
                    try
                    {
                        JSONArray jsonArray=new JSONArray(jsonObject.optString("subjects"));
                        if(jsonArray.length()!=0)
                        {

                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1=jsonArray.optJSONObject(i);
                                GetAllSubjects getAllSubjects=new GetAllSubjects();
                                getAllSubjects.setSubjectcode(jsonObject1.optString("SubjectAbbrevation"));
                                getAllSubjects.setSubjectname(jsonObject1.optString("SubjectName"));
                                listAllSubjects.add(getAllSubjects);
                                listAllSubjects1.add(jsonObject1.optString("SubjectName"));
                            }
                            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listAllSubjects1);
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sp_subject.setAdapter(aa);

                        }
                        else
                        {
                            Toast.makeText(mContext, "No Records Found", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                    break;

                case "addExam":
                    Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    intent=getIntent();
                    startActivity(intent);
                    finish();
                    break;


            }
        } else {
            Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
        }


    }

}
