package com.cloverinfosoft.studentattendance.teacher;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cloverinfosoft.studentattendance.R;
import com.cloverinfosoft.studentattendance.Services.AppConstants;
import com.cloverinfosoft.studentattendance.Services.OnTaskCompleted;
import com.cloverinfosoft.studentattendance.Services.WebService;
import com.cloverinfosoft.studentattendance.adapters.ViewAllStudentByClassNameAdapter;
import com.cloverinfosoft.studentattendance.models.GetAllStudentByClassName;
import com.cloverinfosoft.studentattendance.utils.PreferencesManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/*
* API integration of sms gateway
*
* http://sms.ganeshcable.net/api/mt/SendSMS?user=dnet&password=9404899992&senderid=DNETBB&channel=trans&DCS=0&flashsms=0&number=917038234410,919579795761&text=test%20message%20sms%20service&route=10
*
* Response
* This XML file does not appear to have any style information associated with it. The document tree is shown below.
<SmsResponse xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
<ErrorCode>000</ErrorCode>
<ErrorMessage>Done</ErrorMessage>
<JobId>11178</JobId>
<MessageData>
<Messages>
<Number>917038234410</Number>
<MessageId>5de6Luwx7Uu8YqCrY8K0HA</MessageId>
</Messages>
<Messages>
<Number>919579795761</Number>
<MessageId>KjwdiRyTQUW7etcV4D1q3A</MessageId>
</Messages>
</MessageData>
</SmsResponse>
*
*
*
*
*
* */


public class AttendanceEntryActivity extends AppCompatActivity implements View.OnClickListener, OnTaskCompleted, AdapterView.OnItemSelectedListener {

    private Button btn_save, btn_cancel;
    private ImageView img_back;
    private Context mContext;
    private TextView tv_current_date;
    private RecyclerView recycler_view;
    private PreferencesManager preferencesManager;
    private TextView tv_no_records;
    private List<String> list = new ArrayList<>();
    private List<GetAllStudentByClassName> studentList = new ArrayList<>();
    private Spinner sp_class;
    private ViewAllStudentByClassNameAdapter viewAllStudentByClassNameAdapter;
    private String selectedStudentList = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_entry);
        preferencesManager = PreferencesManager.getInstance(this);
        mContext = this;
        initUI();
        initListener();
        callGetAllClassWS();
    }

    public void initUI() {
        tv_no_records = (TextView) findViewById(R.id.tv_no_records);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        btn_save = (Button) findViewById(R.id.btn_save);
        tv_current_date = (TextView) findViewById(R.id.tv_current_date);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        img_back = (ImageView) findViewById(R.id.img_back);
        sp_class = (Spinner) findViewById(R.id.sp_class);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        tv_current_date.setText("Attendance For Current Date:" + formattedDate);
    }


    public void initListener() {
        btn_save.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        img_back.setOnClickListener(this);
        sp_class.setOnItemSelectedListener(this);
    }

    public void callGetAllClassWS() {
        new WebService(mContext, this, null, "getAllClass").execute(AppConstants.BASE_URL + AppConstants.GET_CLASS_DETAILS + "?classname=" + preferencesManager.getClassname());
    }

    public void callGetAllStudentWS(String classabbrevation) {
        HashMap hashMap = new HashMap();
        hashMap.put("Classname", classabbrevation);
        new WebService(this, this, hashMap, "getAllStudentByClassname").execute(AppConstants.BASE_URL + AppConstants.GET_ALL_STUDENT_BY_CLASSNAME);
    }

    public void callUpdateAllStudentWS() {
        HashMap hashMap = new HashMap();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        hashMap.put("CurrentDate", formattedDate);
        hashMap.put("Classname", sp_class.getSelectedItem().toString());
        hashMap.put("StudentList", selectedStudentList);
        new WebService(this, this, hashMap, "updateStudentByClassname").execute(AppConstants.BASE_URL + AppConstants.UPDATE_STUDENT_BY_CLASSNAME);
    }
    String numberstr="",message="";

    HashMap<Integer,GetAllStudentByClassName> hashMapPresent=new HashMap<>();
    HashMap<Integer,GetAllStudentByClassName> hashMapAbsent=new HashMap<>();
    int j=0,k=0;


    public void callGetAllAttendanceEntry() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        new WebService(this, this, null, "getAllAttendanceEntry").execute
                (AppConstants.BASE_URL + AppConstants.GET_ALL_ATTENDANCE_ENTRY + "?CurrentDate=" + formattedDate + "&Classname=" + sp_class.getSelectedItem().toString());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                String s = "";
                try {

                    JSONArray jsonArray = new JSONArray();
                    for (int i = 0; i < studentList.size(); i++) {

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("UserId", studentList.get(i).getUserid());
                        jsonObject.put("Present", studentList.get(i).getPresent());
                        jsonObject.put("Absent", studentList.get(i).getAbsent());
                        jsonArray.put(jsonObject);

                    }
                    selectedStudentList = jsonArray.toString();
                    callUpdateAllStudentWS();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.btn_cancel:
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
                case "getAllClass":
                    try {
                        JSONArray jsonArray = new JSONArray(jsonObject.optString("classes"));
                        if (jsonArray.length() != 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                                list.add(jsonObject1.optString("ClassAbbrevation"));
                            }
                            tv_no_records.setVisibility(View.GONE);
                            recycler_view.setVisibility(View.VISIBLE);
                            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sp_class.setAdapter(aa);
                            // callGetAllStudentWS(sp_class.getSelectedItem().toString());

                        } else {
                            tv_no_records.setVisibility(View.VISIBLE);
                            recycler_view.setVisibility(View.GONE);
                            Toast.makeText(mContext, "No Records Found", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case "updateStudentByClassname":
                    selectedStudentList = "";

                    finish();

                    break;




                case "getAllAttendanceEntry":
                    try {
                        JSONArray jsonArray = new JSONArray(jsonObject.optString("students"));
                        if (jsonArray.length() != 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                                for (int j = 0; j < studentList.size(); j++) {
                                    if (jsonObject1.optString("UserId").equals(studentList.get(j).getUserid())) {
                                        studentList.get(j).setPresent(jsonObject1.optString("Present"));
                                        studentList.get(j).setAbsent(jsonObject1.optString("Absent"));
                                    }
                                }

                            }
                            viewAllStudentByClassNameAdapter.notifyDataSetChanged();

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

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
                            }
                            tv_no_records.setVisibility(View.GONE);
                            recycler_view.setVisibility(View.VISIBLE);
                            viewAllStudentByClassNameAdapter = new ViewAllStudentByClassNameAdapter(mContext, studentList);
                            recycler_view.setAdapter(viewAllStudentByClassNameAdapter);

                        } else {
                            tv_no_records.setVisibility(View.VISIBLE);
                            recycler_view.setVisibility(View.GONE);
                            Toast.makeText(mContext, "No Records Found", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    callGetAllAttendanceEntry();

                    break;


            }
        } else {
            if (TAG.equals("getAllAttendanceEntry")) {
                if (studentList.size() != 0) {
                    tv_no_records.setVisibility(View.GONE);
                    recycler_view.setVisibility(View.VISIBLE);
                } else {
                    tv_no_records.setVisibility(View.VISIBLE);
                    recycler_view.setVisibility(View.GONE);
                    Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                }
            } else {
                tv_no_records.setVisibility(View.VISIBLE);
                recycler_view.setVisibility(View.GONE);
                Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        if (studentList != null)
            studentList.clear();
        callGetAllStudentWS(list.get(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    public void updateAttendanceStudentList(List<GetAllStudentByClassName> studentList) {
        this.studentList = studentList;
    }


}
