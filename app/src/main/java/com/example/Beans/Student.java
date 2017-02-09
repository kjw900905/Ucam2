package com.example.Beans;

import java.io.Serializable;

/**
 * Created by kjw90 on 2017-02-01.
 */

public class Student implements Serializable{
    private String m_Name;               //이름
    private String m_Id;                 //ID
    private String m_PW;                //PW
    private String m_Student_Num;       //학번
    private String m_University;           //학교
    private String m_Gender;            //성별

    public Student(String name, String id, String password, String student_Num, String university, String gender){
        m_Name = name;
        m_Id = id;
        m_PW = password;
        m_Student_Num = student_Num;
        m_University = university;
        m_Gender = gender;
    }

    public String getStudent_Num(){
        return m_Student_Num;
    }

    public void setStudent_Num(String student_Num){
        m_Student_Num = student_Num;
    }

    public String getName() {
        return m_Name;
    }

    public void setName(String name) {
        m_Name = name;
    }

    public String getId() {
        return m_Id;
    }

    public void setId(String id) {
        m_Id = id;
    }

    public String getPW(){
        return m_PW;
    }

    public void setPW(String pw){
        m_PW = pw;
    }

    public String getUniversity() {
        return m_University;
    }

    public void setUniversity(String university) {
        m_University = university;
    }

    public String getGender() {
        return m_Gender;
    }

    public void setGender(String gender) {
        m_Gender = gender;
    }
}
