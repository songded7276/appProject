package com.appsnipp.loginsamples;

public class User {
    private String username, name,GroupID, prepare,Verify,Approve;
    private int id;
    public User(int id, String username, String name,String GroupID, String IsPrepare, String IsVerify,String IsApprove) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.GroupID = GroupID;
        this.prepare = IsPrepare;
        this.Verify = IsVerify;
        this.Approve = IsApprove;
    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return name;
    }
    public String getGroupID() {return GroupID;}
    public String getPrepare() {return prepare;}
    public String getVerify() {return Verify;}
    public String getApprove() {return Approve;}

    public int getId() {
        return id;
    }

}