package com.quagnitia.studentattendance.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.quagnitia.studentattendance.R;
import com.quagnitia.studentattendance.Services.WebService;
import com.quagnitia.studentattendance.admin.AddClassActivity;
import com.quagnitia.studentattendance.admin.AddStudentActivity;
import com.quagnitia.studentattendance.admin.ViewAllClassesActivity;
import com.quagnitia.studentattendance.admin.ViewAllStudentActivity;
import com.quagnitia.studentattendance.models.GetAllStudents;

import java.util.List;

/**
 * Created by Admin on 10-02-2018.
 */

public class ViewAllStudentsAdapter extends RecyclerView.Adapter<ViewAllStudentsAdapter.MyViewHolder> {

    private List<GetAllStudents> studentsList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_classname;
        private ImageView img_edit,img_delete;

        public MyViewHolder(View view) {
            super(view);
            tv_classname = (TextView) view.findViewById(R.id.tv_class_code);
            img_edit=(ImageView)view.findViewById(R.id.img_edit);
            img_delete=(ImageView)view.findViewById(R.id.img_delete);
        }
    }


    public ViewAllStudentsAdapter(Context mContext,List<GetAllStudents> studentsList) {
        this.mContext=mContext;
        this.studentsList = studentsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.lay_class_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        GetAllStudents getAllStudents = studentsList.get(position);
        holder.tv_classname.setText(getAllStudents.getFullname()+"\n"+getAllStudents.getAdmission_no()+"\n"+getAllStudents.getClassname());
        holder.img_edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext, AddStudentActivity.class);
                i.putExtra("UserId",studentsList.get(position).getUserid());
                i.putExtra("AdmissionNo",studentsList.get(position).getAdmission_no());
                i.putExtra("Classname",studentsList.get(position).getClassname());
                i.putExtra("Gender",studentsList.get(position).getGender());
                i.putExtra("DateOfReg",studentsList.get(position).getDateofregistration());
                i.putExtra("FeeEffectiveFrom",studentsList.get(position).getFeeeffectivefrom());
                i.putExtra("StudentFullname",studentsList.get(position).getFullname());
                i.putExtra("FatherFullname",studentsList.get(position).getFatherfullname());
                i.putExtra("MotherFullname",studentsList.get(position).getMotherfullname());
                i.putExtra("EmailId",studentsList.get(position).getEmailid());
                i.putExtra("MobileNo",studentsList.get(position).getContactno());
                i.putExtra("Username",studentsList.get(position).getUsername());
                i.putExtra("Password",studentsList.get(position).getPassword());
                mContext.startActivity(i);
                ((ViewAllStudentActivity)mContext).finish();
            }
        });
        holder.img_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((ViewAllStudentActivity)mContext).callDeleteStudentWS(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return studentsList.size();
    }
}
