package com.cvirn.mobileapp2.survey.model;

/**
 * Created by janko on 5/31/14.
 */
public class CheckIn {
    /*"{""method"": ""checkIN"", ""param"":
        {
        ""Latitude"": 50.828347,
        ""Longitude"": 4.371963,
        ""GSMSignalStrenght"": 10,
        ""WCDMASignalStrenght"": 10,
        ""LTESignalStrenght"": 9,
        ""GPSSignalStrenght"": 13,
        ""GSMCellID"": ""BRU155A"",
        ""GSMLAC"": ""BRU10"",
        ""SIMOperator"": ""Proximus"",
        ""CheckINDate"": ""2014-05-10 13:25:59""
}
}"*/

    protected String method;
    protected String lat;
    protected String lng;
    protected String gsmsigstren;

    public String getGpssigstren() {
        return gpssigstren;
    }

    public void setGpssigstren(String gpssigstren) {
        this.gpssigstren = gpssigstren;
    }

    protected String gpssigstren;
    protected String lte;
    protected String wcdma;
    protected String gsmcellid;
    protected String gsmlac;
    protected String simoperator;
    protected String checkindate;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getGsmsigstren() {
        return gsmsigstren;
    }

    public void setGsmsigstren(String gsmsigstren) {
        this.gsmsigstren = gsmsigstren;
    }

    public String getLte() {
        return lte;
    }

    public void setLte(String lte) {
        this.lte = lte;
    }

    public String getWcdma() {
        return wcdma;
    }

    public void setWcdma(String wcdma) {
        this.wcdma = wcdma;
    }

    public String getGsmcellid() {
        return gsmcellid;
    }

    public void setGsmcellid(String gsmcellid) {
        this.gsmcellid = gsmcellid;
    }

    public String getGsmlac() {
        return gsmlac;
    }

    public void setGsmlac(String gsmlac) {
        this.gsmlac = gsmlac;
    }

    public String getSimoperator() {
        return simoperator;
    }

    public void setSimoperator(String simoperator) {
        this.simoperator = simoperator;
    }

    public String getCheckindate() {
        return checkindate;
    }

    public void setCheckindate(String checkindate) {
        this.checkindate = checkindate;
    }
}
