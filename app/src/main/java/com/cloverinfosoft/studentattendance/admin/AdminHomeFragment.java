package com.cloverinfosoft.studentattendance.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.cloverinfosoft.studentattendance.R;

public class AdminHomeFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private RelativeLayout rel_add_student,rel_add_teacher,rel_add_class,rel_add_subject;
    private Intent intent;


    public AdminHomeFragment() {
        // Required empty public constructor
    }

    public static AdminHomeFragment newInstance(String param1, String param2) {
        AdminHomeFragment fragment = new AdminHomeFragment();
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
        view=inflater.inflate(R.layout.fragment_admin_home, container, false);
        initUI();
        initListener();
        return view;
    }


    public void initUI()
    {
        rel_add_student=(RelativeLayout)view.findViewById(R.id.rel_add_student);
        rel_add_teacher=(RelativeLayout)view.findViewById(R.id.rel_add_teacher);
        rel_add_class=(RelativeLayout)view.findViewById(R.id.rel_add_class);
        rel_add_subject=(RelativeLayout)view.findViewById(R.id.rel_add_subject);
    }

    public void initListener()
    {
        rel_add_student.setOnClickListener(this);
        rel_add_teacher.setOnClickListener(this);
        rel_add_class.setOnClickListener(this);
        rel_add_subject.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.rel_add_student:
                intent=new Intent(getActivity(),AddStudentActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.rel_add_teacher:
                intent=new Intent(getActivity(),AddTeacherActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.rel_add_class:
                intent=new Intent(getActivity(),AddClassActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.rel_add_subject:
                intent=new Intent(getActivity(),AddSubjectActivity.class);
                getActivity().startActivity(intent);
                break;
        }
    }
}
