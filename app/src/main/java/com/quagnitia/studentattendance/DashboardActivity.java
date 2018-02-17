package com.quagnitia.studentattendance;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.quagnitia.studentattendance.admin.AdminHomeFragment;
import com.quagnitia.studentattendance.student.StudentHomeFragment;
import com.quagnitia.studentattendance.teacher.TeacherHomeFragment;
import com.quagnitia.studentattendance.utils.PreferencesManager;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView img_menu;
    private Context mContext;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private String CURRENTFRAGMENT = "";
    private Fragment fragment;
    private PreferencesManager preferencesManager;

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
            callSetupFragment(SCREENS.ADMIN_HOME, null);
        }
        else if(preferencesManager.getRole().equals("2"))
        {
            callSetupFragment(SCREENS.TEACHER_HOME, null);
        }
        else if(preferencesManager.getRole().equals("3"))
        {
            callSetupFragment(SCREENS.STUDENT_HOME, null);
        }

    }

    public void initUI() {
        img_menu = (ImageView) findViewById(R.id.img_menu);
    }

    public void initListener() {
        img_menu.setOnClickListener(this);
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
            case R.id.img_menu:
                break;
        }

    }
}
