package com.quagnitia.studentattendance.initials;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.quagnitia.studentattendance.R;

public class RegisterationActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_signup, btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        initUI();
        initListener();
    }

    public void initUI() {
        btn_signup = (Button) findViewById(R.id.btn_signup);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
    }

    public void initListener() {
        btn_signup.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signup:
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }
}
