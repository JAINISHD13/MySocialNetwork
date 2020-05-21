package com.example.jainishadabhi.mysocialnetwork.model;

import java.io.Serializable;

public class GroupKeyDetails implements Serializable
{
    private int group_key_id;
    private String email_id;
    private int group_id;
    private String group_key1;
    private int group_version;

    public int getGroup_key_id() {
        return group_key_id;
    }

    public void setGroup_key_id(int group_key_id) {
        this.group_key_id = group_key_id;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getGroup_key1() {
        return group_key1;
    }

    public void setGroup_key1(String group_key1) {
        this.group_key1 = group_key1;
    }

    public int getGroup_version() {
        return group_version;
    }

    public void setGroup_version(int group_version) {
        this.group_version = group_version;
    }
}
