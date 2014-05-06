package com.cvirn.mobileapp2.survey.jsonparser;

import android.util.Log;

import com.cvirn.mobileapp2.survey.model.Message;
import com.cvirn.mobileapp2.survey.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by janko on 5/6/14.
 */
public class JsonParser {

    private static final String TAG ="FLA" ;
    protected String toParse;


    public String getToParse() {
        return toParse;
    }

    public void setToParse(String toParse) {
        this.toParse = toParse;
    }

    public JsonParser(String json){
        this.toParse=json;

    }

    public ArrayList<Message> parseMessages(){

        Log.d(TAG,"Starting parse");
        ArrayList <Message> holder=new ArrayList<Message>();

        try {
            JSONArray localJSONArray1 = new JSONObject(toParse).getJSONArray("Message");
            String test=localJSONArray1.getString(0);

            for (int i=0;i<localJSONArray1.length();i++){

                JSONObject oneObject = localJSONArray1.getJSONObject(i);
                String body=oneObject.getString("test");
                String sid=oneObject.getString("ID");
                String status=oneObject.getString("status");
                String title=oneObject.getString("title");

                Message m=new Message();
                m.setBody(body);
                m.setMid(sid);
                m.setStatus(status);
                m.setTitle(title);

                Log.d(TAG,body);
                Log.d(TAG,title);
                Log.d(TAG,sid);
                Log.d(TAG,status);


                holder.add(m);


            }

            Log.d(TAG,test);



        }
        catch (Exception e){

            Log.e(TAG,"json parser exec. "+e.getLocalizedMessage());

        }

        return holder;
    }

    public User parseUser(){

        Log.d(TAG,"Starting parse");


        User u=new User();
        try {
            JSONArray localJSONArray1 = new JSONObject(toParse).getJSONArray("UserDetails");
            String test=localJSONArray1.getString(0);



                JSONObject oneObject = localJSONArray1.getJSONObject(0);
                String lastname=oneObject.getString("lastname");
                String email=oneObject.getString("email");
                String firstname=oneObject.getString("firstname");





                Log.d(TAG,lastname);
                Log.d(TAG,email);
                Log.d(TAG,firstname);








            Log.d(TAG,test);



        }
        catch (Exception e){

            Log.e(TAG,"json parser exec. "+e.getLocalizedMessage());

        }

        return u;
    }

}
