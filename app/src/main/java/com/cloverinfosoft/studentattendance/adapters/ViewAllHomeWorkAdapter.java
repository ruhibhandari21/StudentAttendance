package com.cloverinfosoft.studentattendance.adapters;

/**
 * Created by admin on 2/16/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloverinfosoft.studentattendance.R;
import com.cloverinfosoft.studentattendance.models.GetAllHomework;
import com.cloverinfosoft.studentattendance.teacher.ViewAllHomeworkActivity;
import com.cloverinfosoft.studentattendance.utils.PreferencesManager;

import java.util.List;


/**
 * Created by Admin on 10-02-2018.
 */

public class ViewAllHomeWorkAdapter extends RecyclerView.Adapter<ViewAllHomeWorkAdapter.MyViewHolder> {

    private List<GetAllHomework> classList;
    private Context mContext;
    private PreferencesManager preferencesManager;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_classname, tv_enter, tv_subject_name, tv_examtype, tv_max_marks, tv_exam_date;
        private ImageView img_edit, img_delete;

        public MyViewHolder(View view) {
            super(view);
            tv_classname = (TextView) view.findViewById(R.id.tv_class_code);
            tv_subject_name = (TextView) view.findViewById(R.id.tv_subject_name);
            tv_examtype = (TextView) view.findViewById(R.id.tv_exam_type);
            tv_max_marks = (TextView) view.findViewById(R.id.tv_max_marks);
            tv_exam_date = (TextView) view.findViewById(R.id.tv_exam_date);
            tv_enter = (TextView) view.findViewById(R.id.tv_enter);
            img_edit = (ImageView) view.findViewById(R.id.img_edit);
            img_delete = (ImageView) view.findViewById(R.id.img_delete);
            preferencesManager=PreferencesManager.getInstance(mContext);
        }
    }


    public ViewAllHomeWorkAdapter(Context mContext, List<GetAllHomework> classList) {
        this.mContext = mContext;
        this.classList = classList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.lay_viewallexams_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final GetAllHomework getAllClass = classList.get(position);
        holder.tv_classname.setText(getAllClass.getDescription());
        holder.tv_subject_name.setText(getAllClass.getSubect());
        holder.tv_examtype.setText(getAllClass.getClassname());
        holder.tv_exam_date.setVisibility(View.GONE);
        holder.tv_max_marks.setVisibility(View.GONE);
        holder.tv_enter.setVisibility(View.GONE);


        if(preferencesManager.getRole().equals("3"))
        {
            holder.img_delete.setVisibility(View.INVISIBLE);
            holder.img_delete.setEnabled(false);
        }
        else
        {
            holder.img_delete.setVisibility(View.VISIBLE);
            holder.img_delete.setEnabled(true);
        }


        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewAllHomeworkActivity) mContext).callDeleteClassWS(position);
            }
        });


    }


    @Override
    public int getItemCount() {
        return classList.size();
    }
}

