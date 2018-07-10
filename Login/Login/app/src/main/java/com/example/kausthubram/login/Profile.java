package com.example.kausthubram.login;


import java.util.ArrayList;
import java.util.List;

public class Profile implements java.io.Serializable {
    private String email,password,fullname;
    private List<Integer> project_ids;
    public Profile(){}

    public Profile(String email,String password, String fullname){
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        //this.project_ids = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword(){
        return password;
    }

    public String getFullname() {
        return fullname;
    }

    //public List<Integer> getProject_ids(){
      //  return project_ids;
    //}
}
