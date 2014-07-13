package com.cvirn.mobileapp2.survey.requests;

import android.util.Base64;
import android.util.Log;

import com.cvirn.mobileapp2.survey.model.CheckIn;
import com.cvirn.mobileapp2.survey.model.POI;
import com.cvirn.mobileapp2.survey.model.User;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by janko on 4/30/14.
 */
public class ApiRequest {


    private static final String API_LOGIN="http://engineroom.realimpactanalytics.com/mobileapp/api/";
    private static final String ACTION_LOGIN="request";
    private static final String ACTION_PARAMS="{\"method\": \"AllData\", \"param\":{}}";
    private static final String ACTION_CHECKIN="request";
    private static final String TAG ="ApiRequest Class" ;
    /*public static final String API_CHECKIN="{\"method\": \"checkIN\", \"param\":\n" +
            "{\n" +
            "\"Latitude\": 50.828347,\n" +
            "\"Longitude\": 4.371963,\n" +
            "\"GSMSignalStrenght\": 10,\n" +
            "\"WCDMASignalStrenght\": 10,\n" +
            "\"LTESignalStrenght\": 9,\n" +
            "\"GPSSignalStrenght\": 13,\n" +
            "\"GSMCellID\": \"BRU155A\",\n" +
            "\"GSMLAC\": \"BRU10\",\n" +
            "\"SIMOperator\": \"Proximus\",\n" +
            "\"CheckINDate\": \"2014-05-10 13:25:59\"\n" +
            "}\n" +
            "}";*/

    protected User user;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void  LoginCheck(User p){

        this.user=p;
    }

    public String Login(){

        String json=login();
        Log.d("JSON response", json);

        return json;

    }


    private String login(){

        String url=user.getUrl();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(
                url
        );

        Log.d(TAG,"URL address:"+user.getUrl());
        String xml = "empty";
        try {

            //Set bacis auth. paramaeters
            String base64EncodedCredentials = "Basic " + Base64.encodeToString(
                    (getUser().getUsername() + ":" + getUser().getPassword()).getBytes(),
                    Base64.NO_WRAP);
            //set credentials
            httppost.setHeader("Authorization", base64EncodedCredentials);

            // Add data to your post
            List<NameValuePair> pairs = new ArrayList<NameValuePair>(2);
            //user data
            pairs.add(new BasicNameValuePair(ACTION_LOGIN, ACTION_PARAMS));
            //pairs.add(new BasicNameValuePair("password", getProfile().getPassword()));

            //action
            pairs.add(new BasicNameValuePair("action",ACTION_LOGIN ));
            httppost.setEntity(new UrlEncodedFormEntity(pairs));

            // Finally, execute the request
            HttpResponse webServerAnswer = httpclient.execute(httppost);

            HttpEntity httpEntity = webServerAnswer.getEntity();

            if (httpEntity != null) {

                xml = EntityUtils.toString(httpEntity);
                Log.d("XML", xml);
            }

        } catch (ClientProtocolException e) {
            // Deal with it
        } catch (IOException e) {
            // Deal with it
        }

        return xml;


    }

    public String checkin(CheckIn c){

        String url=getUser().getUrl();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(
               url
        );
        String xml = "empty";
        try {

            //Set bacis auth. paramaeters
            String base64EncodedCredentials = "Basic " + Base64.encodeToString(
                    (getUser().getUsername() + ":" + getUser().getPassword()).getBytes(),
                    Base64.NO_WRAP);
            //set credentials
            httppost.setHeader("Authorization", base64EncodedCredentials);

            // Add data to your post
            List<NameValuePair> pairs = new ArrayList<NameValuePair>(2);
            //user data
            String payLoad="{\"method\": \"checkIN\", \"param\":\n" +
                    "{\n" +
                    "\"Latitude\": "+c.getLat()+",\n" +
                    "\"Longitude\": "+c.getLng()+",\n" +
                    "\"GSMSignalStrenght\": "+c.getGsmsigstren()+",\n" +
                    "\"WCDMASignalStrenght\": 10,\n" +
                    "\"LTESignalStrenght\": 9,\n" +
                    "\"GPSSignalStrenght\": "+c.getGpssigstren()+",\n" +
                    "\"GSMCellID\": \"BRU155A\",\n" +
                    "\"GSMLAC\": \""+c.getGsmcellid()+"\",\n" +
                    "\"SIMOperator\": \""+c.getSimoperator()+"\",\n" +
                    "\"CheckINDate\": \"2014-05-10 13:25:59\"\n" +
                    "}\n" +
                    "}";
            pairs.add(new BasicNameValuePair(ACTION_CHECKIN,payLoad ));
            //pairs.add(new BasicNameValuePair("password", getProfile().getPassword()));

            //action
            pairs.add(new BasicNameValuePair("action",ACTION_LOGIN ));
            httppost.setEntity(new UrlEncodedFormEntity(pairs));

            // Finally, execute the request
            HttpResponse webServerAnswer = httpclient.execute(httppost);

            HttpEntity httpEntity = webServerAnswer.getEntity();

            if (httpEntity != null) {

                xml = EntityUtils.toString(httpEntity);
                Log.d("JSON", xml);
            }

        } catch (ClientProtocolException e) {
            // Deal with it
        } catch (IOException e) {
            // Deal with it
        }

        return xml;


    }

/*
* New POI
* {"method": "addPOI", "param":
	{
	"POIID": 1,
	"BusinessKey": 1,
	"Name": "Sushi Shop",
	"Type": "POS",
	"Status": "active",
	"Segment": "Brussels",
	"Latitude": 50.829469,
	"Longitude": 4.362033,
	"typespecificfields":[{
		"fieldid": 1,
		"fieldvalue" : "avenue Louise, 144"
		}
		]
	}
}
* */

