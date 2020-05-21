package com.example.jainishadabhi.mysocialnetwork.model;
import java.io.Serializable;

public class PostDetails implements Serializable {
    private int post_id;
    private String post1;
    private String session_key;
    private String digital_signature;
    private Integer group_id;
    private Integer group_version;
    private String privacy;
    private String owner_email_id;
    private Integer original_post_id;
    private String timestamp;

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getPost1() {
        return post1;
    }

    public void setPost1(String post1) {
        this.post1 = post1;
    }

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public String getDigital_signature() {
        return digital_signature;
    }

    public void setDigital_signature(String digital_signature) {
        this.digital_signature = digital_signature;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getGroup_version() {
        return group_version;
    }

    public void setGroup_version(int group_version) {
        this.group_version = group_version;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getOwner_email_id() {
        return owner_email_id;
    }

    public void setOwner_email_id(String owner_email_id) {
        this.owner_email_id = owner_email_id;
    }

    public int getOriginal_post_id() {
        return original_post_id;
    }

    public void setOriginal_post_id(int original_post_id) {
        this.original_post_id = original_post_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
