package com.cloverinfosoft.studentattendance.Services;

import org.json.JSONObject;

public interface OnTaskCompleted {

    void onTaskCompleted(JSONObject jsonObject, String result, String TAG) throws Exception;

//    String uploadData(String url);

}