    public String setNewPoi(POI p){

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(
                getUser().getUrl()
        );
        Log.d(TAG,"URL address:"+user.getUrl());
        String xml = "empty";
        try {

            //Set bacis auth. paramaeters
            String base64EncodedCredentials = "Basic " + Base64.encodeToString(
                    (getUser().getUsername() + ":" + getUser().getPassword()).getBytes(),
                    Base64.NO_WRAP);
            //set credentials
            httppost.setHeader("Authorization", base64EncodedCredentials);

            // Add data to your post
            List<NameValuePair> pairs = new ArrayList<NameValuePair>(2);
            //user data
            String payLoad="{\n" +
                    "\"method\":\"addPOI\",\n" +
                    "\"param\":{\n" +
                    "\"BusinessKey\":"+p.getBuis_key()+",\n" +
                    "\"Name\":\""+p.getName()+"\",\n" +
                    "\"Type\":\""+p.getType()+"\",\n" +
                    "\"Status\":\""+p.getStatus()+"\",\n" +
                    "\"Segment\":\""+p.getCity()+"\",\n" +
                    "\"Latitude\":"+p.getLat()+",\n" +
                    "\"Longitude\":"+p.getLng()+",\n" +
                    "\"typespecificfields\":[\n" +
                    "{\n" +
                    "\"fieldid\":1,\n" +
                    "\"fieldvalue\":\""+p.getAddress()+"\"\n" +
                    "},\n" +
                    "{\n" +
                    "\"fieldid\":2,\n" +
                    "\"fieldvalue\":\""+p.getPhone()+"\"\n" +
                    "},\n" +
                    "{\n" +
                    "\"fieldid\":3,\n" +
                    "\"fieldvalue\":\""+p.getCity()+"\"\n" +
                    "},\n" +
                    "{\n" +
                    "\"fieldid\":4,\n" +
                    "\"fieldvalue\":\""+p.getPic()+"\"\n" +
                    "}\n" +
                    "]\n" +
                    "}\n" +
                    "}";
            pairs.add(new BasicNameValuePair(ACTION_CHECKIN,payLoad ));
            //pairs.add(new BasicNameValuePair("password", getProfile().getPassword()));
            Log.d("Sending JSON",payLoad);
            //action
            pairs.add(new BasicNameValuePair("action",ACTION_LOGIN ));
            httppost.setEntity(new UrlEncodedFormEntity(pairs));

            // Finally, execute the request
            HttpResponse webServerAnswer = httpclient.execute(httppost);

            HttpEntity httpEntity = webServerAnswer.getEntity();

            if (httpEntity != null) {

                xml = EntityUtils.toString(httpEntity);
                Log.d("JSON", xml);
            }

        } catch (ClientProtocolException e) {
            // Deal with it
        } catch (IOException e) {
            // Deal with it
        }

        return xml;


    }

    /*
    {"method": "searchPOI", "param":
	{
	"Latitude": 50.827878,
	"Longitude": 4.373316,
	"SearchString":"Vue"
	}
}
    * */


