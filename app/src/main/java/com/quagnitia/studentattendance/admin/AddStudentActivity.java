package com.quagnitia.studentattendance.admin;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.quagnitia.studentattendance.R;
import com.quagnitia.studentattendance.Services.AppConstants;
import com.quagnitia.studentattendance.Services.OnTaskCompleted;
import com.quagnitia.studentattendance.Services.WebService;
import com.quagnitia.studentattendance.adapters.ViewAllClassAdapter;
import com.quagnitia.studentattendance.models.GetAllClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddStudentActivity extends AppCompatActivity implements View.OnClickListener, OnTaskCompleted, AdapterView.OnItemSelectedListener {

    private Button btn_cancel, btn_register;
    private TextView tv_view_all;
    private ImageView img_back;
    private Intent intent;
    private Context mContext;
    private Spinner sp_class, sp_gender;
    private TextView edt_date_of_reg,edt_feeeffect;
    private EditText edt_admissiono,
            edt_sfl, edt_ffl, edt_mfl, edt_mobileno, edt_username, edt_password, edt_emailid;
    private boolean isEdit = false;
    private String UserId = "";
    private String classname = "", gender = "";
    private List<GetAllClass> listClasses = new ArrayList<>();
    private List<String> list = new ArrayList<>();
    private List<String> ll = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        mContext = AddStudentActivity.this;
        ll.add("Male");
        ll.add("Female");
        initUI();
        initListener();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        callGetAllClassWS();
        if (getIntent() != null && getIntent().hasExtra("AdmissionNo") && isEdit == false) {
            isEdit = true;
            UserId = getIntent().getStringExtra("UserId");
            edt_admissiono.setText(getIntent().getStringExtra("AdmissionNo"));
            edt_date_of_reg.setText(getIntent().getStringExtra("DateOfReg"));
            edt_feeeffect.setText(getIntent().getStringExtra("FeeEffectiveFrom"));
            edt_sfl.setText(getIntent().getStringExtra("StudentFullname"));
            edt_ffl.setText(getIntent().getStringExtra("FatherFullname"));
            edt_mfl.setText(getIntent().getStringExtra("MotherFullname"));
            edt_mobileno.setText(getIntent().getStringExtra("MobileNo"));
            edt_username.setText(getIntent().getStringExtra("Username"));
            edt_password.setText(getIntent().getStringExtra("Password"));
            edt_emailid.setText(getIntent().getStringExtra("EmailId"));
            if (list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).equalsIgnoreCase(getIntent().getStringExtra("Classname"))) {
                        sp_class.setSelection(i);
                        break;
                    }
                }
            }
            if(ll.get(0).equals(getIntent().getStringExtra("Gender")))
            {
                sp_gender.setSelection(0);
            }
            else
            {
                sp_gender.setSelection(1);
            }

            btn_register.setText("Update");
        } else {
            btn_register.setText("Save");
            isEdit = false;

        }


    }

    public void initUI() {
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_register = (Button) findViewById(R.id.btn_register);
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_view_all = (TextView) findViewById(R.id.tv_view_all);
        edt_admissiono = (EditText) findViewById(R.id.edt_admission_no);
        edt_date_of_reg = (TextView) findViewById(R.id.tv_date_of_registration);
        edt_feeeffect = (TextView) findViewById(R.id.tv_fee_effective);
        edt_sfl = (EditText) findViewById(R.id.edt_student_name);
        edt_ffl = (EditText) findViewById(R.id.edt_father_fullname);
        edt_mfl = (EditText) findViewById(R.id.edt_mothers_fullname);
        edt_mobileno = (EditText) findViewById(R.id.edt_contact_no);
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password = (EditText) findViewById(R.id.edt_password);
        edt_emailid = (EditText) findViewById(R.id.edt_emailid);
        sp_class = (Spinner) findViewById(R.id.sp_class);
        sp_gender = (Spinner) findViewById(R.id.spinner_gender);


        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ll);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_gender.setAdapter(aa);

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                edt_date_of_reg.setText(sdf.format(myCalendar.getTime()));
            }

        };


        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                edt_feeeffect.setText(sdf.format(myCalendar.getTime()));
            }

        };

        edt_feeeffect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(mContext, date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        edt_date_of_reg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(mContext, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



    }

    public void callGetAllClassWS() {
        new WebService(this, this, null, "getAllClass").execute(AppConstants.BASE_URL + AppConstants.GET_ALL_CLASS);
    }

    public void callAddStudentWS() {
        HashMap hashMap = new HashMap();
        hashMap.put("AdmissionNo", edt_admissiono.getText().toString());
        hashMap.put("Classname", sp_class.getSelectedItem().toString());
        hashMap.put("Gender", sp_gender.getSelectedItem().toString());
        hashMap.put("DateOfReg", edt_date_of_reg.getText().toString());
        hashMap.put("FeeEffectiveFrom", edt_feeeffect.getText().toString());
        hashMap.put("StudentFullname", edt_sfl.getText().toString());
        hashMap.put("FatherFullname", edt_ffl.getText().toString());
        hashMap.put("MotherFullname", edt_mfl.getText().toString());
        hashMap.put("EmailId", edt_emailid.getText().toString());
        hashMap.put("MobileNo", edt_mobileno.getText().toString());
        hashMap.put("Username", edt_username.getText().toString());
        hashMap.put("Password", edt_password.getText().toString());
        hashMap.put("Present","0");
        hashMap.put("Absent","0");

        new WebService(this, this, hashMap, "addStudent").execute(AppConstants.BASE_URL + AppConstants.ADD_STUDENT);
    }


    public void callEditStudentWS() {
        HashMap hashMap = new HashMap();
        hashMap.put("UserId", UserId);
        hashMap.put("AdmissionNo", edt_admissiono.getText().toString().trim());
        hashMap.put("Classname", sp_class.getSelectedItem().toString());
        hashMap.put("Gender", sp_gender.getSelectedItem().toString());
        hashMap.put("DateOfReg", edt_date_of_reg.getText().toString());
        hashMap.put("FeeEffectiveFrom", edt_feeeffect.getText().toString());
        hashMap.put("StudentFullname", edt_sfl.getText().toString());
        hashMap.put("FatherFullname", edt_ffl.getText().toString());
        hashMap.put("MotherFullname", edt_mfl.getText().toString());
        hashMap.put("EmailId", edt_emailid.getText().toString());
        hashMap.put("MobileNo", edt_mobileno.getText().toString());
        hashMap.put("Username", edt_username.getText().toString());
        hashMap.put("Password", edt_password.getText().toString());
        hashMap.put("Present","0");
        hashMap.put("Absent","0");
        new WebService(this, this, hashMap, "editStudent").execute(AppConstants.BASE_URL + AppConstants.EDIT_STUDENT);
    }


    public void initListener() {
        btn_register.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        img_back.setOnClickListener(this);
        tv_view_all.setOnClickListener(this);
        sp_class.setOnItemSelectedListener(this);
        sp_gender.setOnItemSelectedListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_register:
                if (isEdit) {
                    callEditStudentWS();
                } else {
                    callAddStudentWS();
                }

                break;
            case R.id.tv_view_all:
                intent = new Intent(mContext, ViewAllStudentActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onTaskCompleted(JSONObject jsonObject, String result, String TAG) throws Exception {
        if (result.equals("")) {
            Toast.makeText(mContext, "No Response From Server", Toast.LENGTH_SHORT).show();
            return;
        }
        if (jsonObject.optBoolean("success")) {
            switch (TAG) {
                case "getAllClass":
                    try {
                        JSONArray jsonArray = new JSONArray(jsonObject.optString("classes"));
                        if (jsonArray.length() != 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                                list.add(jsonObject1.optString("ClassAbbrevation"));
                            }

                            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
                            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sp_class.setAdapter(aa);
                        } else {
                            Toast.makeText(mContext, "No Records Found", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;


                case "addStudent":
                    isEdit = false;
                    UserId = "";
                    Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    edt_password.setText("");
                    edt_username.setText("");
                    edt_mobileno.setText("");
                    edt_emailid.setText("");
                    edt_date_of_reg.setText("");
                    edt_admissiono.setText("");
                    edt_mfl.setText("");
                    edt_ffl.setText("");
                    edt_sfl.setText("");
                    edt_feeeffect.setText("");
                    edt_emailid.setText("");
                    break;


                case "editStudent":
                    isEdit = false;
                    UserId = "";
                    Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    edt_password.setText("");
                    edt_username.setText("");
                    edt_mobileno.setText("");
                    edt_emailid.setText("");
                    edt_date_of_reg.setText("");
                    edt_admissiono.setText("");
                    edt_mfl.setText("");
                    edt_ffl.setText("");
                    edt_sfl.setText("");
                    edt_feeeffect.setText("");
                    edt_emailid.setText("");

                    break;


            }
            btn_register.setText("Register");
        } else {
            Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

        if (arg1.getId() == R.id.sp_class) {
            classname = list.get(position);
        } else if (arg1.getId() == R.id.spinner_gender) {
            gender = ll.get(position);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

}
