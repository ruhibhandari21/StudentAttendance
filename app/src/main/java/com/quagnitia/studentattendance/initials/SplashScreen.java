package com.quagnitia.studentattendance.initials;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.quagnitia.studentattendance.DashboardActivity;
import com.quagnitia.studentattendance.R;
import com.quagnitia.studentattendance.Services.AppConstants;
import com.quagnitia.studentattendance.utils.PreferencesManager;

public class SplashScreen extends AppCompatActivity {
    private PreferencesManager preferencesManager;
    private Context mContext;
    int flag = 0;
    int permission = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        preferencesManager=PreferencesManager.getInstance(this);
        mContext=SplashScreen.this;

        accessPermission();
    }
    public void accessPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(SplashScreen.this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(SplashScreen.this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            LayoutInflater lf = (LayoutInflater) this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogview = lf.inflate(R.layout.access_permission_dialog, null);
            Button ok_btn = (Button) dialogview.findViewById(R.id.ok_btn);
            Button cancel_btn = (Button) dialogview.findViewById(R.id.cancel_btn);
            ok_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag = 1;

                    dialog.dismiss();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                                , Manifest.permission.ACCESS_COARSE_LOCATION
                                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                                , Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.RECORD_AUDIO
                                , Manifest.permission.CAMERA
                        }, AppConstants.MY_APP_PERMISSIONS);


                    }

                }
            });
            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flag = 0;
                    Toast.makeText(mContext, "You have cancelled giving permission to the app.So app cannot be accessed without system permissions.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    finish();
                }
            });
            dialog.setContentView(dialogview);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(lp);
            dialog.show();

        } else {
            initUI();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == AppConstants.MY_APP_PERMISSIONS) {

            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //permission not granted
                    permission = PackageManager.PERMISSION_DENIED;
                    break;

                } else if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                    permission = PackageManager.PERMISSION_GRANTED;

                }
            }
            if (permission == PackageManager.PERMISSION_GRANTED) {
                initUI();
            } else if (permission == PackageManager.PERMISSION_DENIED) {
                finish();
            }


        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                    final Intent mainIntent = new Intent(SplashScreen.this, DashboardActivity.class);
                    SplashScreen.this.startActivity(mainIntent);
                    SplashScreen.this.finish();
                }


            }
        }, 5000);
    }

}
