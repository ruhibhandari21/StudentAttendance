package com.quagnitia.studentattendance.admin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.quagnitia.studentattendance.R;
import com.quagnitia.studentattendance.Services.AppConstants;
import com.quagnitia.studentattendance.Services.OnTaskCompleted;
import com.quagnitia.studentattendance.Services.WebService;
import com.quagnitia.studentattendance.adapters.ViewAllClassAdapter;
import com.quagnitia.studentattendance.adapters.ViewAllSubjectAdapter;
import com.quagnitia.studentattendance.adapters.ViewAllTeachersAdapter;
import com.quagnitia.studentattendance.models.GetAllClass;
import com.quagnitia.studentattendance.models.GetAllSubjects;
import com.quagnitia.studentattendance.models.GetAllTeachers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewAllTeachersActivity extends AppCompatActivity implements View.OnClickListener,OnTaskCompleted{
    private RecyclerView recycler_view_teachers;
    private ViewAllTeachersAdapter viewAllTeachersAdapter;
    private ImageView img_back;
    private Context mContext;
    private TextView tv_no_records;
    private int pos=-1;
    List<GetAllTeachers> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0,0);
        setContentView(R.layout.activity_view_all_teachers);
        mContext=this;
        initUI();
        initListener();
        callGetAllTeacherWS();
    }

    public void initUI()
    {
        recycler_view_teachers=(RecyclerView)findViewById(R.id.recycler_view_teachers);
        tv_no_records=(TextView)findViewById(R.id.tv_no_records);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view_teachers.setLayoutManager(mLayoutManager);
        recycler_view_teachers.setItemAnimator(new DefaultItemAnimator());
        img_back=(ImageView)findViewById(R.id.img_back);
        img_back = (ImageView) findViewById(R.id.img_back);
    }

    public void initListener()
    {
        img_back.setOnClickListener(this);
    }

    public void callGetAllTeacherWS()
    {
        new WebService(this,this,null,"getAllTeachers").execute(AppConstants.BASE_URL+AppConstants.GET_ALL_TEACHER);
    }

    public void callDeleteTeacherWS(int pos)
    {
        this.pos=pos;
        HashMap hashMap=new HashMap();
        hashMap.put("UserId",list.get(pos).getUserid());
        new WebService(this,this,hashMap,"deleteTeacher").execute(AppConstants.BASE_URL+AppConstants.DELETE_TEACHER);
    }
    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.img_back:
                finish();
                break;
        }
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
            switch (TAG)
            {
                case "getAllTeachers":
                    try
                    {
                        JSONArray jsonArray=new JSONArray(jsonObject.optString("teachers"));
                        if(jsonArray.length()!=0)
                        {

                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1=jsonArray.optJSONObject(i);
                                GetAllTeachers getAllTeachers=new GetAllTeachers();
                                getAllTeachers.setName(jsonObject1.optString("Fullname"));
                                getAllTeachers.setEmailid(jsonObject1.optString("EmailId"));
                                getAllTeachers.setMobileno(jsonObject1.optString("MobileNo"));
                                getAllTeachers.setUsername(jsonObject1.optString("Username"));
                                getAllTeachers.setPassword(jsonObject1.optString("Password"));
                                getAllTeachers.setUserid(jsonObject1.optString("UserId"));

                                list.add(getAllTeachers);
                            }
                            tv_no_records.setVisibility(View.GONE);
                            recycler_view_teachers.setVisibility(View.VISIBLE);
                            viewAllTeachersAdapter=new ViewAllTeachersAdapter(mContext,list);
                            recycler_view_teachers.setAdapter(viewAllTeachersAdapter);
                        }
                        else
                        {
                            tv_no_records.setVisibility(View.VISIBLE);
                            recycler_view_teachers.setVisibility(View.GONE);
                            Toast.makeText(mContext, "No Records Found", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                    break;


                case "deleteTeacher":
                    Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    Intent in=getIntent();
                    startActivity(in);
                    finish();

                    break;


            }
        }
        else
        {
            tv_no_records.setVisibility(View.VISIBLE);
            recycler_view_teachers.setVisibility(View.GONE);
            Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
        }

    }
}
