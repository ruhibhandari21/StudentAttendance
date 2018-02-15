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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.quagnitia.studentattendance.R;
import com.quagnitia.studentattendance.Services.WebService;
import com.quagnitia.studentattendance.admin.AddClassActivity;
import com.quagnitia.studentattendance.admin.AddStudentActivity;
import com.quagnitia.studentattendance.admin.ViewAllClassesActivity;
import com.quagnitia.studentattendance.admin.ViewAllStudentActivity;
import com.quagnitia.studentattendance.models.GetAllStudentByClassName;
import com.quagnitia.studentattendance.models.GetAllStudents;

import java.util.List;

/**
 * Created by Admin on 10-02-2018.
 */

public class ViewAllStudentByClassNameAdapter extends RecyclerView.Adapter<ViewAllStudentByClassNameAdapter.MyViewHolder> {

    private List<GetAllStudentByClassName> studentsList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_classname;
        private CheckBox chk_present,chk_absent;

        public MyViewHolder(View view) {
            super(view);
            tv_classname = (TextView) view.findViewById(R.id.tv_class_code);
            chk_present=(CheckBox) view.findViewById(R.id.chk_present);
            chk_absent=(CheckBox)view.findViewById(R.id.chk_absent);
        }
    }


    public ViewAllStudentByClassNameAdapter(Context mContext,List<GetAllStudentByClassName> studentsList) {
        this.mContext=mContext;
        this.studentsList = studentsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.lay_studentbyclasssname_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        GetAllStudentByClassName getAllStudents = studentsList.get(position);
        holder.tv_classname.setText(getAllStudents.getFullname());

        if(studentsList.get(position).getPresent().equals("1"))
        {
            holder.chk_present.setChecked(true);
        }
        else
        {
            holder.chk_present.setChecked(false);
        }

        if(studentsList.get(position).getAbsent().equals("1"))
        {
            holder.chk_absent.setChecked(true);
        }
        else
        {
            holder.chk_absent.setChecked(false);
        }


        holder.chk_present.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                {
                    holder.chk_present.setChecked(false);
                    studentsList.get(position).setPresent("0");
                }
                else
                {
                    holder.chk_present.setChecked(true);
                    studentsList.get(position).setPresent("1");
                }
                notifyDataSetChanged();
            }
        });

        holder.chk_absent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                {
                    holder.chk_absent.setChecked(false);
                    studentsList.get(position).setAbsent("0");
                }
                else
                {
                    holder.chk_absent.setChecked(true);
                    studentsList.get(position).setAbsent("1");
                }
                notifyDataSetChanged();
            }
        });



    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }
}
