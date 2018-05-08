package com.cloverinfosoft.studentattendance.models;

/**
 * Created by admin on 2/16/2018.
 */

public class GetAllResult {
    String TeacherUserId="",StudentUserId="",StudentName="",Classname="",SubjectName="",ExamType="",ExamDate="",MaxMarks="",MarksObtained="",MinMarks="",Remark="";

    public String getTeacherUserId() {
        return TeacherUserId;
    }

    public void setTeacherUserId(String teacherUserId) {
        TeacherUserId = teacherUserId;
    }

    public String getStudentUserId() {
        return StudentUserId;
    }

    public void setStudentUserId(String studentUserId) {
        StudentUserId = studentUserId;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

    public String getClassname() {
        return Classname;
    }

    public void setClassname(String classname) {
        Classname = classname;
    }

    public String getSubjectName() {
        return SubjectName;
    }

    public void setSubjectName(String subjectName) {
        SubjectName = subjectName;
    }

    public String getExamType() {
        return ExamType;
    }

    public void setExamType(String examType) {
        ExamType = examType;
    }

    public String getExamDate() {
        return ExamDate;
    }

    public void setExamDate(String examDate) {
        ExamDate = examDate;
    }

    public String getMaxMarks() {
        return MaxMarks;
    }

    public void setMaxMarks(String maxMarks) {
        MaxMarks = maxMarks;
    }

    public String getMarksObtained() {
        return MarksObtained;
    }

    public void setMarksObtained(String marksObtained) {
        MarksObtained = marksObtained;
    }

    public String getMinMarks() {
        return MinMarks;
    }

    public void setMinMarks(String minMarks) {
        MinMarks = minMarks;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }
}
