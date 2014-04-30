package com.cvirn.mobileapp2.survey.model;

/**
 * Created by janko on 4/28/14.
 */
public class Permission {

    protected String userid;
    protected String add_poi;
    protected String delete_poi;
    protected String decline_task;
    protected String online_search;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAdd_poi() {
        return add_poi;
    }

    public void setAdd_poi(String add_poi) {
        this.add_poi = add_poi;
    }

    public String getDelete_poi() {
        return delete_poi;
    }

    public void setDelete_poi(String delete_poi) {
        this.delete_poi = delete_poi;
    }

    public String getDecline_task() {
        return decline_task;
    }

    public void setDecline_task(String decline_task) {
        this.decline_task = decline_task;
    }

    public String getOnline_search() {
        return online_search;
    }

    public void setOnline_search(String online_search) {
        this.online_search = online_search;
    }
}
