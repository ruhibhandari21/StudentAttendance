package com.cloverinfosoft.studentattendance.initials;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.cloverinfosoft.studentattendance.R;

public class RoleSelectionActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rel_admin, rel_teacher, rel_student;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);
        mContext = RoleSelectionActivity.this;
        initUI();
        initListener();
    }

    public void initUI() {
        rel_admin = (RelativeLayout) findViewById(R.id.l3);
        rel_teacher = (RelativeLayout) findViewById(R.id.l4);
        rel_student = (RelativeLayout) findViewById(R.id.l6);
    }

    public void initListener() {
        rel_admin.setOnClickListener(this);
        rel_teacher.setOnClickListener(this);
        rel_student.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int role = 0;
        Intent intent;
        switch (v.getId()) {
            case R.id.l3:
                role = 1;//for admin
                break;
            case R.id.l4:
                role = 2;//for teacher
                break;
            case R.id.l6:
                role = 3;//for student
                break;
        }

        intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra("user_role", role);
        startActivity(intent);
        finish();
    }
}
