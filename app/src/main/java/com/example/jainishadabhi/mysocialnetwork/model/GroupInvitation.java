package com.example.jainishadabhi.mysocialnetwork.model;

import java.io.Serializable;

public class GroupInvitation implements Serializable
{
    private int invitation_id;
    private String from_email_id;
    private String to_email_id;
    private int group_id;
    private String group_name;
    private String isFriend;
    private int acceptRequest;
    private int rejectRequest;

    public int getInvitation_id() {
        return invitation_id;
    }

    public void setInvitation_id(int invitation_id) {
        this.invitation_id = invitation_id;
    }

    public String getFrom_email_id() {
        return from_email_id;
    }

    public void setFrom_email_id(String from_email_id) {
        this.from_email_id = from_email_id;
    }

    public String getTo_email_id() {
        return to_email_id;
    }

    public void setTo_email_id(String to_email_id) {
        this.to_email_id = to_email_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(String isFriend) {
        this.isFriend = isFriend;
    }

    public int getAcceptRequest() {
        return acceptRequest;
    }

    public void setAcceptRequest(int acceptRequest) {
        this.acceptRequest = acceptRequest;
    }

    public int getRejectRequest() {
        return rejectRequest;
    }

    public void setRejectRequest(int rejectRequest) {
        this.rejectRequest = rejectRequest;
    }
}
