package com.cloverinfosoft.studentattendance.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.cloverinfosoft.studentattendance.R;

public class StudentListingActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_listing);
        initUI();
        initListener();
    }

    public void initUI()
    {
         img_back = (ImageView) findViewById(R.id.img_back);
    }

    public void initListener()
    {
        img_back.setOnClickListener(this);
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
}