    public String search(POI p,String search){

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(
                getUser().getUrl()
        );
        String xml = "empty";
        try {

            //Set bacis auth. paramaeters
            String base64EncodedCredentials = "Basic " + Base64.encodeToString(
                    (getUser().getUsername() + ":" + getUser().getPassword()).getBytes(),
                    Base64.NO_WRAP);
            //set credentials
            httppost.setHeader("Authorization", base64EncodedCredentials);

            // Add data to your post
            List<NameValuePair> pairs = new ArrayList<NameValuePair>(2);
            //user data
            String payLoad="{\n" +
                    "\"method\":\"searchPOI\",\n" +
                    "\"param\":{\n" +
                    "\"Latitude\":"+p.getLat()+",\n" +
                    "\"Longitude\":"+p.getLng()+",\n" +
                    "\"SearchString\":\""+search+"\"\n" +
                    "}\n" +
                    "}";
            pairs.add(new BasicNameValuePair(ACTION_CHECKIN,payLoad ));
            //pairs.add(new BasicNameValuePair("password", getProfile().getPassword()));

            //action
            pairs.add(new BasicNameValuePair("action",ACTION_LOGIN ));
            httppost.setEntity(new UrlEncodedFormEntity(pairs));

            // Finally, execute the request
            HttpResponse webServerAnswer = httpclient.execute(httppost);

            HttpEntity httpEntity = webServerAnswer.getEntity();

            if (httpEntity != null) {

                xml = EntityUtils.toString(httpEntity);
                Log.d("JSON", xml);
            }

        } catch (ClientProtocolException e) {
            // Deal with it
        } catch (IOException e) {
            // Deal with it
        }

        return xml;


    }

    public String setSurvey(String[] answers,String poi_id){

        Log.d(TAG,"url to post to:"+getUser().getUrl());
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(
                getUser().getUrl()
        );
        String xml = "empty";
        try {

            //Set bacis auth. paramaeters
            String base64EncodedCredentials = "Basic " + Base64.encodeToString(
                    (getUser().getUsername() + ":" + getUser().getPassword()).getBytes(),
                    Base64.NO_WRAP);
            //set credentials
            httppost.setHeader("Authorization", base64EncodedCredentials);

            // Add data to your post
            List<NameValuePair> pairs = new ArrayList<NameValuePair>(2);
            //user data
            String payLoad="{\n" +
                    "\"method\":\"addSurvey\",\n" +
                    "\"param\":{\n" +
                    "\"form\":1,\n" +
                    "\"checkIN\":\"3\",\n" +
                    "\"POI\":\""+poi_id+"\",\n" +
                    "\"responses\":[\n" +
                    "{\n" +
                    "\"question\":1,\n" +
                    "\"value\":\""+answers[1]+"\"\n" +
                    "},\n" +
                    "{\n" +
                    "\"question\":2,\n" +
                    "\"value\":\""+answers[2]+"\"\n" +
                    "},\n" +
                    "{\n" +
                    "\"question\":3,\n" +
                    "\"value\":\""+answers[3]+"\"\n" +
                    "},\n" +
                    "{\n" +
                    "\"question\":4,\n" +
                    "\"value\":\""+answers[4].replaceAll("\n","")+"\"\n" +
                    "}\n" +
                    "]\n" +
                    "}\n" +
                    "}";

            writeToSDFile(payLoad);
            pairs.add(new BasicNameValuePair(ACTION_CHECKIN,payLoad ));
            //pairs.add(new BasicNameValuePair("password", getProfile().getPassword()));
            Log.d("Sending JSON",payLoad);
            //action
            pairs.add(new BasicNameValuePair("action",ACTION_LOGIN ));
            httppost.setEntity(new UrlEncodedFormEntity(pairs));

            // Finally, execute the request
            HttpResponse webServerAnswer = httpclient.execute(httppost);

            HttpEntity httpEntity = webServerAnswer.getEntity();

            if (httpEntity != null) {

                xml = EntityUtils.toString(httpEntity);
                Log.d("JSON", xml);
            }

        } catch (ClientProtocolException e) {
            // Deal with it
        } catch (IOException e) {
            // Deal with it
        }

        return xml;


    }

    private void writeToSDFile(String payload){



        File root = android.os.Environment.getExternalStorageDirectory();


        File dir = new File (root.getAbsolutePath() + "/survey");
        dir.mkdirs();
        File file = new File(dir, "myData.txt");

        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.println(payload.replaceAll("\n",""));
           // pw.println("Hello");
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("APIRequest", "******* File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //tv.append("\n\nFile written to "+file);
    }




}
