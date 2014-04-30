package com.cvirn.mobileapp2.survey.model;

/**
 * Created by janko on 4/28/14.
 */
public class User {

    protected String username;
    protected String password;
    protected String key;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
