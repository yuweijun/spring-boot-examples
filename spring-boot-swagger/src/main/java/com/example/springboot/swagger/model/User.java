package com.example.springboot.swagger.model;


import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    private String departmentName;

    private String loginName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", departmentName='" + departmentName + '\'' +
                ", loginName='" + loginName + '\'' +
                '}';
    }
}