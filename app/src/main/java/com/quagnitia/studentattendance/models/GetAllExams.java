package com.quagnitia.studentattendance.models;

/**
 * Created by admin on 2/16/2018.
 */

public class GetAllExams {

    String classname="",subject="",examtype="",examdate="",maxmarks="";

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getExamtype() {
        return examtype;
    }

    public void setExamtype(String examtype) {
        this.examtype = examtype;
    }

    public String getExamdate() {
        return examdate;
    }

    public void setExamdate(String examdate) {
        this.examdate = examdate;
    }

    public String getMaxmarks() {
        return maxmarks;
    }

    public void setMaxmarks(String maxmarks) {
        this.maxmarks = maxmarks;
    }
}
