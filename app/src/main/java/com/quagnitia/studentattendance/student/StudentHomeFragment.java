package com.quagnitia.studentattendance.student;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.quagnitia.studentattendance.R;
import com.quagnitia.studentattendance.teacher.AddHomeworkActivity;
import com.quagnitia.studentattendance.teacher.AttendanceEntryActivity;
import com.quagnitia.studentattendance.teacher.AttendanceReportActivity;
import com.quagnitia.studentattendance.teacher.ExamResults;
import com.quagnitia.studentattendance.teacher.ViewAllHomeworkActivity;

public class StudentHomeFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private RelativeLayout rel_attendance_report, rel_attendance_entry, rel_add_exam, rel_exam_results, rel_view_timetable, rel_add_homework;
    private Intent intent;
    private Context mContext;


    public StudentHomeFragment() {
        // Required empty public constructor
    }

    public static StudentHomeFragment newInstance(String param1, String param2) {
        StudentHomeFragment fragment = new StudentHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_student_home, container, false);
        mContext = getActivity();
        initUI();
        initListener();
        return view;
    }


    public void initUI() {
        rel_attendance_report = (RelativeLayout) view.findViewById(R.id.rel_attendance_report);
        rel_exam_results = (RelativeLayout) view.findViewById(R.id.rel_exam_results);
        rel_view_timetable = (RelativeLayout) view.findViewById(R.id.rel_view_timetable);
        rel_add_homework = (RelativeLayout) view.findViewById(R.id.rel_add_homework);
    }

    public void initListener() {
        rel_attendance_report.setOnClickListener(this);
        rel_exam_results.setOnClickListener(this);
        rel_view_timetable.setOnClickListener(this);
        rel_add_homework.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rel_attendance_report:
                intent = new Intent(mContext, AttendanceReportActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.rel_attendance_entry:
                intent = new Intent(mContext, AttendanceEntryActivity.class);
                mContext.startActivity(intent);
                break;

            case R.id.rel_exam_results:
                intent = new Intent(mContext, ExamResults.class);
                mContext.startActivity(intent);
                break;

            case R.id.rel_add_homework:
                intent = new Intent(mContext, ViewAllHomeworkActivity.class);
                mContext.startActivity(intent);
                break;
        }
    }
}
