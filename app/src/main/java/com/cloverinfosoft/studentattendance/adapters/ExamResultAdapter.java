package com.cloverinfosoft.studentattendance.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloverinfosoft.studentattendance.R;
import com.cloverinfosoft.studentattendance.models.GetAllResult;

import java.util.List;

/**
 * Created by admin on 2/16/2018.
 */

public class ExamResultAdapter extends RecyclerView.Adapter<ExamResultAdapter.MyViewHolder> {
    private List<GetAllResult> classList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_subject_name,tv_max_marks,tv_min_marks,tv_marks_obtained,tv_remark;

        public MyViewHolder(View view) {
            super(view);
            tv_subject_name=(TextView)view.findViewById(R.id.tv_subject_name);
            tv_max_marks=(TextView)view.findViewById(R.id.tv_max_marks);
            tv_min_marks=(TextView)view.findViewById(R.id.tv_min_marks);
            tv_marks_obtained=(TextView)view.findViewById(R.id.tv_marks_obtained);
            tv_remark=(TextView)view.findViewById(R.id.tv_remark);
        }
    }


    public ExamResultAdapter(Context mContext, List<GetAllResult> classList) {
        this.mContext = mContext;
        this.classList = classList;
    }

    @Override
    public ExamResultAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.lay_exam_result_display, parent, false);
        return new ExamResultAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ExamResultAdapter.MyViewHolder holder, final int position) {
        GetAllResult getAllClass = classList.get(position);
        holder.tv_subject_name.setText(getAllClass.getSubjectName());
        holder.tv_max_marks.setText(getAllClass.getMaxMarks());
        holder.tv_min_marks.setText(getAllClass.getMinMarks());
        holder.tv_marks_obtained.setText(getAllClass.getMarksObtained());
        holder.tv_remark.setText(getAllClass.getRemark());
        if(getAllClass.getRemark().equals("Pass"))
        {
            holder.tv_remark.setTextColor(mContext.getResources().getColor(android.R.color.holo_green_dark));
        }
        else
        {
            holder.tv_remark.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_dark));
        }

    }


    @Override
    public int getItemCount() {
        return classList.size();
    }
}
