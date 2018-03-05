package com.quagnitia.studentattendance.teacher;

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
import com.quagnitia.studentattendance.adapters.ViewAllExams;
import com.quagnitia.studentattendance.adapters.ViewAllHomeWorkAdapter;
import com.quagnitia.studentattendance.models.GetAllExams;
import com.quagnitia.studentattendance.models.GetAllHomework;
import com.quagnitia.studentattendance.utils.PreferencesManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewAllHomeworkActivity extends AppCompatActivity implements View.OnClickListener , OnTaskCompleted {
    private RecyclerView recycler_view_classes;
    private ImageView img_back;
    private ViewAllHomeWorkAdapter viewAllClassAdapter;
    private Context mContext;
    private int pos=-1;
    private TextView tv_no_records;
    private PreferencesManager preferencesManager;
    List<GetAllHomework> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0,0);
        setContentView(R.layout.activity_view_all_homework);
        preferencesManager=PreferencesManager.getInstance(this);
        mContext=this;
        initUI();
        initListener();
        callGetAllHomeworkWS();
    }
    public void initUI()
    {
        tv_no_records=(TextView)findViewById(R.id.tv_no_records);
        recycler_view_classes=(RecyclerView)findViewById(R.id.recycler_view_classes);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view_classes.setLayoutManager(mLayoutManager);
        recycler_view_classes.setItemAnimator(new DefaultItemAnimator());
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

    public void callGetAllHomeworkWS()
    {
        new WebService(this,this,null,"getAllHomework").execute(AppConstants.BASE_URL+AppConstants.GET_HOMEWORK_BY_CLASSNAME+"?Classname="+preferencesManager.getClassname());
    }

    public void callDeleteClassWS(int pos)
    {
        this.pos=pos;
        HashMap hashMap=new HashMap();
        hashMap.put("TeacherUserId",list.get(pos).getTeacheruserid());
        hashMap.put("Classname",list.get(pos).getClassname());
        hashMap.put("SubjectName",list.get(pos).getSubect());
        hashMap.put("Description",list.get(pos).getDescription());
        new WebService(this,this,hashMap,"deleteHomework").execute(AppConstants.BASE_URL+AppConstants.DELETE_HOMEWORK);
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
                case "getAllHomework":
                    try
                    {
                        JSONArray jsonArray=new JSONArray(jsonObject.optString("result"));
                        if(jsonArray.length()!=0)
                        {

                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1=jsonArray.optJSONObject(i);
                                GetAllHomework getAllHomework=new GetAllHomework();
                                getAllHomework.setTeacheruserid(jsonObject1.optString("TeacherUserId"));
                                getAllHomework.setClassname(jsonObject1.optString("Classname"));
                                getAllHomework.setSubect(jsonObject1.optString("SubjectName"));
                                getAllHomework.setDescription(jsonObject1.optString("Description"));
                                list.add(getAllHomework);
                            }
                            tv_no_records.setVisibility(View.GONE);
                            recycler_view_classes.setVisibility(View.VISIBLE);
                            viewAllClassAdapter=new ViewAllHomeWorkAdapter(mContext,list);
                            recycler_view_classes.setAdapter(viewAllClassAdapter);
                        }
                        else
                        {
                            tv_no_records.setVisibility(View.VISIBLE);
                            recycler_view_classes.setVisibility(View.GONE);
                            Toast.makeText(mContext, "No Records Found", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                    break;


                case "deleteHomework":
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
            recycler_view_classes.setVisibility(View.GONE);
            Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
        }

    }
}

