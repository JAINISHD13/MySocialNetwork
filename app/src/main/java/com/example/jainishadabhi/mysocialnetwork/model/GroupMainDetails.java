package com.example.jainishadabhi.mysocialnetwork.model;

import java.io.Serializable;

public class GroupMainDetails implements Serializable
{
    private String email_id;
    private int group_id;
    private String group_name;
    private String group_isOwner;
    private String isFriend;

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_isOwner() {
        return group_isOwner;
    }

    public void setGroup_isOwner(String group_isOwner) {
        this.group_isOwner = group_isOwner;
    }

    public String getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(String isFriend) {
        this.isFriend = isFriend;
    }
}
