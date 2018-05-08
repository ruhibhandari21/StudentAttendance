package com.cloverinfosoft.studentattendance.admin;

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

import com.cloverinfosoft.studentattendance.R;
import com.cloverinfosoft.studentattendance.Services.AppConstants;
import com.cloverinfosoft.studentattendance.Services.OnTaskCompleted;
import com.cloverinfosoft.studentattendance.Services.WebService;
import com.cloverinfosoft.studentattendance.adapters.ViewAllStudentsAdapter;
import com.cloverinfosoft.studentattendance.models.GetAllStudents;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewAllStudentActivity extends AppCompatActivity implements View.OnClickListener,OnTaskCompleted {
    private RecyclerView recycler_view_student;
    private ImageView img_back;
    private ViewAllStudentsAdapter viewAllStudentsAdapter;
    private Context mContext;
    private int pos=-1;
    private TextView tv_no_records;
    List<GetAllStudents> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0,0);
        setContentView(R.layout.activity_view_all_student);
        mContext=this;
        initUI();
        initListener();
        callGetAllStudentWS();
    }
    public void initUI()
    {
        tv_no_records=(TextView)findViewById(R.id.tv_no_records);
        recycler_view_student=(RecyclerView)findViewById(R.id.recycler_view_student);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view_student.setLayoutManager(mLayoutManager);
        recycler_view_student.setItemAnimator(new DefaultItemAnimator());
        img_back=(ImageView)findViewById(R.id.img_back);
    }

    public void initListener()
    {
        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.img_back:
                finish();
                break;
        }
    }

    public void callGetAllStudentWS()
    {
        new WebService(this,this,null,"getAllStudent").execute(AppConstants.BASE_URL+AppConstants.GET_ALL_STUDENT);
    }

    public void callDeleteStudentWS(int pos)
    {
        this.pos=pos;
        HashMap hashMap=new HashMap();
        hashMap.put("UserId",list.get(pos).getUserid());
        new WebService(this,this,hashMap,"deleteStudent").execute(AppConstants.BASE_URL+AppConstants.DELETE_STUDENT);
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
                case "getAllStudent":
                    try
                    {
                        JSONArray jsonArray=new JSONArray(jsonObject.optString("students"));
                        if(jsonArray.length()!=0)
                        {

                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1=jsonArray.optJSONObject(i);
                                GetAllStudents getAllStudents=new GetAllStudents();
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
                                list.add(getAllStudents);
                            }
                            tv_no_records.setVisibility(View.GONE);
                            recycler_view_student.setVisibility(View.VISIBLE);
                            viewAllStudentsAdapter=new ViewAllStudentsAdapter(mContext,list);
                            recycler_view_student.setAdapter(viewAllStudentsAdapter);
                        }
                        else
                        {
                            tv_no_records.setVisibility(View.VISIBLE);
                            recycler_view_student.setVisibility(View.GONE);
                            Toast.makeText(mContext, "No Records Found", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                    break;


                case "deleteStudent":
                    Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    Intent in=getIntent();
                    startActivity(in);
                    finish();
//                        list.remove(pos);
//                        viewAllClassAdapter=new ViewAllClassAdapter(mContext,list);
//                        viewAllClassAdapter.notifyDataSetChanged();

                    break;


            }
        }
        else
        {
            tv_no_records.setVisibility(View.VISIBLE);
            recycler_view_student.setVisibility(View.GONE);
            Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
        }

    }
}

