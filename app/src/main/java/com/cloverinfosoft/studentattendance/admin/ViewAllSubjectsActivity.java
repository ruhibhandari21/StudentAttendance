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
import com.cloverinfosoft.studentattendance.adapters.ViewAllSubjectAdapter;
import com.cloverinfosoft.studentattendance.models.GetAllSubjects;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewAllSubjectsActivity extends AppCompatActivity implements View.OnClickListener,OnTaskCompleted{
    private RecyclerView recycler_view_subjects;
    private ViewAllSubjectAdapter viewAllSubjectAdapter;
    private ImageView img_back;
    private Context mContext;
    private TextView tv_no_records;
    private int pos=-1;
    List<GetAllSubjects> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0,0);
        setContentView(R.layout.activity_view_all_subjects);
        mContext=this;
        initUI();
        initListener();
        callGetAllClassWS();
    }


    public void initUI()
    {
        recycler_view_subjects=(RecyclerView)findViewById(R.id.recycler_view_subjects);
        tv_no_records=(TextView)findViewById(R.id.tv_no_records);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view_subjects.setLayoutManager(mLayoutManager);
        recycler_view_subjects.setItemAnimator(new DefaultItemAnimator());
        img_back=(ImageView)findViewById(R.id.img_back);
    }

    public void initListener()
    {
        img_back.setOnClickListener(this);
    }
    public void callGetAllClassWS()
    {
        new WebService(this,this,null,"getAllSubjects").execute(AppConstants.BASE_URL+AppConstants.GET_ALL_SUBJECTS);
    }

    public void callDeleteSubjectWS(int pos)
    {
        this.pos=pos;
        HashMap hashMap=new HashMap();
        hashMap.put("SubjectName",list.get(pos).getSubjectname());
        hashMap.put("SubjectAbbrevation",list.get(pos).getSubjectcode());
        new WebService(this,this,hashMap,"deleteSubject").execute(AppConstants.BASE_URL+AppConstants.DELETE_SUBJECT);
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
                                list.add(getAllSubjects);
                            }
                            tv_no_records.setVisibility(View.GONE);
                            recycler_view_subjects.setVisibility(View.VISIBLE);
                            viewAllSubjectAdapter=new ViewAllSubjectAdapter(mContext,list);
                            recycler_view_subjects.setAdapter(viewAllSubjectAdapter);
                        }
                        else
                        {
                            tv_no_records.setVisibility(View.VISIBLE);
                            recycler_view_subjects.setVisibility(View.GONE);
                            Toast.makeText(mContext, "No Records Found", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                    break;


                case "deleteSubject":
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
            recycler_view_subjects.setVisibility(View.GONE);
            Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
        }

    }
}
