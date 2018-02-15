package com.quagnitia.studentattendance.teacher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.quagnitia.studentattendance.R;
import com.quagnitia.studentattendance.Services.OnTaskCompleted;

import org.json.JSONObject;

public class AttendanceReportActivity extends AppCompatActivity implements OnTaskCompleted,View.OnClickListener,AdapterView.OnItemSelectedListener{

    private Spinner sp_class,sp_student;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_report);
    }

    public void initUI()
    {
        sp_class=(Spinner)findViewById(R.id.sp_class);
        sp_student=(Spinner)findViewById(R.id.sp_student);
    }

    public void initListener()
    {
        sp_class.setOnItemSelectedListener(this);
        sp_student.setOnItemSelectedListener(this);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onTaskCompleted(JSONObject jsonObject, String result, String TAG) throws Exception {

    }
}
