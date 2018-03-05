package com.quagnitia.studentattendance.teacher;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.quagnitia.studentattendance.R;
import com.quagnitia.studentattendance.Services.AppConstants;
import com.quagnitia.studentattendance.Services.OnTaskCompleted;
import com.quagnitia.studentattendance.Services.WebService;
import com.quagnitia.studentattendance.adapters.ViewAllStudentByClassNameAdapter;
import com.quagnitia.studentattendance.models.GellAllAttendanceEntry;
import com.quagnitia.studentattendance.models.GetAllStudentByClassName;
import com.quagnitia.studentattendance.utils.PreferencesManager;
import com.stacktips.view.CustomCalendarView;
import com.stacktips.view.DayDecorator;
import com.stacktips.view.DayView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AttendanceReportActivity extends AppCompatActivity implements OnTaskCompleted, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Spinner sp_class, sp_student;
    private CustomCalendarView calendarView;
    private Button btn_save, btn_cancel;
    private ImageView img_back;
    private Context mContext;
    private TextView tv_current_date;
    private RecyclerView recycler_view;
    private TextView tv_no_records;
    private PreferencesManager preferencesManager;
    private List<String> list = new ArrayList<>();
    private List<GetAllStudentByClassName> studentList = new ArrayList<>();
    private List<String> studentList1 = new ArrayList<>();
    private List<GellAllAttendanceEntry> listall = new ArrayList<>();
    private ViewAllStudentByClassNameAdapter viewAllStudentByClassNameAdapter;
    private String selectedStudentList = "";
    Calendar currentCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_report);
        preferencesManager = PreferencesManager.getInstance(this);
        mContext = this;
        initUI();
        initListener();
        callGetAllClassWS();
    }

    public void initUI() {
        sp_class = (Spinner) findViewById(R.id.sp_class);
        sp_student = (Spinner) findViewById(R.id.sp_student);
        calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);
        currentCalendar = Calendar.getInstance(Locale.getDefault());
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        calendarView.setShowOverflowDate(false);
        calendarView.refreshCalendar(currentCalendar);

        img_back = (ImageView) findViewById(R.id.img_back);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
    }

    public void initListener() {
        sp_class.setOnItemSelectedListener(this);
        sp_student.setOnItemSelectedListener(this);
        img_back.setOnClickListener(this);
    }

    public void callGetAllClassWS() {
        new WebService(mContext, this, null, "getAllClass").execute(AppConstants.BASE_URL + AppConstants.GET_CLASS_DETAILS + "?classname=" + preferencesManager.getClassname());
    }

    public void callGetAllStudentWS(String classabbrevation) {
        HashMap hashMap = new HashMap();
        hashMap.put("Classname", classabbrevation);
        new WebService(this, this, hashMap, "getAllStudentByClassname").execute(AppConstants.BASE_URL + AppConstants.GET_ALL_STUDENT_BY_CLASSNAME);
    }

    public void callGetAllAttendanceEntry() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        String userid = "";
        for (int i = 0; i < studentList.size(); i++) {
            if (sp_student.getSelectedItem().toString().equals(studentList.get(i).getFullname())) {
                userid = studentList.get(i).getUserid();
                break;
            }
        }

        new WebService(this, this, null, "getAllAttendanceEntry").execute
                (AppConstants.BASE_URL + AppConstants.GET_ALL_ATTENDANCE_ENTRY_PERMONTH + "?Classname=" + sp_class.getSelectedItem().toString() + "&UserId=" + userid);
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
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
//        if (studentList != null)
//            studentList.clear();
        if (arg0.getId() == R.id.sp_class) {
            callGetAllStudentWS(list.get(position));
        } else if (arg0.getId() == R.id.sp_student) {
            callGetAllAttendanceEntry();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

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

                        } else {
                            Toast.makeText(mContext, "No Records Found", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;


                case "getAllAttendanceEntry":
                    try {
                        JSONArray jsonArray = new JSONArray(jsonObject.optString("students"));
                        if (jsonArray.length() != 0) {
                            if (listall != null)
                                listall.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                                GellAllAttendanceEntry getAllAttendanceEntry = new GellAllAttendanceEntry();
                                getAllAttendanceEntry.setCurrentDate(jsonObject1.optString("CurrentDate"));
                                getAllAttendanceEntry.setClassname(jsonObject1.optString("Classname"));
                                getAllAttendanceEntry.setUserid(jsonObject1.optString("UserId"));
                                getAllAttendanceEntry.setPresent(jsonObject1.optString("Present"));
                                getAllAttendanceEntry.setAbsent(jsonObject1.optString("Absent"));
                                listall.add(getAllAttendanceEntry);
                            }

                            //call decorator
                            List<DayDecorator> decorators = new ArrayList<>();
                            decorators.add(new ColorDecorator());
                            calendarView.setDecorators(decorators);
                            calendarView.refreshCalendar(currentCalendar);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case "getAllStudentByClassname":
                    try {
                        if (studentList1 != null)
                            studentList1.clear();

                        JSONArray jsonArray = new JSONArray(jsonObject.optString("students"));
                        if (jsonArray.length() != 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                                GetAllStudentByClassName getAllStudents = new GetAllStudentByClassName();
                                if(preferencesManager.getUserId().equals(jsonObject1.optString("UserId")))
                                {
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
                    callGetAllAttendanceEntry();

                    break;


            }
        } else {
            Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
        }


    }


    private class ColorDecorator implements DayDecorator {

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        long fromMilli = 0, toMilli = 0, cellMilli = 0;
        String colorCode;

        public ColorDecorator() {

        }

        @Override
        public void decorate(DayView cell) {
            try {


                String cellDate = formatter.format(cell.getDate());

                Date c = cell.getDate();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c);

                for (int i = 0; i < listall.size(); i++) {
                    if (formattedDate.equals(listall.get(i).getCurrentDate()) && listall.get(i).getPresent().equals("1")) {
                        cell.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                    } else if (formattedDate.equals(listall.get(i).getCurrentDate()) && listall.get(i).getAbsent().equals("1")) {
                        cell.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                    } else if (formattedDate.equals(listall.get(i).getCurrentDate()) && listall.get(i).getPresent().equals("0") && listall.get(i).getAbsent().equals("0")) {
                        cell.setBackgroundColor(getResources().getColor(android.R.color.white));
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

}
