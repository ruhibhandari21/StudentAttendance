package com.quagnitia.studentattendance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.quagnitia.studentattendance.Services.AppConstants;
import com.quagnitia.studentattendance.Services.OnTaskCompleted;
import com.quagnitia.studentattendance.Services.WebService;
import com.quagnitia.studentattendance.admin.AdminHomeFragment;
import com.quagnitia.studentattendance.initials.RoleSelectionActivity;
import com.quagnitia.studentattendance.student.StudentHomeFragment;
import com.quagnitia.studentattendance.teacher.TeacherHomeFragment;
import com.quagnitia.studentattendance.utils.PreferencesManager;

import org.json.JSONObject;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener,OnTaskCompleted {

    private ImageView img_menu;
    private Context mContext;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private String CURRENTFRAGMENT = "";
    private Fragment fragment;
    private PreferencesManager preferencesManager;
    private TextView tv_logout,tv_header_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferencesManager=PreferencesManager.getInstance(this);
        mContext = DashboardActivity.this;
        initUI();
        initListener();

        if(preferencesManager.getRole().equals("1"))
        {
            tv_header_title.setText("Admin Management");
            callSetupFragment(SCREENS.ADMIN_HOME, null);

        }
        else if(preferencesManager.getRole().equals("2"))
        {
            tv_header_title.setText("Teacher Management");
            callSetupFragment(SCREENS.TEACHER_HOME, null);
        }
        else if(preferencesManager.getRole().equals("3"))
        {
            tv_header_title.setText("Student Management");
            callSetupFragment(SCREENS.STUDENT_HOME, null);
        }

    }

    public void initUI() {
//        img_menu = (ImageView) findViewById(R.id.img_menu);
        tv_logout=(TextView)findViewById(R.id.tv_logout);
        tv_header_title=(TextView)findViewById(R.id.tv_header_title);
    }

    public void initListener() {
//        img_menu.setOnClickListener(this);
        tv_logout.setOnClickListener(this);
    }

    public void callLogoutWS()
    {
        String role="";
        if(preferencesManager.getRole().equals("1"))
        {
            role="admin";
        }
        else if(preferencesManager.getRole().equals("2"))
        {
            role="teacher";
        }
        else if(preferencesManager.getRole().equals("3"))
        {
            role="student";
        }
        new WebService(this,this,null,"logout").execute(AppConstants.BASE_URL+AppConstants.LOGOUT+"?UserId="+preferencesManager.getUserId()+"&Role="+role);
    }


    @Override
    public void onTaskCompleted(JSONObject jsonObject, String result, String TAG) throws Exception {

        if(result.equals(""))
        {
            Toast.makeText(mContext, "Something went worng.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(jsonObject.optBoolean("success"))
        {
            switch (TAG)
            {
                case "logout":
                    preferencesManager.clearPrefrences();
                    Intent i=new Intent(mContext, RoleSelectionActivity.class);
                    startActivity(i);
                    finish();
                    break;
            }
        }
        else
        {
            Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
        }

    }

    public enum SCREENS {
        ADMIN_HOME,TEACHER_HOME,STUDENT_HOME
    }

    public void callSetupFragment(SCREENS screens, Object data) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        switch (screens) {
            case ADMIN_HOME:
                fragment = AdminHomeFragment.newInstance("", "");
                CURRENTFRAGMENT = SCREENS.ADMIN_HOME.toString();
                break;
            case TEACHER_HOME:
                fragment = TeacherHomeFragment.newInstance("", "");
                CURRENTFRAGMENT = SCREENS.TEACHER_HOME.toString();
                break;
            case STUDENT_HOME:
                fragment = StudentHomeFragment.newInstance("", "");
                CURRENTFRAGMENT = SCREENS.STUDENT_HOME.toString();
                break;
        }
        fragmentTransaction.replace(R.id.fragmentContainer, fragment, CURRENTFRAGMENT);
        fragmentTransaction.commit();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.img_menu:
//                break;
            case R.id.tv_logout:
                callLogoutWS();
                break;
        }

    }
}
