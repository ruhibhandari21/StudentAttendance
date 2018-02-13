package com.quagnitia.studentattendance.initials;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.quagnitia.studentattendance.DashboardActivity;
import com.quagnitia.studentattendance.R;
import com.quagnitia.studentattendance.utils.PreferencesManager;

public class SplashScreen extends AppCompatActivity {
    private PreferencesManager preferencesManager;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        preferencesManager=PreferencesManager.getInstance(this);
        mContext=SplashScreen.this;
        initUI();
    }

    public void initUI()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(preferencesManager.getUserId().equals(""))
                {
                    final Intent mainIntent = new Intent(SplashScreen.this, RoleSelectionActivity.class);
                    SplashScreen.this.startActivity(mainIntent);
                    SplashScreen.this.finish();
                }
                else if(!preferencesManager.getUserId().equals(""))
                {
                    switch(preferencesManager.getRole())
                    {
                        case "1"://admin
                            final Intent mainIntent = new Intent(SplashScreen.this, DashboardActivity.class);
                            SplashScreen.this.startActivity(mainIntent);
                            SplashScreen.this.finish();
                            break;
                        case "2":
                            break;
                        case "3":
                            break;
                    }
                }


            }
        }, 5000);
    }

}
