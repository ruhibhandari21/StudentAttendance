package com.cloverinfosoft.studentattendance.models;

/**
 * Created by admin on 2/16/2018.
 */

public class GetAllHomework {
    String teacheruserid="",classname="",subect="",description="";

    public String getTeacheruserid() {
        return teacheruserid;
    }

    public void setTeacheruserid(String teacheruserid) {
        this.teacheruserid = teacheruserid;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getSubect() {
        return subect;
    }

    public void setSubect(String subect) {
        this.subect = subect;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
