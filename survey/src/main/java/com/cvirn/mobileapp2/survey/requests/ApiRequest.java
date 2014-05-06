package com.cvirn.mobileapp2.survey.requests;

import android.util.Base64;
import android.util.Log;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by janko on 4/30/14.
 */
public class ApiRequest {


    private static final String API_LOGIN="http://engineroom.realimpactanalytics.com/mobileapp/api/";
    private static final String ACTION_LOGIN="request";
    private static final String ACTION_PARAMS="{\"method\": \"AllData\", \"param\":\"\"}";


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

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(
                API_LOGIN
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



}
