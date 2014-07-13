package com.cvirn.mobileapp2.survey.model;

/**
 * Created by janko on 7/6/14.
 */
public class TypeSpecFields {

    public int id;
    public String parentid;
    public String name;
    public String type;
    public String sid;
    public String seq_order;
    public String value;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSeq_order() {
        return seq_order;
    }

    public void setSeq_order(String seq_order) {
        this.seq_order = seq_order;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
