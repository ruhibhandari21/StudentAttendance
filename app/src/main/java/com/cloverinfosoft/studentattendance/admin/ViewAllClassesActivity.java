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
import com.cloverinfosoft.studentattendance.adapters.ViewAllClassAdapter;
import com.cloverinfosoft.studentattendance.models.GetAllClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewAllClassesActivity extends AppCompatActivity implements View.OnClickListener , OnTaskCompleted{

    private RecyclerView recycler_view_classes;
    private ImageView img_back;
    private ViewAllClassAdapter viewAllClassAdapter;
    private Context mContext;
    private int pos=-1;
    private TextView tv_no_records;
    List<GetAllClass> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0,0);
        setContentView(R.layout.activity_view_all_classes);
        mContext=this;
        initUI();
        initListener();
        callGetAllClassWS();
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

    public void callGetAllClassWS()
    {
        new WebService(this,this,null,"getAllClass").execute(AppConstants.BASE_URL+AppConstants.GET_ALL_CLASS);
    }

    public void callDeleteClassWS(int pos)
    {
        this.pos=pos;
        HashMap hashMap=new HashMap();
        hashMap.put("ClassName",list.get(pos).getClassname());
        hashMap.put("ClassAbbrevation",list.get(pos).getClassabbre());
        new WebService(this,this,hashMap,"deleteClass").execute(AppConstants.BASE_URL+AppConstants.DELETE_CLASS);
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
                case "getAllClass":
                    try
                    {
                        JSONArray jsonArray=new JSONArray(jsonObject.optString("classes"));
                        if(jsonArray.length()!=0)
                        {

                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1=jsonArray.optJSONObject(i);
                                GetAllClass getAllClass=new GetAllClass();
                                getAllClass.setClassabbre(jsonObject1.optString("ClassAbbrevation"));
                                getAllClass.setClassname(jsonObject1.optString("ClassName"));
                                list.add(getAllClass);
                            }
                            tv_no_records.setVisibility(View.GONE);
                            recycler_view_classes.setVisibility(View.VISIBLE);
                            viewAllClassAdapter=new ViewAllClassAdapter(mContext,list);
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


                case "deleteClass":
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
            recycler_view_classes.setVisibility(View.GONE);
            Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
        }

    }
}
