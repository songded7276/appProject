package com.appsnipp.loginsamples;

public class User {
    private String username, name,GroupID, member_info;
    private int id;
    public User(int id, String username, String name,String GroupID, String Member_info) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.GroupID = GroupID;
        this.member_info = Member_info;
    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return name;
    }
    public String getGroupID() {return GroupID;}
    public String getMember_info() {return member_info;}

    public int getId() {
        return id;
    }

}