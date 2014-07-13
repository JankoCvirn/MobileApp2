package com.cvirn.mobileapp2.survey.model;

/**
 * Created by janko on 4/30/14.
 */
public class POI {

    /*{
            "BusinessKey": "3225282325",
            "Name": "Vue sur Flagey",
            "TypeSpecificFields": [
                {
                    "Type": "text",
                    "ID": "1",
                    "Value": "Place Fagey, 18",
                    "Name": "Address"
                },
                {
                    "Type": "phone",
                    "ID": "2",
                    "Value": "+32.2.528.23.25",
                    "Name": "Phone"
                },
                {
                    "Type": "text",
                    "ID": "3",
                    "Value": "Ixelles",
                    "Name": "City"
                },
                {
                    "Type": "image",
                    "ID": "4",
                    "Value": "mobileapp/vuesurflagey_1",
                    "Name": "Picture"
                }
            ],
            "Longitude": "4.373307",
            "LastSurvey": [],
            "Latitude": "50.827859",
            "Type": "POS",
            "ID": "1",
            "Status": "",
            "CreatedBy": "MAUser1",
            "ModificationDate": "2014-05-07",
            "CreationDate": "2014-05-07",
            "Segment": "2"
        }*/

    protected String lng;
    protected String lat;
    protected String type;
    protected String sid;
    protected String status;
    protected String created_by;
    protected String mod_time;
    protected String create_time;
    protected String segment;
    protected String name;
    protected String buis_key;
    protected String city;
    protected String address;
    protected String phone;
    protected String pic;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getMod_time() {
        return mod_time;
    }

    public void setMod_time(String mod_time) {
        this.mod_time = mod_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuis_key() {
        return buis_key;
    }

    public void setBuis_key(String buis_key) {
        this.buis_key = buis_key;
    }
}
